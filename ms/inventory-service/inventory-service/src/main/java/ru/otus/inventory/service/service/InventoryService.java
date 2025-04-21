package ru.otus.inventory.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.inventory.lib.api.ProductBalanceResponseDto;
import ru.otus.inventory.service.model.entity.Inventory;
import ru.otus.inventory.service.model.entity.ReservedProduct;
import ru.otus.inventory.service.model.entity.ReservedProductId;
import ru.otus.inventory.service.repository.InventoryRepository;
import ru.otus.inventory.service.repository.ReservedProductRepository;
import ru.otus.lib.kafka.model.ReleaseIngredientsModel;
import ru.otus.lib.kafka.model.ReleaseType;
import ru.otus.lib.kafka.model.ReservationConfirmationModel;
import ru.otus.lib.kafka.model.ReservationProcessModel;
import ru.otus.lib.kafka.service.BusinessTopics;
import ru.otus.lib.kafka.service.KafkaProducerService;
import ru.otus.menu.lib.api.MenuServiceClient;
import ru.otus.menu.lib.api.RecipeResponseDto;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final String RECIPE_CASH_NAME = "dishRecipes";

    private final InventoryRepository inventoryRepository;
    private final ReservedProductRepository reservedProductRepository;
    private final MenuServiceClient menuServiceClient;
    private final CacheManager cacheManager;
    private final KafkaProducerService kafkaProducerService;

    public ProductBalanceResponseDto getBalance(List<UUID> productIds) {
        if (CollectionUtils.isEmpty(productIds)) {
            log.warn("No products found for ids: {}", productIds);
            return new ProductBalanceResponseDto(Map.of());
        }

        var values = inventoryRepository.findAllById(productIds);
        if (CollectionUtils.isEmpty(values)) {
            log.warn("No product balance found for ids: {}", productIds);
            return new ProductBalanceResponseDto(Map.of());
        }

        var map = values.stream().collect(Collectors.toMap(Inventory::getProductId, Inventory::getQuantity));
        return new ProductBalanceResponseDto(map);
    }

    @Transactional
    public void processReservation(ReservationProcessModel model) {
        var dishIds = new ArrayList<>(model.getDishQuantityMap().keySet());
        var orderId = model.getOrderId();
        log.debug("Trying to check products for dishes with ids: {} for order with id: {}", dishIds, orderId);

        var cachedRecipes = getCachedRecipes(dishIds);
        var missingIds = dishIds.stream()
                .filter(id -> !cachedRecipes.containsKey(id))
                .toList();

        if (!missingIds.isEmpty()) {
            var freshRecipes = menuServiceClient.getRecipes(dishIds);
            freshRecipes.forEach(recipe ->
                    cacheManager.getCache(RECIPE_CASH_NAME).put(recipe.getDishId(), recipe)
            );
            cachedRecipes.putAll(freshRecipes.stream()
                    .collect(Collectors.toMap(RecipeResponseDto::getDishId, Function.identity())));
        }

        var productQuantityList = cachedRecipes.values().stream()
                .map(m -> m.getProductQuantityMap().entrySet())
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                ));

        var productIds = productQuantityList.keySet();
        var inventories = inventoryRepository.findAllById(productIds);
        if (CollectionUtils.isEmpty(inventories)) {
            log.error("Unable to cook all dishes with ids: {}. Inventory is empty for order with id: {}", dishIds, orderId);
            kafkaProducerService.send(
                    BusinessTopics.ORDER_RESERVATION_CONFIRMATION,
                    ReservationConfirmationModel.error(orderId, "Inventory is empty")
            );
        }

        //каких продуктов не хватает
        var shortageIds = inventories.stream()
                .filter(i -> {
                    var productId = i.getProductId();
                    var requestedQuantity = productQuantityList.get(productId);
                    return requestedQuantity == null || requestedQuantity > i.getQuantity();
                })
                .map(Inventory::getProductId)
                .toList();

        if (!CollectionUtils.isEmpty(shortageIds)) {
            log.error("Unable to reserve products, there is lack of products with ids: {} for the order with id: {}", shortageIds, orderId);
            kafkaProducerService.send(
                    BusinessTopics.ORDER_RESERVATION_CONFIRMATION,
                    ReservationConfirmationModel.error(orderId, "Some inventory is empty or are not enough")
            );
        }

        log.debug("Shortage products id list is empty! Everything is fine, reserving products...");
        var reservedProductList = productQuantityList.entrySet().stream()
                .map(entry -> {
                    var id = ReservedProductId.builder()
                            .orderId(orderId)
                            .productId(entry.getKey())
                            .build();
                    return ReservedProduct.builder()
                            .id(id)
                            .quantity(entry.getValue())
                            .build();
                })
                .collect(Collectors.toList());
        reservedProductRepository.saveAll(reservedProductList);

        kafkaProducerService.send(
                BusinessTopics.ORDER_RESERVATION_CONFIRMATION,
                ReservationConfirmationModel.success(orderId)
        );
    }

    @Transactional
    public void processRelease(ReleaseIngredientsModel model) {
        var orderId = model.getOrderId();
        var releaseType = model.getType();

        if (ReleaseType.COOKING == releaseType) {
            var reservedProducts = reservedProductRepository.findAllByOrderId(orderId);
            if (CollectionUtils.isEmpty(reservedProducts)) {
                log.warn("Products for order with id {} already released", orderId);
                return;
            }
            var productMap = reservedProducts.stream()
                    .collect(Collectors.toMap(
                            i -> i.getId().getProductId(),
                            i -> i.getQuantity(),
                            (i1, i2) -> i1 + i2));
            var productIds = productMap.keySet();
            var inventories = inventoryRepository.findForUpdateAllByProductIds(productIds);
            if (CollectionUtils.isEmpty(inventories)) {
                log.warn("Inventory does not contain products with ids: {}", productIds);
                return;
            }

            inventories.forEach(i -> {
                var updatedQuantity = i.getQuantity() - productMap.getOrDefault(i.getProductId(), 0);
                i.setQuantity(updatedQuantity);
            });

            inventoryRepository.saveAll(inventories);
        }

        reservedProductRepository.deleteAllByOrderId(orderId);
    }

    private Map<UUID, RecipeResponseDto> getCachedRecipes(List<UUID> ids) {
        Cache cache = cacheManager.getCache(RECIPE_CASH_NAME);
        return ids.stream()
                .map(id -> {
                    Cache.ValueWrapper wrapper = cache.get(id);
                    return wrapper != null ? (RecipeResponseDto) wrapper.get() : null;
                })
                .filter(v -> v != null)
                .collect(Collectors.toMap(RecipeResponseDto::getDishId, Function.identity()));
    }
}

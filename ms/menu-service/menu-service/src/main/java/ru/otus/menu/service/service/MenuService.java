package ru.otus.menu.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.common.error.BusinessAppException;
import ru.otus.inventory.lib.api.InventoryServiceClient;
import ru.otus.inventory.lib.api.ProductBalanceResponseDto;
import ru.otus.menu.lib.api.RecipeResponseDto;
import ru.otus.menu.lib.api.DishResponseDto;
import ru.otus.menu.service.mapper.MenuMapper;
import ru.otus.menu.service.model.dto.MenuListResponseDto;
import ru.otus.menu.service.model.entity.Dish;
import ru.otus.menu.service.model.entity.DishProduct;
import ru.otus.menu.service.repository.DishProductRepository;
import ru.otus.menu.service.repository.DishRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuMapper mapper;
    private final DishRepository repository;
    private final DishProductRepository dishProductRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Transactional
    public MenuListResponseDto getActualMenu() {
        var dishes = repository.findAllWithProducts();
        if (CollectionUtils.isEmpty(dishes)) {
            log.warn("No dishes info");
            return new MenuListResponseDto();
        }

        var filteredDishes = processDishes(dishes, Map.of());

        var menu = mapper.mapMenu(filteredDishes);
        return menu;
    }

    @Transactional
    public DishResponseDto getById(Integer dishId, Integer quantity) {
        var dishOpt = repository.findById(dishId);
        if (dishOpt.isEmpty()) {
            log.error("Dish with id {} not found", dishId);
            throw new NoSuchElementException("Dish with id " + dishId + " not found");
        }

        var map = Map.of(dishId, quantity);
        var dish = dishOpt.get();
        var filteredDish = processDishes(List.of(dish), map);
        var resp = mapper.mapDish(dish);
        if (CollectionUtils.isEmpty(filteredDish)) {
            log.error("Quantity of products in inventory is not enough for the dish with id {} and quantity {}", dishId, quantity);
            resp.setIsAvailable(false);
        } else {
            log.debug("There is enough products for the dish with id: {} and quantity: {}", dishId, quantity);
            resp.setIsAvailable(true);
        }
        return resp;
    }

    @Transactional
    public RecipeResponseDto getRecipes(List<Integer> dishIds) {
        var dishProducts = dishProductRepository.findAllByDishIds(dishIds);
        if (CollectionUtils.isEmpty(dishProducts)) {
            log.warn("Dish list for ids: {} is empty", dishIds);
            return new RecipeResponseDto(Map.of());
        }

        var dishProductQuantityMap = dishProducts.stream()
                .collect(Collectors.groupingBy(dp -> dp.getId().getDishId()))
                .entrySet().stream().collect(Collectors.toMap(
                        i -> i.getKey(),
                        i -> i.getValue().stream()
                                .collect(Collectors.toMap(
                                        v -> v.getId().getProductId(),
                                        v -> v.getQuantity(),
                                        (v1, v2) -> v1 + v2)
                                )

                ));
        return new RecipeResponseDto(dishProductQuantityMap);
    }

    private List<Dish> processDishes(List<Dish> dishes, Map<Integer, Integer> dishQuantityMap) {
        var productQuantityMap = dishes.stream()
                .filter(d -> !CollectionUtils.isEmpty(d.getProducts()))
                .flatMap(dish ->
                    dish.getProducts().stream()
                            .peek(p ->
                                    p.setQuantity(
                                            p.getQuantity()
                                                    * dishQuantityMap.getOrDefault(p.getId().getDishId(), 1)))
                )
                .collect(Collectors.groupingBy(
                        d -> d.getId().getProductId(),
                        Collectors.summingInt(DishProduct::getQuantity)
                ));
        var productIds = productQuantityMap.keySet();

        ProductBalanceResponseDto actualBalance = null;
        try {
            actualBalance = inventoryServiceClient.getActualBalance(new ArrayList<>(productIds));
        } catch (Exception e) {
            log.error("Error while getting actual balance from inventory-service. Skip the result");
        }

        if (actualBalance == null) {
            log.warn("No data about current product balance");
            return dishes;
        }

        var balance = actualBalance.getBalance();

        var dishesToExclude = dishes.stream()
                .filter(d -> d.getProducts().stream()
                        .anyMatch(dp -> dp.getQuantity() > balance.getOrDefault(dp.getId().getProductId(), 0))
                )
                .map(Dish::getId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(dishesToExclude)) {
            log.debug("No dishes to exclude. Everything is fine!");
            return dishes;
        } else {
            log.warn("The list of dished to exclude because there is a lack of products: {}", dishesToExclude);
            return dishes.stream()
                    .filter(d -> !dishesToExclude.contains(d.getId()))
                    .collect(Collectors.toList());
        }
    }
}

package ru.otus.inventory.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.inventory.lib.api.ProductBalanceResponseDto;
import ru.otus.inventory.service.model.entity.Inventory;
import ru.otus.inventory.service.repository.InventoryRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

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
}

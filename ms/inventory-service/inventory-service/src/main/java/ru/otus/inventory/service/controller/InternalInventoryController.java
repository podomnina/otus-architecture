package ru.otus.inventory.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.inventory.lib.api.InventoryServiceClient;
import ru.otus.inventory.lib.api.ProductBalanceResponseDto;
import ru.otus.inventory.service.service.InventoryService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping(InventoryServiceClient.BASE_INTERNAL_URL)
@RequiredArgsConstructor
public class InternalInventoryController implements InventoryServiceClient {

    private final InventoryService service;

    @Override
    @GetMapping(InventoryServiceClient.BALANCE_URL)
    public ProductBalanceResponseDto getActualBalance(@RequestParam List<UUID> productIds) {
        log.debug("Trying to check balance for products with ids: {}", productIds);
        return service.getBalance(productIds);
    }

}

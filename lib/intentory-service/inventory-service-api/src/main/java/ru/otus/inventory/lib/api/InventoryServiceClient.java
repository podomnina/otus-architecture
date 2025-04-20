package ru.otus.inventory.lib.api;


import java.util.List;
import java.util.UUID;

public interface InventoryServiceClient {

    String BASE_INTERNAL_URL = "/api/internal/inventory";
    String BALANCE_URL = "/balance";

    ProductBalanceResponseDto getActualBalance(List<UUID> productIds);
}

package ru.otus.menu.lib.api;

import java.util.UUID;

public interface MenuServiceClient {

    String BASE_INTERNAL_URL = "/api/internal/menu";

    String DISH_URL = "/dish";

    DishResponseDto getById(UUID dishId, Integer quantity);
}

package ru.otus.menu.lib.api;

import java.util.List;
import java.util.UUID;

public interface MenuServiceClient {

    String BASE_INTERNAL_URL = "/api/internal/menu";

    String DISH_URL = "/dish";

    String RECIPE_URL = "/recipe";

    DishResponseDto getById(UUID dishId, Integer quantity);

    List<RecipeResponseDto> getRecipes(List<UUID> dishIds);
}

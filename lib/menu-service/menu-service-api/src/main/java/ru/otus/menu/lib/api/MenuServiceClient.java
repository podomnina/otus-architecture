package ru.otus.menu.lib.api;

import java.util.List;

public interface MenuServiceClient {

    String BASE_INTERNAL_URL = "/api/internal/menu";

    String DISH_URL = "/dish";

    String RECIPE_URL = "/recipe";

    DishResponseDto getById(Integer dishId, Integer quantity);

    RecipeResponseDto getRecipes(List<Integer> dishIds);
}

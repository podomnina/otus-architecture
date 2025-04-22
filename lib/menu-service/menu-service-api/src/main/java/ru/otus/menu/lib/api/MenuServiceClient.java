package ru.otus.menu.lib.api;

import java.util.List;

public interface MenuServiceClient {

    String BASE_INTERNAL_URL = "/api/internal/menu";

    String DISH_URL = "/dish";

    String LIST_URL = "/list";

    String RECIPE_URL = "/recipe";

    DishResponseDto getById(Integer dishId, Integer quantity);

    DishListResponseDto getByIds(List<Integer> dishId);

    RecipeResponseDto getRecipes(List<Integer> dishIds);
}

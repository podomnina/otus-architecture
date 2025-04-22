package ru.otus.menu.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.menu.lib.api.DishListResponseDto;
import ru.otus.menu.lib.api.RecipeResponseDto;
import ru.otus.menu.lib.api.DishResponseDto;
import ru.otus.menu.lib.api.MenuServiceClient;
import ru.otus.menu.service.service.MenuService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping(MenuServiceClient.BASE_INTERNAL_URL)
@RequiredArgsConstructor
public class InternalMenuController implements MenuServiceClient {

    private final MenuService service;

    @Override
    @GetMapping(MenuServiceClient.DISH_URL + "/{dishId}")
    public DishResponseDto getById(@PathVariable Integer dishId,
                                   @RequestParam Integer quantity) {
        log.debug("Trying to get dish by id: {}", dishId);
        return service.getById(dishId, quantity);
    }

    @Override
    @GetMapping(MenuServiceClient.DISH_URL + MenuServiceClient.LIST_URL)
    public DishListResponseDto getByIds(@RequestParam List<Integer> dishIds) {
        return service.getByIds(dishIds);
    }

    @Override
    @GetMapping(MenuServiceClient.RECIPE_URL)
    public RecipeResponseDto getRecipes(@RequestParam List<Integer> dishIds) {
        log.debug("Trying to get dish products for dishes: {}", dishIds);
        return service.getRecipes(dishIds);
    }
}

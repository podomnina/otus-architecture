package ru.otus.menu.service.mapper;

import org.mapstruct.Mapper;
import org.springframework.util.CollectionUtils;
import ru.otus.menu.lib.api.DishResponseDto;
import ru.otus.menu.service.model.DishCategory;
import ru.otus.menu.service.model.dto.MenuItemDto;
import ru.otus.menu.service.model.dto.MenuListResponseDto;
import ru.otus.menu.service.model.entity.Dish;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    MenuItemDto map(Dish dish);

    DishResponseDto mapDish(Dish dish);

    default List<MenuItemDto> map(List<Dish> dishes) {
        if (CollectionUtils.isEmpty(dishes)) {
            return new ArrayList<>();
        }

        return dishes.stream().map(this::map)
                .sorted(Comparator.comparing(MenuItemDto::getPrice).reversed())
                .collect(Collectors.toList());
    }

    default MenuListResponseDto mapMenu(List<Dish> dishes) {
        if (CollectionUtils.isEmpty(dishes)) {
            return new MenuListResponseDto();
        }

        var categoryMap = dishes.stream()
                .filter(d -> d.getCategory() != null)
                .collect(Collectors.groupingBy(Dish::getCategory));

        var dto = new MenuListResponseDto();
        dto.setStarters(map(categoryMap.get(DishCategory.STARTERS)));
        dto.setSalad(map(categoryMap.get(DishCategory.SALAD)));
        dto.setSoup(map(categoryMap.get(DishCategory.SOUP)));
        dto.setMeat(map(categoryMap.get(DishCategory.MEAT)));
        dto.setFish(map(categoryMap.get(DishCategory.FISH)));
        dto.setGarnish(map(categoryMap.get(DishCategory.GARNISH)));
        dto.setDessert(map(categoryMap.get(DishCategory.DESSERT)));
        dto.setDrinks(map(categoryMap.get(DishCategory.DRINKS)));
        return dto;
    }
}

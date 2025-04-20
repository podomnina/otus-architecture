package ru.otus.menu.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.inventory.lib.api.InventoryServiceClient;
import ru.otus.menu.lib.api.RecipeResponseDto;
import ru.otus.menu.lib.api.DishResponseDto;
import ru.otus.menu.service.mapper.MenuMapper;
import ru.otus.menu.service.model.dto.MenuListResponseDto;
import ru.otus.menu.service.model.entity.Dish;
import ru.otus.menu.service.model.entity.DishProduct;
import ru.otus.menu.service.repository.DishProductRepository;
import ru.otus.menu.service.repository.DishRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuMapper mapper;
    private final DishRepository repository;
    private final DishProductRepository dishProductRepository;
    private final InventoryServiceClient inventoryServiceClient;

    public MenuListResponseDto getActualMenu() {
        var dishes = repository.findAll();
        if (CollectionUtils.isEmpty(dishes)) {
            log.warn("No dishes info");
            return new MenuListResponseDto();
        }

        var filteredDishes = processDishes(dishes);

        var menu = mapper.mapMenu(filteredDishes);
        return menu;
    }

    public DishResponseDto getById(UUID dishId, Integer quantity) {
        var dish = repository.findById(dishId);
        if (dish.isEmpty()) {
            log.error("Dish with id {} not found", dishId);
            throw new NoSuchElementException("Dish with id " + dishId + " not found");
        }
        //todo check with quantity!!!
        var filteredDish = processDishes(List.of(dish.get()));
        return mapper.mapDish(filteredDish.get(0)); //todo check!!!
    }

    public List<RecipeResponseDto> getRecipes(List<UUID> dishIds) {
        var dishProducts = dishProductRepository.findAllByDishIds(dishIds);
        if (CollectionUtils.isEmpty(dishProducts)) {
            log.warn("Dish list for ids: {} is empty", dishIds);
            return List.of();
        }

        return dishProducts.stream().collect(Collectors.groupingBy(dp -> dp.getId().getDishId()))
                .entrySet().stream().map(entry -> {
                    var map = entry.getValue().stream()
                            .collect(Collectors.toMap(
                                    v -> v.getId().getProductId(),
                                    v -> v.getQuantity(),
                                    (v1, v2) -> v1 + v2)
                            );
                    return RecipeResponseDto.builder()
                            .dishId(entry.getKey())
                            .productQuantityMap(map)
                            .build();
                }).collect(Collectors.toList());
    }

    private List<Dish> processDishes(List<Dish> dishes) {
        var productQuantityMap = dishes.stream() //todo может мапа не нужна
                .filter(d -> !CollectionUtils.isEmpty(d.getProducts()))
                .flatMap(dish -> dish.getProducts().stream())
                .collect(Collectors.groupingBy(
                        d -> d.getId().getProductId(),
                        Collectors.summingInt(DishProduct::getQuantity)
                ));
        var productIds = productQuantityMap.keySet();

        var actualBalance = inventoryServiceClient.getActualBalance(new ArrayList<>(productIds));
        if (actualBalance == null || CollectionUtils.isEmpty(actualBalance.getBalance())) {
            log.warn("No data about current product balance");
            return dishes;
        }

        var balance = actualBalance.getBalance();

        var dishesToExclude = dishes.stream()
                .filter(d -> d.getProducts().stream()
                        .anyMatch(dp -> dp.getQuantity() > balance.get(dp.getId().getProductId()))
                )
                .map(Dish::getId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(dishesToExclude)) {
            log.debug("No dishes to exclude. Everything is fine!");
            return dishes;
        } else {
            log.warn("The list of dished to exclude because there is a lack of products: {}", dishesToExclude);
            return dishes.stream()
                    .filter(d -> dishesToExclude.contains(d.getId()))
                    .collect(Collectors.toList());
        }
    }
}

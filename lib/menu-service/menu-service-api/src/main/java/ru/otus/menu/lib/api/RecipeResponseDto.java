package ru.otus.menu.lib.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * Только показывает состав блюда и сколько нужно ингредиентов
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponseDto {
    private UUID dishId;
    private Map<UUID, Integer> productQuantityMap;
}

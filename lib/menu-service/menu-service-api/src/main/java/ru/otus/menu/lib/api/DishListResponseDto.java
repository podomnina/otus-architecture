package ru.otus.menu.lib.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishListResponseDto {
    private List<DishResponseDto> items;
}

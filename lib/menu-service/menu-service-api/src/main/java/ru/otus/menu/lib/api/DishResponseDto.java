package ru.otus.menu.lib.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishResponseDto {
    private UUID dishId;
    private String name;
    private Integer price;
    private Boolean isAvailable;
}

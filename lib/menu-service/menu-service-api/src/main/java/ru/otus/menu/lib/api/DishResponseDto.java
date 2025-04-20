package ru.otus.menu.lib.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishResponseDto {
    private UUID dishId;
    private String name;
    private BigDecimal price;
    private Boolean isAvailable;
}

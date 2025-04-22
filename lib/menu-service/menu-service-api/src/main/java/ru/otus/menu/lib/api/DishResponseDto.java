package ru.otus.menu.lib.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishResponseDto {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Boolean isAvailable;
}

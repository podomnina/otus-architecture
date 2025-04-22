package ru.otus.menu.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDto {
    private Integer id;
    private String name;
    private BigDecimal price;
}
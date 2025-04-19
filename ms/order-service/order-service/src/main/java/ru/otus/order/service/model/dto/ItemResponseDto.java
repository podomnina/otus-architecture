package ru.otus.order.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {
    private UUID dishId;
    private String name;
    private Integer price;
    private Integer quantity;
    private Boolean isAvailable;
}

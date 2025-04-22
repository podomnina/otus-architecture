package ru.otus.order.service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemId {
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name = "dish_id")
    private Integer dishId;
}

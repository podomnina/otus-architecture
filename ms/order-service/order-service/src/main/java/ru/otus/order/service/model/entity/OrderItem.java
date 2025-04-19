package ru.otus.order.service.model.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "order_item", schema = "order")
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;
    private Integer price;
    private Integer quantity;
}

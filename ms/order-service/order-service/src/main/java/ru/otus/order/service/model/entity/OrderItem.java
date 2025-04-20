package ru.otus.order.service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_item", schema = "order")
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;
    @Column(precision = 8, scale = 2)
    private BigDecimal price;
    private Integer quantity;
}

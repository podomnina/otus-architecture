package ru.otus.order.service.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_item", schema = "order")
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;

    @Column(precision = 8, scale = 2)
    private BigDecimal price;
    private Integer quantity;

    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}

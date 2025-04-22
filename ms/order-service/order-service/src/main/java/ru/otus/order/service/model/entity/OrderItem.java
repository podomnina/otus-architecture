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
@Table(name = "order_item", schema = "\"order\"")
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;
    private String name;

    @Column(precision = 8, scale = 2)
    private BigDecimal price;
    private Integer quantity;

    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    //@Column(name = "dish_id", insertable = false, updatable = false)
    //private Integer dishId;

    //@PrePersist
    //protected void onCreate() {
    //    if (this.id == null) {
    //        this.id = new OrderItemId();
    //    }
//
    //    if (this.dishId != null) {
    //        this.id.setDishId(this.dishId);
    //    }
    //}
}

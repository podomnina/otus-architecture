package ru.otus.order.service.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import ru.otus.order.service.model.OrderStatus;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "order", schema = "order")
public class Order {
    @Id
    @UuidGenerator
    private UUID id;
    private UUID userId;
    private OrderStatus status;
    private OffsetDateTime createdAt;
    private Integer totalPrice;
    private OffsetDateTime completedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> products;
}

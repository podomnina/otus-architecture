package ru.otus.order.service.model.entity;

import jakarta.persistence.Column;
import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
@RedisHash(value = "OrderCart")
public class Cart implements Serializable {
    @Id
    private UUID userId;
    @Column(precision = 8, scale = 2)
    private BigDecimal totalPrice;
    private List<Item> items;

    @TimeToLive(unit = TimeUnit.MINUTES) // todo to config
    private Long expirationTime = 60L; // 60 минут

    @Data
    public static class Item {
        private UUID dishId;
        private String name;
        @Column(precision = 8, scale = 2)
        private BigDecimal price;
        private Integer quantity;
        private Boolean isAvailable;
    }
}

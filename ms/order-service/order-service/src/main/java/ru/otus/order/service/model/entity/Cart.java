package ru.otus.order.service.model.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
@RedisHash(value = "OrderCart")
public class Cart implements Serializable {
    @Id
    private UUID userId;
    private Integer totalPrice;
    private List<Item> items;

    @TimeToLive(unit = TimeUnit.MINUTES) // todo to config
    private Long expirationTime = 60L; // 60 минут

    @Data
    public static class Item {
        private UUID dishId;
        private String name;
        private Integer price;
        private Integer quantity;
        private Boolean isAvailable;
    }
}

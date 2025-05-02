package ru.otus.order.service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Data
@RedisHash(value = "IdempotencyKeyOrder")
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyKey {
    @Id
    private String key;
    private Integer orderId;

    @TimeToLive(unit = TimeUnit.MINUTES)
    private Long expirationTime = 60L; // 60 минут
}

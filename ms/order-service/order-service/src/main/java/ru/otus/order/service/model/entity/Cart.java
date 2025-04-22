package ru.otus.order.service.model.entity;

import jakarta.persistence.Column;
import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Data
@RedisHash(value = "OrderCart")
public class Cart implements Serializable {
    @Id
    private UUID userId;
    @Column(precision = 8, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    private Map<Integer, Item> items = new HashMap<>();

    @TimeToLive(unit = TimeUnit.MINUTES) // todo to config
    private Long expirationTime = 60L; // 60 минут

    @Data
    public static class Item {
        private Integer dishId;
        private String name;
        @Column(precision = 8, scale = 2)
        private BigDecimal price = BigDecimal.ZERO;
        private Integer quantity = 1;
        private Boolean isAvailable = true;
    }

    public static Cart newCart(UUID userId) {
        var cart = new Cart();
        cart.setUserId(userId);
        return cart;
    }

    public void addItem(Item item) {
        var dishId = item.getDishId();
        var existingItem = items.get(dishId);
        if (existingItem == null) {
            items.put(dishId, item);
        } else {
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            //items.put(dishId, existingItem);
        }
        calculateTotal(); // Пересчет общей суммы

        this.expirationTime = 60L; // todo to config
    }

    public void removeItem(Integer dishId, Integer quantity) {
        var existingItem = items.get(dishId);
        if (existingItem == null) {
            return;
        } else {
            var newQuantity = existingItem.getQuantity() - quantity;
            if (newQuantity > 0) {
                existingItem.setQuantity(newQuantity); //todo test
                items.put(dishId, existingItem);
            } else {
                items.remove(dishId);
            }

        }

        calculateTotal();
        this.expirationTime = 60L; // todo to config
    }

    public void addItems(Map<Integer, Item> items) {
        this.setItems(items);
        calculateTotal();
    }

    private void calculateTotal() {
        this.totalPrice = items.values().stream()
                .map(i -> {
                    var q = i.getQuantity() != null ? i.getQuantity() : 1;
                    var p = i.getPrice() != null ? i.getPrice() : BigDecimal.ZERO;
                    return p.multiply(new BigDecimal(q));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

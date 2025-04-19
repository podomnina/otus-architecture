package ru.otus.menu.service.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "dish_product", schema = "menu")
public class DishProduct {

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @EmbeddedId
    private DishProductId id;
    private Integer quantity;
}

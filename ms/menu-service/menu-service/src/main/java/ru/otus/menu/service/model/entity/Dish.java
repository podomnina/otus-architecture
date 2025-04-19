package ru.otus.menu.service.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import ru.otus.menu.service.model.DishCategory;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "dish", schema = "menu")
public class Dish {
    @Id
    @UuidGenerator
    private UUID id;
    private String name;
    private DishCategory category;
    private Integer price;
    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DishProduct> products;
}

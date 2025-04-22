package ru.otus.menu.service.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ru.otus.menu.service.model.DishCategory;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "products")
@Table(name = "dish", schema = "menu")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Enumerated(EnumType.STRING)
    private DishCategory category;
    @Column(precision = 8, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;
    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DishProduct> products = new HashSet<>();

    public void addProduct(DishProduct product) {
        products.add(product);
        product.setDish(this);
    }

    public void removeProduct(DishProduct product) {
        products.remove(product);
        product.setDish(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id != null && id.equals(dish.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

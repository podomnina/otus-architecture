package ru.otus.inventory.service.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "inventory", schema = "inventory")
public class Inventory {
    private Integer quantity;
    private Integer threshold;
    @Id
    private UUID productId;
    //@OneToOne
    //@JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    //private Product product;
}

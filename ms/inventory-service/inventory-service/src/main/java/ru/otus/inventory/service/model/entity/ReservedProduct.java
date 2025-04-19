package ru.otus.inventory.service.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reserved_product", schema = "inventory")
public class ReservedProduct {

    @EmbeddedId
    private ReservedProductId id;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}

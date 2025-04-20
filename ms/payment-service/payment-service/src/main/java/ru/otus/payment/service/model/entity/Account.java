package ru.otus.payment.service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "account", schema = "payment")
public class Account {
    @Id
    private UUID userId;
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
    private OffsetDateTime createdAt;
}
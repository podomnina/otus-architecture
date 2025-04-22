package ru.otus.payment.service.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FillUpAccountRequestDto {
    @Positive
    @NotNull
    private BigDecimal amount;
}

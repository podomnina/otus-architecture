package ru.otus.delivery.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {
    private Integer orderId;
    private String status;
    private OffsetDateTime timeslot;
    private String deliveryMan;
    private OffsetDateTime createdAt;
}
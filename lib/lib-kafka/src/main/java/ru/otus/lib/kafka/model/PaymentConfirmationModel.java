package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmationModel {
    private UUID orderId;
    private ProcessStatus status;
    private String reason;

    public static PaymentConfirmationModel success(UUID orderId) {
        return PaymentConfirmationModel.builder()
                .orderId(orderId)
                .status(ProcessStatus.SUCCESS)
                .build();
    }

    public static PaymentConfirmationModel error(UUID orderId, String reason) {
        return PaymentConfirmationModel.builder()
                .orderId(orderId)
                .reason(reason)
                .status(ProcessStatus.ERROR)
                .build();
    }
}

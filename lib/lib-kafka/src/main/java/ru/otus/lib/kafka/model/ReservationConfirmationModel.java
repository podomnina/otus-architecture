package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationConfirmationModel {
    private UUID orderId;
    private ProcessStatus status;
    private String reason;

    public static ReservationConfirmationModel success(UUID orderId) {
        return ReservationConfirmationModel.builder()
                .orderId(orderId)
                .status(ProcessStatus.SUCCESS)
                .build();
    }

    public static ReservationConfirmationModel error(UUID orderId, String reason) {
        return ReservationConfirmationModel.builder()
                .orderId(orderId)
                .reason(reason)
                .status(ProcessStatus.ERROR)
                .build();
    }
}

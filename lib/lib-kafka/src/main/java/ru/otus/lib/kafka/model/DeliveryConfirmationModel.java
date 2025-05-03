package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryConfirmationModel {
    private Integer orderId;
    private ProcessStatus status;
    private String reason;

    public static DeliveryConfirmationModel success(Integer orderId) {
        return DeliveryConfirmationModel.builder()
                .orderId(orderId)
                .status(ProcessStatus.SUCCESS)
                .build();
    }

    public static DeliveryConfirmationModel error(Integer orderId, String reason) {
        return DeliveryConfirmationModel.builder()
                .orderId(orderId)
                .reason(reason)
                .status(ProcessStatus.ERROR)
                .build();
    }
}

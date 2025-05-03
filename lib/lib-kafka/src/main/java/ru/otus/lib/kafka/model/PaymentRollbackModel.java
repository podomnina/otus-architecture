package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRollbackModel {
    private Integer orderId;

    public static PaymentRollbackModel init(Integer orderId) {
        return new PaymentRollbackModel(orderId);
    }
}

package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryProcessModel {
    private Integer orderId;

    public static DeliveryProcessModel initDelivery(Integer orderId) {
        return new DeliveryProcessModel(orderId);
    }
}

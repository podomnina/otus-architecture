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
public class ReleaseIngredientsModel {
    private Integer orderId;
    private ReleaseType type;

    public static ReleaseIngredientsModel initCooking(Integer orderId) {
        return ReleaseIngredientsModel.builder()
                .orderId(orderId)
                .type(ReleaseType.COOKING)
                .build();
    }

    public static ReleaseIngredientsModel initRelease(Integer orderId) {
        return ReleaseIngredientsModel.builder()
                .orderId(orderId)
                .type(ReleaseType.RELEASE)
                .build();
    }
}

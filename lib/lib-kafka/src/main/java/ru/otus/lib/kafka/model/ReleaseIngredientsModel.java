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
    private UUID orderId;
    private ReleaseType type;

    public static ReleaseIngredientsModel initCooking(UUID orderId) {
        return ReleaseIngredientsModel.builder()
                .orderId(orderId)
                .type(ReleaseType.COOKING)
                .build();
    }

    public static ReleaseIngredientsModel initRelease(UUID orderId) {
        return ReleaseIngredientsModel.builder()
                .orderId(orderId)
                .type(ReleaseType.RELEASE)
                .build();
    }
}

package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationProcessModel {
    private UUID orderId;
    private Map<UUID, Integer> dishQuantityMap;

    public static ReservationProcessModel initReserve(UUID orderId, Map<UUID, Integer> dishQuantityMap) {
        return ReservationProcessModel.builder()
                .orderId(orderId)
                .dishQuantityMap(dishQuantityMap)
                .build();
    }

}

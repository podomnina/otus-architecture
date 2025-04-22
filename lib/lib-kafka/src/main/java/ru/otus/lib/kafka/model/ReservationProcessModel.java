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
    private Integer orderId;
    private Map<Integer, Integer> dishQuantityMap;

    public static ReservationProcessModel initReserve(Integer orderId, Map<Integer, Integer> dishQuantityMap) {
        return ReservationProcessModel.builder()
                .orderId(orderId)
                .dishQuantityMap(dishQuantityMap)
                .build();
    }

}

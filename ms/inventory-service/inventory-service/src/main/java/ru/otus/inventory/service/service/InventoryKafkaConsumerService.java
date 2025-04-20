package ru.otus.inventory.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.lib.kafka.model.ReleaseIngredientsModel;
import ru.otus.lib.kafka.model.ReleaseType;
import ru.otus.lib.kafka.model.ReservationProcessModel;
import ru.otus.lib.kafka.service.BusinessTopics;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryKafkaConsumerService {

    private final InventoryService service;

    @KafkaListener(topics = BusinessTopics.ORDER_RESERVATION_PROCESS, groupId = "${spring.kafka.consumer.group-id}")
    public void listenOrderReservationProcess(ReservationProcessModel model) {
        log.debug("Received new reservation process: {}", model);
        service.processReservation(model);

    }

    @KafkaListener(topics = BusinessTopics.ORDER_RELEASE_INGREDIENTS, groupId = "${spring.kafka.consumer.group-id}")
    public void listenOrderReleaseIngredients(ReleaseIngredientsModel model) {
        log.debug("Received new release ingredients: {}", model);
        service.processRelease(model);
    }
}

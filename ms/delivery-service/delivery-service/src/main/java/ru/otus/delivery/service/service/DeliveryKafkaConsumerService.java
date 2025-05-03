package ru.otus.delivery.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.lib.kafka.model.DeliveryProcessModel;
import ru.otus.lib.kafka.service.BusinessTopics;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryKafkaConsumerService {

    private final DeliveryService service;

    @KafkaListener(topics = BusinessTopics.ORDER_DELIVERY_PROCESS, groupId = "${spring.kafka.consumer.group-id}")
    public void listenOrderDeliveryProcess(DeliveryProcessModel model) {
        log.debug("Received new delivery process: {}", model);
        try {
            service.handleDeliveryProcess(model);
        } catch (Exception e) {
            log.error("Error while processing order delivery process", e);
            throw e;
        }
    }
}

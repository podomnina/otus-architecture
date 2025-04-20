package ru.otus.payment.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.lib.kafka.model.PaymentProcessModel;
import ru.otus.lib.kafka.service.BusinessTopics;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentKafkaConsumerService {

    private final AccountService service;

    @KafkaListener(topics = BusinessTopics.ORDER_PAYMENT_PROCESS, groupId = "${spring.kafka.consumer.group-id}")
    public void listenOrderPaymentProcess(PaymentProcessModel model) {
        log.debug("Received new payment process: {}", model);
        service.handlePaymentProcess(model);
    }
}

package ru.otus.delivery.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.common.error.BusinessAppException;
import ru.otus.delivery.service.model.dto.DeliveryResponseDto;
import ru.otus.delivery.service.model.entity.Delivery;
import ru.otus.delivery.service.model.entity.DeliveryMan;
import ru.otus.delivery.service.repository.DeliveryRepository;
import ru.otus.lib.kafka.model.DeliveryConfirmationModel;
import ru.otus.lib.kafka.model.DeliveryProcessModel;
import ru.otus.lib.kafka.service.BusinessTopics;
import ru.otus.lib.kafka.service.KafkaProducerService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final List<Integer> TIMESLOT_LIST = List.of(10);

    private final DeliveryRepository repository;
    private final KafkaProducerService kafkaProducerService;

    public DeliveryResponseDto getDelivery(Integer orderId) {
        var deliveryOpt = repository.findByOrderId(orderId);
        if (deliveryOpt.isEmpty()) {
            throw new BusinessAppException("delivery.not.found", "Доставка не найдена");
        }

        var delivery = deliveryOpt.get();
        return DeliveryResponseDto.builder()
                .orderId(delivery.getOrderId())
                .status(delivery.getStatus())
                .timeslot(OffsetDateTime.now()
                        .withHour(delivery.getTimeslot())
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                )
                .deliveryMan(delivery.getDeliveryMan().getName())
                .createdAt(delivery.getCreatedAt())
                .build();
    }

    public void handleDeliveryProcess(DeliveryProcessModel model) {
        var orderId = model.getOrderId();
        var deliveries = repository.findAll();
        var bookedTimeslots = deliveries.stream()
                .map(Delivery::getTimeslot)
                .collect(Collectors.toSet());

        var freeTimeslot = TIMESLOT_LIST.stream()
                .filter(t -> !bookedTimeslots.contains(t))
                .findFirst();
        if (freeTimeslot.isEmpty()) {
            var deliveryConfirmationModel = DeliveryConfirmationModel.error(orderId, "No free timeslots");
            kafkaProducerService.send(BusinessTopics.ORDER_DELIVERY_CONFIRMATION, deliveryConfirmationModel);
            return;
        }

        var deliveryMan = DeliveryMan.builder()
                .id(1)
                .build();

        var delivery = Delivery.builder()
                .orderId(orderId)
                .status("CREATED")
                .timeslot(freeTimeslot.get())
                .createdAt(OffsetDateTime.now())
                .deliveryMan(deliveryMan)
                .build();
        repository.save(delivery);

        var deliveryConfirmationModel = DeliveryConfirmationModel.success(orderId);
        kafkaProducerService.send(BusinessTopics.ORDER_DELIVERY_CONFIRMATION, deliveryConfirmationModel);
    }
}
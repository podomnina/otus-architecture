package ru.otus.order.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.common.UserCtx;
import ru.otus.common.error.BusinessAppException;
import ru.otus.lib.kafka.model.*;
import ru.otus.lib.kafka.service.BusinessTopics;
import ru.otus.lib.kafka.service.KafkaProducerService;
import ru.otus.order.service.model.OrderEvent;
import ru.otus.order.service.model.OrderStatus;
import ru.otus.order.service.model.entity.Order;
import ru.otus.order.service.repository.OrderRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessorService {

    private final OrderRepository orderRepository;
    private final KafkaProducerService kafkaProducerService;
    private final OrderStateMachineService orderStateMachineService;

    public Order createOrder(Order order, UserCtx userCtx) {
        var createdOrder = orderRepository.save(order);

        var orderId = order.getId();
/*
        var dishMap = createdOrder.getItems().stream()
                .collect(Collectors.toMap(
                        i -> i.getId().getDishId(),
                        i -> i.getQuantity(),
                        (i1, i2) -> i1 + i2)
                );

        var reservationModel = ReservationProcessModel.initReserve(orderId, dishMap);
        kafkaProducerService.send(BusinessTopics.ORDER_RESERVATION_PROCESS, reservationModel);

        var notificationModel = SendNotificationModel.orderCreated(orderId, order.getEmail());
        kafkaProducerService.send(BusinessTopics.NOTIFICATION_SEND, notificationModel);*/

        var userId = order.getUserId();
        var amount = order.getTotalPrice();
        var paymentModel = PaymentProcessModel.init(orderId, userId, amount);
        kafkaProducerService.send(BusinessTopics.ORDER_PAYMENT_PROCESS, paymentModel);

        return createdOrder;
    }

    @Transactional
    public Order setStatus(Integer orderId, OrderStatus status) {
        var order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new BusinessAppException("order.not.found", "Order not found"));

        order.setStatus(status);
        if (OrderStatus.READY == status) {
            orderStateMachineService.sendToReadyAction(order);
        } else if (OrderStatus.DELIVERED == status) {
            orderStateMachineService.sendToDeliveredAction(order);
        } else {
            throw new BusinessAppException("order.status.transition.invalid", "Неверный статус");
        }
        return order;
    }

    @Transactional
    public void handleReservationConfirmation(ReservationConfirmationModel model) {
        var orderId = model.getOrderId();
        var orderOpt = orderRepository.findByIdForUpdate(orderId);
        if (orderOpt.isEmpty()) {
            log.error("Order not found for reservation confirmation for: {}. Skip it", model);
            return;
        }

        var order = orderOpt.get();

        var status = model.getStatus();
        if (ProcessStatus.SUCCESS == status) {
            log.debug("Successfully reserved some products for the order with id {}", orderId);
            orderStateMachineService.sendToPaymentAction(order);
        } else {
            log.warn("Products are not reserved for order {}", orderId);
            orderStateMachineService.cancelOrderAction(order, OrderEvent.INGREDIENTS_NOT_AVAILABLE);
        }
    }

    @Transactional
    public void handlePaymentConfirmation(PaymentConfirmationModel model) {
        var orderId = model.getOrderId();
        var orderOpt = orderRepository.findByIdForUpdate(orderId);
        if (orderOpt.isEmpty()) {
            log.error("Order not found for payment confirmation for: {}. Skip it", model);
            return;
        }

        var order = orderOpt.get();

        var status = model.getStatus();
        if (ProcessStatus.SUCCESS == status) {
            log.debug("Successfully paid for the order with id {}", orderId);
            orderStateMachineService.startCookingAction(order);
        } else {
            log.warn("Failed to pay for the order {}", orderId);
            orderStateMachineService.cancelOrderAction(order, OrderEvent.PAYMENT_FAILED);
        }
    }

}

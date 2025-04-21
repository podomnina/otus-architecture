package ru.otus.order.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import ru.otus.common.UserCtx;
import ru.otus.common.error.BusinessAppException;
import ru.otus.lib.kafka.model.*;
import ru.otus.lib.kafka.service.BusinessTopics;
import ru.otus.lib.kafka.service.KafkaProducerService;
import ru.otus.order.service.mapper.CartMapper;
import ru.otus.order.service.model.OrderEvent;
import ru.otus.order.service.model.OrderStatus;
import ru.otus.order.service.model.entity.Order;
import ru.otus.order.service.repository.CartRepository;
import ru.otus.order.service.repository.OrderRepository;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessorService {

    private final OrderRepository orderRepository;
    private final StateMachineFactory<OrderStatus, OrderEvent> stateMachineFactory;
    private final KafkaProducerService kafkaProducerService;

    public Order createOrder(Order order, UserCtx userCtx) {
        var createdOrder = orderRepository.save(order);

        var orderId = order.getId();

        var dishMap = createdOrder.getItems().stream()
                .collect(Collectors.toMap(
                        i -> i.getId().getDishId(),
                        i -> i.getQuantity(),
                        (i1, i2) -> i1 + i2)
                );

        var sm = buildStateMachine(order, userCtx);

        var reservationModel = ReservationProcessModel.initReserve(orderId, dishMap);
        kafkaProducerService.send(BusinessTopics.ORDER_RESERVATION_PROCESS, reservationModel);

        var notificationModel = SendNotificationModel.orderCreated(userCtx);
        kafkaProducerService.send(BusinessTopics.NOTIFICATION_SEND, notificationModel);
        return createdOrder;
    }

    @Transactional
    public Order setStatus(UUID orderId, OrderEvent event) {
        var order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new BusinessAppException("order.not.found", "Order not found"));

        var sm = buildStateMachine(order);
        sm.sendEvent(event);

        order.setStatus(sm.getState().getId());
        return orderRepository.save(order);
    }

    @Transactional
    public void handleReservationConfirmation(ReservationConfirmationModel model) {
        var orderId = model.getOrderId();
        var order = orderRepository.findById(orderId) //todo locked?
                .orElseThrow(() -> new BusinessAppException("order.not.found", "Order not found"));;
        var sm = buildStateMachine(order);

        var status = model.getStatus();
        if (ProcessStatus.SUCCESS == status) {
            log.debug("Successfully reserved some products for the order with id {}", orderId);
            sm.sendEvent(OrderEvent.INGREDIENTS_RESERVED);
        } else {
            log.warn("Products are not reserved for order {}", orderId);
            sm.sendEvent(OrderEvent.INGREDIENTS_NOT_AVAILABLE);
        }
    }

    @Transactional
    public void handlePaymentConfirmation(PaymentConfirmationModel model) {
        var orderId = model.getOrderId();
        var order = orderRepository.findById(orderId) //todo locked?
                .orElseThrow(() -> new BusinessAppException("order.not.found", "Order not found"));;
        var sm = buildStateMachine(order);

        var status = model.getStatus();
        if (ProcessStatus.SUCCESS == status) {
            log.debug("Successfully paid for the order with id {}", orderId);
            sm.sendEvent(OrderEvent.PAYMENT_RECEIVED);
        } else {
            log.warn("Failed to pay for the order {}", orderId);
            sm.sendEvent(OrderEvent.PAYMENT_FAILED);
        }
    }



    private StateMachine<OrderStatus, OrderEvent> buildStateMachine(Order order) {
        return buildStateMachine(order, null);
    }

    private StateMachine<OrderStatus, OrderEvent> buildStateMachine(Order order, UserCtx userCtx) {
        var sm = stateMachineFactory.getStateMachine(order.getId().toString());

        sm.start();
        sm.getExtendedState().getVariables().put("order", order);
        if (userCtx != null) {
            sm.getExtendedState().getVariables().put("userCtx", userCtx);
        }

        if (!sm.getState().getId().equals(order.getStatus())) {
            sm.getStateMachineAccessor()
                    .doWithAllRegions(access -> access.resetStateMachine(
                            new DefaultStateMachineContext<>(
                                    order.getStatus(), null, null, null, null,
                                    stateMachineFactory.getStateMachine(order.getId().toString()).getId())));
        }

        return sm;
    }
}

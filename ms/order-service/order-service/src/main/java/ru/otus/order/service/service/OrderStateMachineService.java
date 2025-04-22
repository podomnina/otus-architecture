package ru.otus.order.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.common.error.BusinessAppException;
import ru.otus.lib.kafka.model.PaymentProcessModel;
import ru.otus.lib.kafka.model.ReleaseIngredientsModel;
import ru.otus.lib.kafka.model.SendNotificationModel;
import ru.otus.lib.kafka.service.BusinessTopics;
import ru.otus.lib.kafka.service.KafkaProducerService;
import ru.otus.order.service.mapper.CartMapper;
import ru.otus.order.service.model.OrderEvent;
import ru.otus.order.service.model.OrderStatus;
import ru.otus.order.service.model.entity.Order;
import ru.otus.order.service.repository.CartRepository;
import ru.otus.order.service.repository.OrderRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStateMachineService {

    private final OrderRepository orderRepository;
    private final KafkaProducerService kafkaProducerService;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public void sendToPaymentAction(Order order) {
        var orderId = order.getId();
        log.debug("Preparing order with id: {} for the payment action", orderId);
        orderRepository.updateStatus(orderId, OrderStatus.PENDING_PAYMENT);

        var userId = order.getUserId();
        var amount = order.getTotalPrice();
        var paymentModel = PaymentProcessModel.init(orderId, userId, amount);
        kafkaProducerService.send(BusinessTopics.ORDER_PAYMENT_PROCESS, paymentModel);
    }

    @Transactional
    public void startCookingAction(Order order) {
        var orderId = order.getId();
        log.debug("Preparing order with id: {} for the cooking action", order.getId());
        orderRepository.updateStatus(orderId, OrderStatus.COOKING); //todo?

        var releaseModel = ReleaseIngredientsModel.initCooking(orderId);
        kafkaProducerService.send(BusinessTopics.ORDER_RELEASE_INGREDIENTS, releaseModel);
    }

    @Transactional
    public void sendToReadyAction(Order order) {
        log.debug("Sending ready action email info for order with id: {}", order.getId());
        var notificationModel = SendNotificationModel.orderIsReady(order.getId(), order.getEmail());
        kafkaProducerService.send(BusinessTopics.NOTIFICATION_SEND, notificationModel);
    }

    @Transactional
    public void sendToDeliveredAction(Order order) {
        log.debug("Sending delivered action email info for order with id: {}", order.getId());
        var notificationModel = SendNotificationModel.orderIsDelivered(order.getId(), order.getEmail());
        kafkaProducerService.send(BusinessTopics.NOTIFICATION_SEND, notificationModel);
    }

    @Transactional
    public void cancelOrderAction(Order order, OrderEvent event) {
        var orderId = order.getId();
        log.debug("Preparing order with id: {} for the cancelling because of the event: {}", order.getId(), event);
        orderRepository.updateStatus(orderId, OrderStatus.CANCELLED); //todo?

        var cart = cartMapper.map(order);
        //todo recalculate + check dishes again
        cartRepository.save(cart);

        var releaseModel = ReleaseIngredientsModel.initRelease(orderId);
        kafkaProducerService.send(BusinessTopics.ORDER_RELEASE_INGREDIENTS, releaseModel);

        var notificationModel = prepareErrorNotification(orderId, order.getEmail(), event);
        kafkaProducerService.send(BusinessTopics.NOTIFICATION_SEND, notificationModel);
    }

    private SendNotificationModel prepareErrorNotification(Integer orderId, String email, OrderEvent event) {
        if (OrderEvent.INGREDIENTS_NOT_AVAILABLE == event) {
            return SendNotificationModel.orderCancelledByIngredients(orderId, email);
        } else if (OrderEvent.PAYMENT_FAILED == event) {
            return SendNotificationModel.orderCancelledByPayment(orderId, email);
        }

        //todo
        throw new BusinessAppException("order.error.unknown.event", "Unknown event");
    }
}

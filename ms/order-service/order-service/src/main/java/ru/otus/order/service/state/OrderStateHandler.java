package ru.otus.order.service.state;

import ru.otus.order.service.model.OrderStatus;
import ru.otus.order.service.model.entity.Order;

public abstract class OrderStateHandler {
    public void preHandle(Order order, OrderStatus newStatus) {}
    public void postHandle(Order order, OrderStatus oldStatus) {}
}
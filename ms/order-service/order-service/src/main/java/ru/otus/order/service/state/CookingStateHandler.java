package ru.otus.order.service.state;

import org.springframework.stereotype.Service;
import ru.otus.order.service.model.OrderStatus;
import ru.otus.order.service.model.entity.Order;

@Service
public class CookingStateHandler extends OrderStateHandler {

    @Override
    public void preHandle(Order order, OrderStatus newStatus) {
        if (newStatus == OrderStatus.COOKING) {
            // например, проверить наличие ингредиентов
        }
    }
}
package ru.otus.order.service.model;

public enum OrderStatus {
    CREATED, PENDING_PAYMENT, PAID, COOKING, READY, DELIVERED, CANCELLED;

    public static boolean isValidForManualTransition(OrderStatus value) {
        return value == READY || value == DELIVERED;
    }

}

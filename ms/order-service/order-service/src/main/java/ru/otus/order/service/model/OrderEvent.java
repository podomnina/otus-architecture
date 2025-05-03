package ru.otus.order.service.model;

public enum OrderEvent {
    INGREDIENTS_RESERVED,
    INGREDIENTS_NOT_AVAILABLE,
    PAYMENT_RECEIVED,
    PAYMENT_FAILED,
    DELIVERY_FAILED,
    MARK_AS_READY,
    MARK_AS_DELIVERED;

    public static boolean isValidForManualTransition(OrderEvent value) {
        return value == MARK_AS_READY || value == MARK_AS_DELIVERED;
    }
}

package ru.otus.lib.kafka.service;

public final class BusinessTopics {
    public static final String ORDER_RESERVATION_PROCESS = "order.reservation.process";
    public static final String ORDER_RESERVATION_CONFIRMATION = "order.reservation.confirmation";
    public static final String ORDER_PAYMENT_PROCESS = "order.payment.process";
    public static final String ORDER_PAYMENT_CONFIRMATION = "order.payment.confirmation";
    public static final String NOTIFICATION_SEND = "notification.send";
    public static final String ORDER_DELIVERY_PROCESS = "order.delivery.process";
    public static final String ORDER_DELIVERY_CONFIRMATION = "order.delivery.confirmation";

    public static final String ORDER_RELEASE_INGREDIENTS = "order.release.ingredients";
    public static final String ORDER_PAYMENT_ROLLBACK = "order.payment.rollback";

    public static final String PAYMENT_NEW_ACCOUNT = "payment.new.account";
}

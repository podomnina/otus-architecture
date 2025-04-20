package ru.otus.lib.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.common.UserCtx;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationModel {
    //private UUID orderId;
    private String email;
    private NotificationType type;
    private String subject;
    private String message;

    public static SendNotificationModel orderCreated(UserCtx userCtx) {
        var type = NotificationType.ORDER_CREATED;
        return prepare(userCtx, type);
    }

    public static SendNotificationModel orderCancelledByIngredients(UserCtx userCtx) {
        var type = NotificationType.INGREDIENTS_NOT_AVAILABLE;
        return prepare(userCtx, type);
    }

    public static SendNotificationModel orderCancelledByPayment(UserCtx userCtx) {
        var type = NotificationType.PAYMENT_FAILED;
        return prepare(userCtx, type);
    }

    public static SendNotificationModel orderIsReady(UserCtx userCtx) {
        var type = NotificationType.ORDER_IS_READY;
        return prepare(userCtx, type);
    }

    public static SendNotificationModel orderIsDelivered(UserCtx userCtx) {
        var type = NotificationType.ORDER_IS_DELIVERED;
        return prepare(userCtx, type);
    }

    private static SendNotificationModel prepare(UserCtx userCtx, NotificationType type) {
        var email = userCtx.getLogin();
        var name = userCtx.getFirstName();
        var subject = type.getSubject();
        var message = type.getMessage().formatted(name);
        return SendNotificationModel.builder()
                .email(email)
                .type(type)
                .subject(subject)
                .message(message)
                .build();
    }
}

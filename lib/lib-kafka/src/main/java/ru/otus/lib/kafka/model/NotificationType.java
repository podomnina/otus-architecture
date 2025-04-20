package ru.otus.lib.kafka.model;

public enum NotificationType {
    ORDER_CREATED("Ваш заказ создан", "Здравствуйте, %s! Спасибо за заказ! Мы передали Ваш заказ на обработку."),
    INGREDIENTS_NOT_AVAILABLE("В Вашем заказе есть блюда, которые недоступны", "%s, к сожалению, некоторые блюда мы не можем приготовить. Выберите другое блюдо и попробуйте оформить заказ еще раз! Спасибо за понимание!"),
    PAYMENT_FAILED("Оплата не прошла", "%s, к сожалению, оплата не прошла, возможно недостаточно средств на счете. Пополните счет и попробуйте сделать заказ еще раз. Ваши блюда возвращены в корзину."),
    ORDER_IS_READY("Заказ готов", "%s, Ваш заказ готов! Скорее получите его!"),
    ORDER_IS_DELIVERED("Заказ доставлен", "%s, Ваш заказ получен. Спасибо, что выбрали нас! Приятного аппетита!");

    private final String subject;
    private final String message;

    NotificationType(String subject, String message) {
        this.subject = subject;
        this.message = message;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getMessage() {
        return this.message;
    }
}

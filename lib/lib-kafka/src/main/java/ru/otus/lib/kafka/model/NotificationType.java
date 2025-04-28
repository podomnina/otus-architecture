package ru.otus.lib.kafka.model;

public enum NotificationType {
    ORDER_CREATED("Ваш заказ создан", "Здравствуйте! Спасибо за заказ! Мы передали Ваш заказ № %d на обработку."),
    INGREDIENTS_NOT_AVAILABLE("В Вашем заказе есть блюда, которые недоступны", "К сожалению, некоторые блюда мы не можем приготовить. Выберите другое блюдо и попробуйте оформить заказ еще раз! Спасибо за понимание!"),
    PAYMENT_FAILED("Оплата не прошла", "К сожалению, оплата не прошла, возможно недостаточно средств на счете. Пополните счет и попробуйте сделать заказ еще раз. Ваши блюда возвращены в корзину."),
    ORDER_IS_COOKING("Заказ принят", "Ваш заказ № %d принят и уже готовится!"),
    ORDER_IS_READY("Заказ готов", "Ваш заказ № %d готов! Скорее получите его!"),
    ORDER_IS_DELIVERED("Заказ доставлен", "Ваш заказ № %d получен. Спасибо, что выбрали нас! Приятного аппетита!"),
    ORDER_IS_PAID("Ваш заказ оплачен", "Ваш заказ № %d оплачен! Спасибо!");

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

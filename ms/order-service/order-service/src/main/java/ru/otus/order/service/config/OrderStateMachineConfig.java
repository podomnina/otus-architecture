package ru.otus.order.service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
//@EnableStateMachineFactory(name = "stateMachineFactory")
@RequiredArgsConstructor
public class OrderStateMachineConfig {}
        /*extends StateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

    private final OrderStateMachineService stateMachineService;

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
        states.withStates()
                .initial(OrderStatus.CREATED)
                .states(EnumSet.allOf(OrderStatus.class));
                //.and()
                //.withStates()
                //.parent(OrderStatus.PAID)
                //.initial(OrderStatus.COOKING);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions
                // Проверка ингредиентов
                .withExternal()
                .source(OrderStatus.CREATED)
                .target(OrderStatus.PENDING_PAYMENT)
                .event(OrderEvent.INGREDIENTS_RESERVED)
                .action(sendToPaymentAction())
                .and()
                .withExternal()
                .source(OrderStatus.CREATED)
                .target(OrderStatus.CANCELLED)
                .event(OrderEvent.INGREDIENTS_NOT_AVAILABLE)
                .action(cancelOrderAction())

                // Оплата
                .and()
                .withExternal()
                .source(OrderStatus.PENDING_PAYMENT)
                .target(OrderStatus.COOKING)
                .event(OrderEvent.PAYMENT_RECEIVED)
                .action(startCookingAction())
                .and()
                .withExternal()
                .source(OrderStatus.PENDING_PAYMENT)
                .target(OrderStatus.CANCELLED)
                .event(OrderEvent.PAYMENT_FAILED)
                .action(cancelOrderAction())

                // Готовка
                .and()
                .withExternal()
                .source(OrderStatus.COOKING)
                .target(OrderStatus.READY)
                .event(OrderEvent.MARK_AS_READY)
                .action(sendToReadyAction())

                // Выдача
                .and()
                .withExternal()
                .source(OrderStatus.READY)
                .target(OrderStatus.DELIVERED)
                .event(OrderEvent.MARK_AS_DELIVERED)
                .action(sendToDeliveredAction());
    }

    @Bean
    public Action<OrderStatus, OrderEvent> sendToPaymentAction() {
        return context -> {
            Order order = context.getMessage().getHeaders().get("order", Order.class);
            stateMachineService.sendToPaymentAction(order);
        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> startCookingAction() {
        return context -> {
            Order order = context.getMessage().getHeaders().get("order", Order.class);
            stateMachineService.startCookingAction(order);
        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> sendToReadyAction() {
        return context -> {
            Order order = context.getMessage().getHeaders().get("order", Order.class);
            UserCtx userCtx = context.getMessage().getHeaders().get("userCtx", UserCtx.class);
            stateMachineService.sendToReadyAction(order, userCtx);
        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> sendToDeliveredAction() {
        return context -> {
            Order order = context.getMessage().getHeaders().get("order", Order.class);
            UserCtx userCtx = context.getMessage().getHeaders().get("userCtx", UserCtx.class);
            stateMachineService.sendToDeliveredAction(order, userCtx);
        };
    }

    @Bean
    public Action<OrderStatus, OrderEvent> cancelOrderAction() {
        return context -> {
            Order order = getOrderFromContext(context);
            UserCtx userCtx = context.getMessage().getHeaders().get("userCtx", UserCtx.class);
            var event = context.getEvent();
            stateMachineService.cancelOrderAction(order, userCtx, event);
        };
    }

    @Bean
    public StateMachineListener<OrderStatus, OrderEvent> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<OrderStatus, OrderEvent> from,
                                     State<OrderStatus, OrderEvent> to) {
                log.debug("Changing status from: {} to: {}", from.getId(), to.getId());
            }
        };
    }

    private Order getOrderFromContext(StateContext<OrderStatus, OrderEvent> context) {
        return context.getExtendedState().get("order", Order.class);
    }
}*/
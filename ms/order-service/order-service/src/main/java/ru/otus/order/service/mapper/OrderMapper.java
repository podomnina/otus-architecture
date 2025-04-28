package ru.otus.order.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.common.UserCtx;
import ru.otus.order.service.model.OrderStatus;
import ru.otus.order.service.model.dto.AddItemRequestDto;
import ru.otus.order.service.model.dto.OrderResponseDto;
import ru.otus.order.service.model.entity.Cart;
import ru.otus.order.service.model.entity.Order;
import ru.otus.order.service.model.entity.OrderItem;
import ru.otus.order.service.model.entity.OrderItemId;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Map<Integer, BigDecimal> productPriceMap = Map.of(
            1, BigDecimal.valueOf(100),
            2, BigDecimal.valueOf(500)
    );

    default Order map(Cart cart) {
        Order order = Order.builder()
                .userId(cart.getUserId())
                .status(OrderStatus.CREATED)
                .createdAt(OffsetDateTime.now())
                .items(new HashSet<>()) // Инициализируем коллекцию
                .build();

        cart.getItems().values().forEach(i -> {
            OrderItem item = OrderItem.builder()
                    .id(new OrderItemId(null, i.getDishId()))
                    .name(i.getName())
                    .price(i.getPrice())
                    .quantity(i.getQuantity())
                    .order(order) // Устанавливаем связь с Order
                    .build();

            order.getItems().add(item);
        });

        var totalPrice = order.getItems().stream()
                .map(i -> {
                    var q = i.getQuantity() != null ? i.getQuantity() : 1;
                    var p = i.getPrice() != null ? i.getPrice() : BigDecimal.ZERO;
                    return p.multiply(new BigDecimal(q));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);
        return order;
    }

    OrderResponseDto map(Order order);

    @Mapping(target = "dishId", source = "id.dishId")
    OrderResponseDto.Item map(OrderItem orderItem);

    default Order mapMock(AddItemRequestDto dto, UserCtx userCtx) {
        Order order = Order.builder()
                .userId(userCtx.getId())
                .status(OrderStatus.CREATED)
                .email(userCtx.getLogin())
                .createdAt(OffsetDateTime.now())
                .items(new HashSet<>()) // Инициализируем коллекцию
                .build();

        var value = productPriceMap.getOrDefault(dto.getDishId(), BigDecimal.valueOf(100));

        OrderItem item = OrderItem.builder()
                .id(new OrderItemId(null, dto.getDishId()))
                .name("Product " + dto.getDishId())
                .price(value)
                .quantity(1)
                .order(order) // Устанавливаем связь с Order
                .build();

        order.getItems().add(item);

        var totalPrice = order.getItems().stream()
                .map(i -> {
                    var q = i.getQuantity() != null ? i.getQuantity() : 1;
                    var p = i.getPrice() != null ? i.getPrice() : BigDecimal.ZERO;
                    return p.multiply(new BigDecimal(q));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);
        return order;
    }
}

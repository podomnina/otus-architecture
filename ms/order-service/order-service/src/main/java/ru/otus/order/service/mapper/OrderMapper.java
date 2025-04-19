package ru.otus.order.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.order.service.model.dto.OrderResponseDto;
import ru.otus.order.service.model.entity.Cart;
import ru.otus.order.service.model.entity.Order;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "status", defaultExpression = "java(ru.otus.order.service.model.OrderStatus.CREATED)")
    Order map(Cart cart, UUID id);

    OrderResponseDto map(Order order);
}

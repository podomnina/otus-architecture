package ru.otus.order.service.mapper;

import org.mapstruct.Mapper;
import ru.otus.order.service.model.dto.CartResponseDto;
import ru.otus.order.service.model.entity.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartResponseDto map(Cart cart);
}

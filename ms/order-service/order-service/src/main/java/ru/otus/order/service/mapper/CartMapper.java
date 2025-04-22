package ru.otus.order.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;
import ru.otus.menu.lib.api.DishListResponseDto;
import ru.otus.menu.lib.api.DishResponseDto;
import ru.otus.order.service.model.dto.CartResponseDto;
import ru.otus.order.service.model.dto.ItemResponseDto;
import ru.otus.order.service.model.entity.Cart;
import ru.otus.order.service.model.entity.Order;
import ru.otus.order.service.model.entity.OrderItem;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CartMapper {

    default CartResponseDto map(Cart cart) {
        var dto = new CartResponseDto();
        dto.setUserId(cart.getUserId());
        dto.setTotalPrice(cart.getTotalPrice());
        dto.setItems(map(cart.getItems().values()));
        return dto;
    }

    ItemResponseDto map(Cart.Item item);

    List<ItemResponseDto> map(Collection<Cart.Item> item);

    default Cart map(Order order, DishListResponseDto dto) {
        var actualDishIds = CollectionUtils.isEmpty(dto.getItems()) ? List.of()
                : dto.getItems().stream().map(DishResponseDto::getId).collect(Collectors.toSet());

        var cart = Cart.newCart(order.getUserId());
        var items = order.getItems().stream()
                .filter(i -> actualDishIds.contains(i.getId().getDishId()))
                .map(i -> map(i))
                .collect(Collectors.toMap(i -> i.getDishId(), i -> i, (i1, i2) -> i1));
        cart.addItems(items);
        return cart;
    }

    @Mapping(target = "dishId", source = "item.id.dishId")
    Cart.Item map(OrderItem item);
}

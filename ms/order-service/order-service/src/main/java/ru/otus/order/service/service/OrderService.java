package ru.otus.order.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.common.error.BusinessAppException;
import ru.otus.common.Roles;
import ru.otus.common.UserCtx;
import ru.otus.menu.lib.api.MenuServiceClient;
import ru.otus.order.service.mapper.CartMapper;
import ru.otus.order.service.mapper.OrderMapper;
import ru.otus.order.service.model.OrderEvent;
import ru.otus.order.service.model.dto.AddItemRequestDto;
import ru.otus.order.service.model.dto.CartResponseDto;
import ru.otus.order.service.model.dto.OrderResponseDto;
import ru.otus.order.service.model.entity.Cart;
import ru.otus.order.service.repository.CartRepository;
import ru.otus.order.service.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final MenuServiceClient menuServiceClient;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;
    private final OrderProcessorService orderProcessorService;

    //todo transactional?
    public CartResponseDto addItem(AddItemRequestDto dto, UUID userId) {
        Cart cart;
        var cartOpt = cartRepository.findById(userId);
        if (cartOpt.isPresent()) {
            log.debug("Found existing cart for user with id: {}", userId);
            cart = cartOpt.get();
        } else {
            log.debug("No cart for current user with id: {}. Creating it...", userId);
            cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());
        }

        var newDishId = dto.getDishId();
        var quantity = dto.getQuantity();

        Optional<Cart.Item> existingDish = !CollectionUtils.isEmpty(cart.getItems()) ?
                cart.getItems().stream().filter(d -> d.getDishId().equals(newDishId)).findFirst()
                : Optional.empty();
        if (existingDish.isPresent()) {
            quantity = quantity + existingDish.get().getQuantity();
        }

        var dish = menuServiceClient.getById(newDishId, quantity);
        if (Boolean.FALSE == dish.getIsAvailable()) {
            log.error("Current item with id {} is unavailable", newDishId);
            throw new BusinessAppException("dish.is.unavailable", "Current item is unavailable for the order");
        }

        var newDish = new Cart.Item();
        newDish.setDishId(dish.getDishId());
        newDish.setName(dish.getName());
        newDish.setPrice(dish.getPrice());
        newDish.setIsAvailable(dish.getIsAvailable());

        List<Cart.Item> items = cart.getItems() != null ? cart.getItems() : new ArrayList();
        items.add(newDish);

        var totalPrice = items.stream()
                .map(Cart.Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(totalPrice);
        cart.setItems(items);
        //todo если добавляют в корзину, то продлевать время хранения в редисе
        cartRepository.save(cart);
        return cartMapper.map(cart);
    }

    public CartResponseDto getCart(UUID userId) {
        var cartOpt = cartRepository.findById(userId);
        if (cartOpt.isPresent()) {
            log.debug("Found existing cart for user with id: {}", userId);
            return cartMapper.map(cartOpt.get());
        } else {
            log.debug("No cart for current user with id: {}. Creating it...", userId);
            var cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());
            cart.setTotalPrice(BigDecimal.ZERO);
            cartRepository.save(cart);
            return cartMapper.map(cart);
        }
    }

    @Transactional
    public OrderResponseDto submit(UUID orderId, UserCtx userCtx) {
        var userId = userCtx.getId();
        var cartOpt = cartRepository.findById(userId);
        if (cartOpt.isEmpty()) {
            log.error("Cart for the user with id {} not found", userId);
            throw new BusinessAppException("cart.not.found", "Cart not found");
        }

        var order = orderMapper.map(cartOpt.get(), orderId);
        var createdOrder = orderProcessorService.createOrder(order, userCtx);
        cartRepository.deleteById(userId); //todo check

        return orderMapper.map(createdOrder);
    }

    public OrderResponseDto getStatus(UUID orderId, UserCtx userCtx) {
        var orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            log.error("Order with id {} not found", orderId);
            throw new BusinessAppException("order.not.found", "Order not found");
        }

        var order = orderOpt.get();
        if (userCtx.getRoles().contains(Roles.CLIENT) && order.getUserId() != userCtx.getId()) {
            log.error("User with id {} trying to get not own order with id {}", userCtx.getId(), orderId);
            throw new BusinessAppException("order.not.found", "Order not found");
        }

        return orderMapper.map(order);
    }

    @Transactional
    public OrderResponseDto setStatus(UUID orderId, OrderEvent event) {
        var order = orderProcessorService.setStatus(orderId, event);
        return orderMapper.map(order);
    }
}

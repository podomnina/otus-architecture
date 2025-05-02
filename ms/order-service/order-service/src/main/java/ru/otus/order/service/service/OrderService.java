package ru.otus.order.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.common.error.BusinessAppException;
import ru.otus.common.Roles;
import ru.otus.common.UserCtx;
import ru.otus.menu.lib.api.DishResponseDto;
import ru.otus.menu.lib.api.MenuServiceClient;
import ru.otus.order.service.mapper.CartMapper;
import ru.otus.order.service.mapper.OrderMapper;
import ru.otus.order.service.model.OrderStatus;
import ru.otus.order.service.model.dto.AddItemRequestDto;
import ru.otus.order.service.model.dto.CartResponseDto;
import ru.otus.order.service.model.dto.OrderResponseDto;
import ru.otus.order.service.model.entity.Cart;
import ru.otus.order.service.model.entity.IdempotencyKey;
import ru.otus.order.service.model.entity.Order;
import ru.otus.order.service.repository.CartRepository;
import ru.otus.order.service.repository.IdempotencyRepository;
import ru.otus.order.service.repository.OrderRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final MenuServiceClient menuServiceClient;
    //private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;
    private final OrderProcessorService orderProcessorService;
    private final IdempotencyRepository idempotencyRepository;

    @Transactional
    public CartResponseDto addItem(AddItemRequestDto dto, UUID userId) {
        Cart cart;
        //var cartOpt = cartRepository.findById(userId);
        //if (cartOpt.isPresent()) {
        //    log.debug("Found existing cart for user with id: {}", userId);
        //    cart = cartOpt.get();
        //} else {
        //    log.debug("No cart for current user with id: {}. Creating it...", userId);
        //    cart = Cart.newCart(userId);
        //}

        var newDishId = dto.getDishId();
        var quantity = dto.getQuantity();

        //Optional<Cart.Item> existingDish = !CollectionUtils.isEmpty(cart.getItems()) ?
        //        cart.getItems().values().stream().filter(d -> d != null && d.getDishId().equals(newDishId)).findFirst()
        //        : Optional.empty();
        //if (existingDish.isPresent()) {
        //    quantity = quantity + existingDish.get().getQuantity();
        //}

        DishResponseDto dish = null;
        try {
            dish = menuServiceClient.getById(newDishId, quantity);
        } catch (Exception e) {
            log.error("Unable to get dish info", e);
        }

        if (dish == null || Boolean.FALSE == dish.getIsAvailable()) {
            log.error("Current item with id {} is unavailable", newDishId);
            throw new BusinessAppException("dish.is.unavailable", "Выбранное блюдо недоступно для заказа");
        }

        var newDish = new Cart.Item();
        newDish.setDishId(dish.getId());
        newDish.setName(dish.getName());
        newDish.setPrice(dish.getPrice());
        newDish.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 1);
        newDish.setIsAvailable(dish.getIsAvailable());

        //cart.addItem(newDish);

        //cartRepository.save(cart);
        return cartMapper.map(new Cart());
    }

    @Transactional
    public OrderResponseDto createOrder(AddItemRequestDto dto, UserCtx userCtx, String idempotencyKey) {
        var idempotencyRecord = idempotencyRepository.findById(idempotencyKey);
        if (idempotencyRecord.isPresent()) {
            var orderId = idempotencyRecord.get().getOrderId();
            var orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isEmpty()) {
                log.error("Order with id {} not found", orderId);
                throw new BusinessAppException("order.not.found", "Заказ не найден");
            }

            var order = orderOpt.get();
            return orderMapper.map(order);
        }

        var order = orderMapper.mapMock(dto, userCtx);
        var createdOrder = orderProcessorService.createOrder(order, userCtx);
        var orderId = createdOrder.getId();
        var idempotencyRec = new IdempotencyKey();
        idempotencyRec.setKey(idempotencyKey);
        idempotencyRec.setOrderId(orderId);

        idempotencyRepository.save(idempotencyRec);

        return orderMapper.map(createdOrder);
    }

    @Transactional
    public CartResponseDto getCart(UUID userId) {
        //var cartOpt = cartRepository.findById(userId);
        //if (cartOpt.isPresent()) {
        //    log.debug("Found existing cart for user with id: {}", userId);
        //    return cartMapper.map(cartOpt.get());
        //} else {
        //    log.debug("No cart for current user with id: {}. Creating it...", userId);
        //    var cart = Cart.newCart(userId);
        //    cartRepository.save(cart);
        //    return cartMapper.map(cart);
        //}
        return null;
    }

    @Transactional
    public OrderResponseDto submit(Integer orderId, UserCtx userCtx) {
        var userId = userCtx.getId();
        //var cartOpt = cartRepository.findById(userId);
        //if (cartOpt.isEmpty()) {
        //    log.error("Cart for the user with id {} not found", userId);
        //    throw new BusinessAppException("cart.not.found", "Корзина не найдена");
        //}
        //if (CollectionUtils.isEmpty(cartOpt.get().getItems())) {
        //    log.error("Cart items list is empty for user with id: {}. Unable to submit", userId);
        //    throw new BusinessAppException("cart.items.list.is.empty", "Попытка оформить заказ с пустой корзиной");
        //}

//todo cart items is empty??? можно засабмитить пустую корзину
        //var order = orderMapper.map(cartOpt.get());
        //order.setEmail(userCtx.getLogin());
        //var createdOrder = orderProcessorService.createOrder(order, userCtx);
        //cartRepository.deleteById(userId);

        return orderMapper.map(new Order());
    }

    @Transactional
    public OrderResponseDto getStatus(Integer orderId, UserCtx userCtx) {
        var orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            log.error("Order with id {} not found", orderId);
            throw new BusinessAppException("order.not.found", "Заказ не найден");
        }

        var order = orderOpt.get();
        if (userCtx.getRoles().contains(Roles.CLIENT) && !Objects.equals(order.getUserId(), userCtx.getId())) {
            log.error("User with id {} trying to get not own order with id {}", userCtx.getId(), orderId);
            throw new BusinessAppException("order.not.found", "Заказ не найден");
        }

        return orderMapper.map(order);
    }

    @Transactional
    public OrderResponseDto setStatus(Integer orderId, OrderStatus status) {
        var order = orderProcessorService.setStatus(orderId, status);
        return orderMapper.map(order);
    }
}

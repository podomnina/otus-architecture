package ru.otus.order.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.common.error.BusinessAppException;
import ru.otus.common.Roles;
import ru.otus.common.UserCtx;
import ru.otus.lib.ctx.UserContext;
import ru.otus.order.service.model.OrderEvent;
import ru.otus.order.service.model.OrderStatus;
import ru.otus.order.service.model.dto.AddItemRequestDto;
import ru.otus.order.service.model.dto.CartResponseDto;
import ru.otus.order.service.model.dto.OrderResponseDto;
import ru.otus.order.service.service.OrderService;

@Slf4j
@Validated
@RestController
@RequestMapping(OrderController.BASE_PATH)
@RequiredArgsConstructor
public class OrderController {

    public final static String BASE_PATH = "/api/v1/order";

    private final OrderService service;

    @PostMapping("/cart/add")
    public CartResponseDto addItem(@UserContext UserCtx userCtx, @RequestBody AddItemRequestDto dto) {
        var userId = userCtx.getId();
        log.debug("Trying to add item to order cart {} by user with id: {}", dto, userId);
        return service.addItem(dto, userId);
    }

    @GetMapping("/cart")
    public CartResponseDto getCart(@UserContext UserCtx userCtx) {
        var userId = userCtx.getId();
        log.debug("Trying to get cart by user with id: {}", userId);
        return service.getCart(userId);
    }

    @PostMapping("/cart/submit")
    public OrderResponseDto submit(@UserContext UserCtx userCtx,
                                   @RequestHeader(name = "idempotency-key", required = false) Integer orderId) { //todo???
        var userId = userCtx.getId();
        log.debug("Trying to submit cart by user with id: {}", userId);
        return service.submit(orderId, userCtx);
    }

    @GetMapping("/status/{id}")
    public OrderResponseDto getStatus(@UserContext UserCtx userCtx, @PathVariable Integer id) {
        var userId = userCtx.getId();
        log.debug("Trying to check order status by user with id: {} for order with id: {}", userId, id);
        return service.getStatus(id, userCtx);
    }

    //todo claims roles
    @PostMapping("/status/{id}")
    public OrderResponseDto setStatus(@UserContext UserCtx userCtx,
                                      @PathVariable Integer id,
                                      @RequestParam OrderStatus status) {
        log.debug("Trying to change status for order with id {} by user: {} to status: {}", id, userCtx, status);
        if (userCtx.getRoles().contains(Roles.CLIENT)) { //todo
            log.error("You are not allowed to change status");
            throw new BusinessAppException("order.status.transition.not.allowed", "Изменение статуса заказа недоступно", "403"); //todo check
        }

        if (!OrderStatus.isValidForManualTransition(status)) {
            log.error("Manual transition for {} status is unavailable", status);
            throw new BusinessAppException("order.status.transition.invalid", "Неверный статус перехода для заказа");
        }

        return service.setStatus(id, status);
    }

}

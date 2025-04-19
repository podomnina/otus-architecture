package ru.otus.order.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.common.error.UserCtx;
import ru.otus.order.service.model.dto.AddItemRequestDto;
import ru.otus.order.service.model.dto.CartResponseDto;
import ru.otus.order.service.model.dto.OrderResponseDto;
import ru.otus.order.service.service.OrderService;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping(OrderController.BASE_PATH)
@RequiredArgsConstructor
public class OrderController {

    public final static String BASE_PATH = "/api/v1/order";

    private final OrderService service;

    @PostMapping("/cart/add")
    public CartResponseDto addItem(@AuthenticationPrincipal UserCtx userCtx, @RequestBody AddItemRequestDto dto) {
        var userId = userCtx.getId();
        log.debug("Trying to add item to order cart {} by user with id: {}", dto, userId);
        return service.addItem(dto, userId);
    }

    @GetMapping("/cart")
    public CartResponseDto getCart(@AuthenticationPrincipal UserCtx userCtx) {
        var userId = userCtx.getId();
        log.debug("Trying to get cart by user with id: {}", userId);
        return service.getCart(userId);
    }

    @PostMapping("/cart/submit")
    public OrderResponseDto submit(@AuthenticationPrincipal UserCtx userCtx,
                                   @RequestHeader("idempotency-key") UUID orderId) { //todo???
        var userId = userCtx.getId();
        log.debug("Trying to submit cart by user with id: {}", userId);
        return service.submit(orderId, userId);
    }

    @PostMapping("/status/{id}")
    public OrderResponseDto getStatus(@AuthenticationPrincipal UserCtx userCtx, @PathVariable UUID id) {
        var userId = userCtx.getId();
        log.debug("Trying to check order status by user with id: {} for order with id: {}", userId, id);
        return service.getStatus(id, userCtx);
    }

}

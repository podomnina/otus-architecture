package ru.otus.payment.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.common.UserCtx;
import ru.otus.lib.ctx.UserContext;
import ru.otus.payment.service.model.dto.AccountResponseDto;
import ru.otus.payment.service.model.dto.PaymentResponseDto;
import ru.otus.payment.service.service.PaymentService;

@Slf4j
@Validated
@RestController
@RequestMapping(InternalPaymentController.BASE_PATH)
@RequiredArgsConstructor
public class InternalPaymentController {

    public final static String BASE_PATH = "/api/internal/payment";

    private final PaymentService service;

    @GetMapping
    public PaymentResponseDto get(@RequestParam Integer orderId) {
        return service.getPayment(orderId);
    }
}

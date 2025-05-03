package ru.otus.delivery.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.delivery.service.model.dto.DeliveryResponseDto;
import ru.otus.delivery.service.service.DeliveryService;

@Slf4j
@Validated
@RestController
@RequestMapping(InternalDeliveryController.BASE_PATH)
@RequiredArgsConstructor
public class InternalDeliveryController {

    public final static String BASE_PATH = "/api/internal/delivery";

    private final DeliveryService service;

    @GetMapping
    public DeliveryResponseDto get(@RequestParam Integer orderId) {
        return service.getDelivery(orderId);
    }
}

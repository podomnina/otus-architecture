package ru.otus.payment.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.common.UserCtx;
import ru.otus.lib.ctx.UserContext;
import ru.otus.payment.service.model.dto.AccountResponseDto;
import ru.otus.payment.service.model.dto.FillUpAccountRequestDto;
import ru.otus.payment.service.service.AccountService;

@Slf4j
@Validated
@RestController
@RequestMapping(AccountController.BASE_PATH)
@RequiredArgsConstructor
public class AccountController {

    public final static String BASE_PATH = "/api/v1/account";

    private final AccountService service;

    @GetMapping
    public AccountResponseDto get(@UserContext UserCtx userCtx) {
        var userId = userCtx.getId();
        log.debug("Trying to check account info by user with id: {}", userId);
        return service.get(userId);
    }

    @PostMapping("/fill-up")
    public AccountResponseDto fillUp(@UserContext UserCtx userCtx,
                                     @RequestBody FillUpAccountRequestDto dto) {
        var userId = userCtx.getId();
        log.debug("Trying to fill up account info by user with id: {}", userId);
        return service.fillUp(userId, dto);
    }
}

package ru.otus.auth.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.auth.gateway.model.AuthRequestDto;
import ru.otus.auth.gateway.model.AuthResponseDto;
import ru.otus.auth.gateway.model.TokenRequestDto;
import ru.otus.auth.gateway.model.TokenResponseDto;
import ru.otus.auth.gateway.service.AuthService;

@RestController
@RequestMapping(AuthController.BASE_PATH)
@RequiredArgsConstructor
public class AuthController {

    public final static String BASE_PATH = "/api/v1/auth";
    public final static String PWD_PATH = "/pwd";
    public final static String TOKEN_PATH = "/token";

    private final AuthService service;

    @PostMapping(PWD_PATH)
    public AuthResponseDto auth(@RequestBody AuthRequestDto dto) {
        return service.auth(dto);
    }

    @PostMapping(TOKEN_PATH)
    public TokenResponseDto token(@RequestBody TokenRequestDto dto) {
        return service.token(dto);
    }
}
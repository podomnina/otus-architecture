package ru.otus.auth.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.auth.gateway.mapper.AuthMapper;
import ru.otus.auth.gateway.model.AuthRequestDto;
import ru.otus.auth.gateway.model.AuthResponseDto;
import ru.otus.auth.gateway.model.TokenRequestDto;
import ru.otus.auth.gateway.model.TokenResponseDto;
import ru.otus.auth.service.api.client.AuthServiceClient;
import ru.otus.auth.service.api.dto.AuthServiceRequestDto;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthServiceClient authServiceClient;
    private final JwtService jwtService;
    private final AuthMapper mapper;

    public AuthResponseDto auth(AuthRequestDto dto) {
        if (!StringUtils.hasText(dto.getLogin()) || !StringUtils.hasText(dto.getPassword())) {
            log.error("Login or password is empty");
            throw new NoSuchElementException("Login or password is empty"); //todo process business error
        }

        var userCtx = authServiceClient.authenticate(new AuthServiceRequestDto(dto.getLogin(), dto.getPassword()));

        var token = jwtService.generateToken(userCtx);
        var profile = mapper.toDto(userCtx);
        return AuthResponseDto.builder()
                .token(token)
                .profile(profile)
                .build();
    }

    public TokenResponseDto token(TokenRequestDto dto) {
        return new TokenResponseDto();
    }
}
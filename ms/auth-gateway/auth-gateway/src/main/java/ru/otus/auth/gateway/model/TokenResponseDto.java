package ru.otus.auth.gateway.model;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TokenResponseDto {
    private String refreshToken;
    private OffsetDateTime refreshTokenExpiresIn;
    private String token;
    private OffsetDateTime tokenExpireIn;
    private UserDto profile;
}

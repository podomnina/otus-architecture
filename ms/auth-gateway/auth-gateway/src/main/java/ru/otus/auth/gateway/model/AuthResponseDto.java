package ru.otus.auth.gateway.model;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class AuthResponseDto {
    private String refreshToken;
    private OffsetDateTime refreshTokenExpiresIn;
    private String token;
    private OffsetDateTime tokenExpireIn;
    private UserDto profile;
}

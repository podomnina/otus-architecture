package ru.otus.auth.gateway.model;

import lombok.Data;

@Data
public class TokenRequestDto {
    private String refreshToken;
}

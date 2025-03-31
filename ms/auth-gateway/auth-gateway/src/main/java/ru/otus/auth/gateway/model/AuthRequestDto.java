package ru.otus.auth.gateway.model;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String login;
    private String password;
}

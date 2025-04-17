package ru.otus.auth.service.model.dto.auth;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private UUID restaurantId;
    private String restaurantCode;
}
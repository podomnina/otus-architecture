package ru.otus.auth.gateway.model;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private UUID restaurantId;
    private String restaurantCode;
}

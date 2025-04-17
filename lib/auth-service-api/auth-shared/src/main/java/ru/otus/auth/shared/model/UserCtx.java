package ru.otus.auth.shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCtx {
    private UUID id;
    private String login;
    private UUID restaurantId;
    private String restaurantCode;
}

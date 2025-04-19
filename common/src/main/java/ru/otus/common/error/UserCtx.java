package ru.otus.common.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCtx {
    private UUID id;
    private String login;
    private UUID restaurantId;
    private String restaurantCode;
    private List<String> roles;
}

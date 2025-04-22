package ru.otus.common;

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
    private String firstName;
    private List<Roles> roles;
}

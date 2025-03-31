package ru.otus.auth.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponseDto {
    private List<UserResponseDto> users;
}

package ru.otus.auth.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.auth.service.model.dto.CreateUserRequestDto;
import ru.otus.auth.service.model.dto.UpdateUserRequestDto;
import ru.otus.auth.service.model.dto.UserListResponseDto;
import ru.otus.auth.service.model.dto.UserResponseDto;
import ru.otus.auth.service.service.UserService;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping(UserController.BASE_PATH)
@RequiredArgsConstructor
public class UserController {

    public final static String BASE_PATH = "/api/v1/user";

    private final UserService userService;

    @PostMapping
    public UserResponseDto create(@RequestBody CreateUserRequestDto dto) {
        log.debug("Trying to create user with data: {}", dto);
        return userService.create(dto);
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable UUID id) {
        log.debug("Trying to get user by id: {}", id);
        return userService.getById(id);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable UUID id, @RequestBody UpdateUserRequestDto dto) {
        log.debug("Trying to update user by id: {}", id);
        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        log.debug("Trying to delete user by id: {}", id);
        userService.delete(id);
    }

    @GetMapping
    public UserListResponseDto getAll() {
        log.debug("Trying to get all users");
        return userService.getAll();
    }
}

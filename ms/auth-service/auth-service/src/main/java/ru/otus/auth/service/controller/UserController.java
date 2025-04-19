package ru.otus.auth.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.auth.service.model.dto.user.UpdateUserRequestDto;
import ru.otus.auth.service.model.dto.user.UserListResponseDto;
import ru.otus.auth.service.model.dto.user.UserResponseDto;
import ru.otus.auth.service.service.UserService;
import ru.otus.common.error.UserCtx;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping(UserController.BASE_PATH)
@RequiredArgsConstructor
public class UserController {

    public final static String BASE_PATH = "/api/v1/user";

    private final UserService userService;

    /*
    @PostMapping
    public UserResponseDto create(@RequestBody CreateUserRequestDto dto) {
        log.debug("Trying to create user with data: {}", dto);
        return userService.create(dto);
    }
     */

    @GetMapping("/{id}")
    public UserResponseDto getById(@AuthenticationPrincipal UserCtx userCtx,
                                   @PathVariable UUID id) {
        log.debug("Trying to get user by id: {}", id);
        return userService.getById(id, userCtx);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@AuthenticationPrincipal UserCtx userCtx,
                                  @PathVariable UUID id, @RequestBody UpdateUserRequestDto dto) {
        log.debug("Trying to update user by id: {}", id);
        return userService.update(id, dto, userCtx);
    }

    /*
        @DeleteMapping("/{id}")
        public void delete(@PathVariable UUID id) {
            log.debug("Trying to delete user by id: {}", id);
            userService.delete(id);
        }
    */
    @GetMapping
    public UserListResponseDto getAll() {
        log.debug("Trying to get all users");
        return userService.getAll();
    }
}

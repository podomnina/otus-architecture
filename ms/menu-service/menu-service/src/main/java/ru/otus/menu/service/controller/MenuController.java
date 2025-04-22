package ru.otus.menu.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.menu.service.model.dto.MenuListResponseDto;
import ru.otus.menu.service.service.MenuService;

@Slf4j
@Validated
@RestController
@RequestMapping(MenuController.BASE_PATH)
@RequiredArgsConstructor
public class MenuController {

    public final static String BASE_PATH = "/api/v1/menu";

    private final MenuService service;

    @GetMapping("/actual")
    public MenuListResponseDto getMenu() {
        log.debug("Trying to get actual menu");
        return service.getActualMenu();
    }
}

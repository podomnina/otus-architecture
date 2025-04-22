package ru.otus.menu.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuListResponseDto {
    private List<MenuItemDto> starters = new ArrayList<>();
    private List<MenuItemDto> salad = new ArrayList<>();
    private List<MenuItemDto> soup = new ArrayList<>();
    private List<MenuItemDto> fish = new ArrayList<>();
    private List<MenuItemDto> meat = new ArrayList<>();
    private List<MenuItemDto> garnish = new ArrayList<>();
    private List<MenuItemDto> dessert = new ArrayList<>();
    private List<MenuItemDto> drinks = new ArrayList<>();
}

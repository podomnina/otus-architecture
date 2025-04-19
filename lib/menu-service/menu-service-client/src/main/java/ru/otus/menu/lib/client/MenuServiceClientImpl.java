package ru.otus.menu.lib.client;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.otus.menu.lib.api.DishResponseDto;
import ru.otus.menu.lib.api.MenuServiceClient;

import java.util.UUID;

@Component
public class MenuServiceClientImpl implements MenuServiceClient {

    @Value("${webclient.menuService.url}")
    private String baseUrl;
    private RestClient client;


    @PostConstruct
    public void init() {
        client = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public DishResponseDto getById(UUID dishId, Integer quantity) {
        return client.get()
                .uri(BASE_INTERNAL_URL + DISH_URL + "/" + dishId,
                        uriBuilder -> uriBuilder
                                .queryParam("quantity", quantity).build()
                )
                .retrieve()
                .body(DishResponseDto.class);
    }
}
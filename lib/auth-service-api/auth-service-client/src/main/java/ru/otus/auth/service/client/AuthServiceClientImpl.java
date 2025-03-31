package ru.otus.auth.service.client;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.otus.auth.service.api.client.AuthServiceClient;
import ru.otus.auth.service.api.dto.AuthServiceRequestDto;
import ru.otus.auth.service.api.model.AuthContext;

@Component
public class AuthServiceClientImpl implements AuthServiceClient {

    private RestClient client;

    @PostConstruct
    public void init() {
        client = RestClient.builder()
                .baseUrl("http://localhost:8081") //todo
                .build();
    }

    @Override
    public AuthContext authenticate(AuthServiceRequestDto dto) {
        return client.get()
                .uri(AuthServiceClient.AUTHENTICATE_BASE_URL)
                .retrieve()
                .body(AuthContext.class);
    }
}

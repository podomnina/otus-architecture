package ru.otus.auth.service.client;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.otus.auth.shared.model.AuthContext;

@Component
public class AuthServiceClientImpl {

    private RestClient client;

    @PostConstruct
    public void init() {
        client = RestClient.builder()
                .baseUrl("http://localhost:8081") //todo
                .build();
    }

    public AuthContext authenticate(Object dto) {
        return client.get()
                .uri("")
                .retrieve()
                .body(AuthContext.class);
    }
}

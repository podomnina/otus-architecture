package ru.otus.auth.service.api.client;

import ru.otus.auth.service.api.dto.AuthServiceRequestDto;
import ru.otus.auth.service.api.model.AuthContext;

public interface AuthServiceClient {
    AuthContext authenticate(AuthServiceRequestDto dto);

    String BASE_INTERNAL_URL = "/api/internal";

    String AUTHENTICATE_BASE_URL = BASE_INTERNAL_URL + "/auth";
}

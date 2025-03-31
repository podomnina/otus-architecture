package ru.otus.auth.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "ru.otus.auth.security")
@Component
public class JwtConfigProps {
    private String jwtSecret;
    private String jwtRefreshSecret;
    private Long jwtExpirationInMs;
    private Long jwtRefreshExpirationInMs;
}
package ru.otus.auth.shared.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProps {
    private String secret;
    private String refreshSecret;
    private Long expirationInMs;
    private Long refreshExpirationInMs;
}
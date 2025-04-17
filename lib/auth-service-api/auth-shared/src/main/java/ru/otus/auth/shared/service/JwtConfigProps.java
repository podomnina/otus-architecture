package ru.otus.auth.shared.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProps {
    private String secret; //todo
    private String refreshSecret;
    private Long expirationInMs; //todo default value?
    private Long refreshExpirationInMs;
}
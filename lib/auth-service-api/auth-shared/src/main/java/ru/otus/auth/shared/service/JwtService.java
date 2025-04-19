package ru.otus.auth.shared.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.common.error.UserCtx;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    //private final Key jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final JwtConfigProps properties;
    private Key signingKey;
    private Key refreshSigningKey;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        signingKey = getSigningKey(properties.getSecret());
        if (properties.getRefreshSecret() != null) {
            refreshSigningKey = getSigningKey(properties.getRefreshSecret());
        }
    }

    @SneakyThrows
    public String generateToken(UserCtx userCtx) {
        var expirationTime = properties.getExpirationInMs();
        return generateToken(userCtx, expirationTime, signingKey);
    }

    @SneakyThrows
    public String generateRefreshToken(UserCtx userCtx) {
        var expirationTime = properties.getRefreshExpirationInMs();
        return generateToken(userCtx, expirationTime, signingKey);
    }

    @SneakyThrows
    public String generateToken(UserCtx userCtx, Long expirationTime, Key key) {
        var subject = userCtx != null ? objectMapper.writeValueAsString(userCtx) : null;
        var expiration = expirationTime != -1 ? new Date(System.currentTimeMillis() + expirationTime) : null;
        return Jwts.builder()
                .setSubject(subject)
                .claim("roles", userCtx.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public Pair<Boolean, String> isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build()
                    .parseClaimsJws(token);
            return Pair.of(true, "");
        } catch (SignatureException | MalformedJwtException ex) {
            log.error("Invalid JWT signature");
            return Pair.of(false, "Invalid JWT signature");
        } catch (ExpiredJwtException ex) {
            log.debug("Expired JWT token");
            return Pair.of(false, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            return Pair.of(false, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            return Pair.of(false, "JWT claims string is empty");
        } catch (Exception e) {
            log.error("error get user from token", e);
            return Pair.of(false, "Error while processing JWT token");
        }
    }

    @SneakyThrows
    public UserCtx getUserCtxFromToken(String token) {
            var subject = Jwts.parserBuilder().setSigningKey(signingKey).build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return objectMapper.readValue(subject, UserCtx.class);
    }

    private Key getSigningKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
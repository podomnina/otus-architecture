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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.otus.common.UserCtx;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

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
            return Pair.of(false, "Недействительная подпись JWT токена");
        } catch (ExpiredJwtException ex) {
            log.debug("Expired JWT token");
            return Pair.of(false, "Истек срок действия JWT токена");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            return Pair.of(false, "Неподдерживаемый JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            return Pair.of(false, "JWT claims не указаны");
        } catch (Exception e) {
            log.error("error get user from token", e);
            return Pair.of(false, "Ошибка во время обработки JWT токена");
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

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);

        if (claims.get("authorities") != null) {
            @SuppressWarnings("unchecked")
            List<String> authorities = claims.get("authorities", List.class);
            return authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getPayloadFromToken(String token) {
        String[] chunks = token.split("\\.");
        //Base64.Decoder decoder = Base64.getUrlDecoder();
        //return new String(decoder.decode(chunks[1]));
        return chunks[1];
    }

    private Key getSigningKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
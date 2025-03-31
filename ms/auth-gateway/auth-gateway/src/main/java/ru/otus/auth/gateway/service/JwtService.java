package ru.otus.auth.gateway.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.otus.auth.gateway.config.JwtConfigProps;
import ru.otus.auth.service.api.model.AuthContext;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfigProps configProps;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return false; // extractExpiration(token).before(new Date()); //todo добавить просроченность токена
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof AuthContext ctx) {
            claims.put("id", ctx.getId());
            claims.put("restaurantId", ctx.getRestaurantId());
            claims.put("restaurantCode", ctx.getRestaurantCode());
        }
        return generateToken(claims, userDetails);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //.setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(configProps.getJwtSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

package com.jhe.question_bank.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    private static final String ACCESS = "access";
    private static final String REFRESH = "refresh";

    public String createAccessToken(String userId) {

        Date expiration = Date.from(Instant.now().plus(5, ChronoUnit.MINUTES));

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        String accessToken = null;

        try {

            accessToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(userId)
                .claim("type", ACCESS)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .compact();

        } catch (Exception exception) {
            log.debug("error = {}", exception);
        }
        
        return accessToken;
    }

    public String createRefreshToken(String userId) {

        Date expiration = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        String refreshToken = null;

        try {

            refreshToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(userId)
                .claim("type", REFRESH)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .compact();

        } catch (Exception exception) {
            log.debug("error = {}", exception);
        }
        
        return refreshToken;
    }

    public String validateAccessToken(String jwt) {

        String userId = null;

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {

            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
            if (claims == null) return null;

            String type = claims.get("type", String.class);
            boolean isAccessToken = ACCESS.equals(type);
            if (!isAccessToken) return null;

            userId = claims.getSubject();
            
        } catch (Exception exception) {
            log.debug("error = {}", exception);
        }

        return userId;
    }

    public String validateRefreshToken(String jwt) {

        String userId = null;

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {

            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
            if (claims == null) return null;

            String type = claims.get("type", String.class);
            boolean isRefreshToken = REFRESH.equals(type);
            if (!isRefreshToken) return null;

            userId = claims.getSubject();
            
        } catch (Exception exception) {
            log.debug("error = {}", exception);
        }

        return userId;
    }

}

package com.jhe.question_bank.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    public String createAccessToken(String userId) {

        Date expiration = Date.from(Instant.now().plus(5, ChronoUnit.MINUTES));

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        String accessToken = null;

        try {

            accessToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .compact();

        } catch (Exception exception) {
            exception.printStackTrace();
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
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .compact();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        
        return refreshToken;
    }

    public String validate(String jwt) {

        String userId = null;

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {

            userId = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return userId;
    }

}

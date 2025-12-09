package com.example.SentiStock_backend.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyPlain;

    @Value("${jwt.access-token-validity-ms:1800000}")      // 30분
    private long accessTokenValidityInMs;

    @Value("${jwt.refresh-token-validity-ms:1209600000}")  // 14일
    private long refreshTokenValidityInMs;

    private SecretKey secretKey;

    private final CustomUserDetailsService userDetailsService;

    public JwtTokenProvider(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyPlain.getBytes(StandardCharsets.UTF_8));
    }


    public String createAccessToken(String userId) {
        return createToken(userId, accessTokenValidityInMs, "ACCESS");
    }

    public String createRefreshToken(String userId) {
        return createToken(userId, refreshTokenValidityInMs, "REFRESH");
    }

    private String createToken(String userId, long validityInMs, String type) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(validityInMs);

        return Jwts.builder()
                .subject(userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .claim("type", type)   
                .signWith(secretKey)
                .compact();
    }

    public String getUserId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰이 REFRESH인지 확인
    public boolean isRefreshToken(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String type = claims.get("type", String.class);
            return "REFRESH".equals(type);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String userId = getUserId(token);
        var userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }

    public long getRefreshTokenValidityInMs() {
        return refreshTokenValidityInMs;
    }
}

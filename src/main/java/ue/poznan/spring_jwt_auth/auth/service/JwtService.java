package ue.poznan.spring_jwt_auth.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ue.poznan.spring_jwt_auth.auth.config.AuthConfigurationProperties;
import ue.poznan.spring_jwt_auth.auth.config.JwtConstraintsUtils;
import ue.poznan.spring_jwt_auth.auth.roles.Role;
import ue.poznan.spring_jwt_auth.user.domain.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static ue.poznan.spring_jwt_auth.auth.config.JwtConstraintsUtils.Claims.*;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final AuthConfigurationProperties properties;

    public String generateToken(User user) {
        return buildToken(
                Map.of(
                        ID, user.getId(),
                        USERNAME, user.getUsername(),
                        JWT_TOKEN_TYPE, JwtConstraintsUtils.Token.AUTHORIZATION,
                        ROLES, user.getRoles()
                ),
                user.getUsername(),
                properties.getExpirationTime());
    }

    public List<Role> getRoles(Claims claims) {
        List<?> raw = claims.get(ROLES, List.class);
        if (raw == null) return List.of();

        return raw.stream()
                .map(Role::valueOf)
                .toList();
    }

    public String generateRefreshToken(User user) {
        return buildToken(
                Map.of(JWT_TOKEN_TYPE, JwtConstraintsUtils.Token.REFRESH),
                user.getUsername(),
                properties.getExpirationTime());
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            String username,
            long expiration
    ) {
        return Jwts
                .builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claims(extraClaims)
                .subject(username)
                .signWith(getSignKey())
                .compact();
    }

    @SneakyThrows
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(properties.getSecret()));
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}

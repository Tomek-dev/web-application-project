package ue.poznan.spring_jwt_auth.auth.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("security.auth")
public class AuthConfigurationProperties {
    private final String secret;
    private final long expirationTime;
    private final long refreshTokenExpirationTime;
}

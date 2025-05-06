package ue.poznan.spring_jwt_auth.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ue.poznan.spring_jwt_auth.auth.config.AuthConfigurationProperties;
import ue.poznan.spring_jwt_auth.auth.dto.LoginResponseDTO;
import ue.poznan.spring_jwt_auth.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthConfigurationProperties properties;

    public LoginResponseDTO login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        var user = userRepository.findByUsername(username).orElseThrow();

        return LoginResponseDTO.builder()
                .token(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .expiresIn(properties.getExpirationTime())
                .refreshTokenExpiresIn(properties.getRefreshTokenExpirationTime())
                .build();
    }

    public LoginResponseDTO refresh(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        var user = userRepository.findByUsername(username).orElseThrow();

        return LoginResponseDTO.builder()
                .token(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .expiresIn(properties.getExpirationTime())
                .refreshTokenExpiresIn(properties.getRefreshTokenExpirationTime())
                .build();
    }
}

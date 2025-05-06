package ue.poznan.spring_jwt_auth.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ue.poznan.spring_jwt_auth.auth.dto.LoginRequestDTO;
import ue.poznan.spring_jwt_auth.auth.dto.LoginResponseDTO;
import ue.poznan.spring_jwt_auth.auth.dto.SignUpRequestDTO;
import ue.poznan.spring_jwt_auth.auth.service.AuthService;
import ue.poznan.spring_jwt_auth.auth.service.SignUpService;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final SignUpService signUpService;
    private final AuthService authService;

    @PostMapping("login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
    }

    @PostMapping("signup")
    public void signup(@RequestBody SignUpRequestDTO signupRequestDTO) {
        signUpService.signUp(signupRequestDTO);
    }

    @PostMapping("refresh-token")
    public LoginResponseDTO refreshToken(Authentication authentication) {
        return authService.refresh((String) authentication.getPrincipal(), (String) authentication.getCredentials());
    }
}

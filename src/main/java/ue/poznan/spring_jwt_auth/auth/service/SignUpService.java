package ue.poznan.spring_jwt_auth.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ue.poznan.spring_jwt_auth.auth.dto.SignUpRequestDTO;
import ue.poznan.spring_jwt_auth.auth.roles.Role;
import ue.poznan.spring_jwt_auth.user.UserRepository;
import ue.poznan.spring_jwt_auth.user.domain.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequestDTO signUpRequestDTO) {
        var user = User.builder()
                .username(signUpRequestDTO.getUsername())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .email(signUpRequestDTO.getEmail())
                .roles(List.of(Role.USER))
                .build();
        userRepository.save(user);
    }
}

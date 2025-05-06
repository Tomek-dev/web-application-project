package ue.poznan.spring_jwt_auth.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ue.poznan.spring_jwt_auth.user.UserRepository;
import ue.poznan.spring_jwt_auth.user.domain.User;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Not found user with id: %s", id)));
    }
}

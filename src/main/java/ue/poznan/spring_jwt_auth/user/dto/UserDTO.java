package ue.poznan.spring_jwt_auth.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDTO {
    private UUID id;

    private String email;

    private String username;
}

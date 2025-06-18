package ue.poznan.spring_jwt_auth.user.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String username;
    private String email;
}

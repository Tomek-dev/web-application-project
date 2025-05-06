package ue.poznan.spring_jwt_auth.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private long expiresIn;
    private String refreshToken;
    private long refreshTokenExpiresIn;
}

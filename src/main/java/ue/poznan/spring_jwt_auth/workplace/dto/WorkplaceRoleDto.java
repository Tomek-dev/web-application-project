package ue.poznan.spring_jwt_auth.workplace.dto;

import lombok.Data;
import ue.poznan.spring_jwt_auth.workplace.domain.UserRoleType;
import java.util.UUID;

@Data
public class WorkplaceRoleDto {
    private UUID id;
    private UUID userId;
    private String username;
    private String email;
    private UserRoleType roleType;
} 
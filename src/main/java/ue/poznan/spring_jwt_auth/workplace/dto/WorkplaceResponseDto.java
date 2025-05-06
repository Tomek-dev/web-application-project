package ue.poznan.spring_jwt_auth.workplace.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class WorkplaceResponseDto {
    private UUID id;
    private String name;
    private String description;
} 
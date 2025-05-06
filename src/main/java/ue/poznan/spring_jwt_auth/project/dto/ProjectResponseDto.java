package ue.poznan.spring_jwt_auth.project.dto;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class ProjectResponseDto {
    private UUID id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private UUID workplaceId;
} 
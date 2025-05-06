package ue.poznan.spring_jwt_auth.sprint.dto;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class SprintResponseDto {
    private UUID id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private UUID projectId;
} 
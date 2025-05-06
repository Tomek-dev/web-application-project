package ue.poznan.spring_jwt_auth.project.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ProjectRequestDto {
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
} 
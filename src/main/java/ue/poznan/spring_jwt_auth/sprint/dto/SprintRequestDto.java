package ue.poznan.spring_jwt_auth.sprint.dto;

import lombok.Data;
import java.util.Date;

@Data
public class SprintRequestDto {
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
} 
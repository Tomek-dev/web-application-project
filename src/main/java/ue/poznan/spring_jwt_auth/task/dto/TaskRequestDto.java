package ue.poznan.spring_jwt_auth.task.dto;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class TaskRequestDto {
    private String title;
    private String description;
    private Date deadline;
    private UUID sprintId; // optional
    private UUID assignedUserId; // optional
} 
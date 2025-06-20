package ue.poznan.spring_jwt_auth.task.dto;

import lombok.Data;
import ue.poznan.spring_jwt_auth.user.dto.UserDto;
import java.util.Date;
import java.util.UUID;

@Data
public class TaskResponseDto {
    private UUID id;
    private String title;
    private String description;
    private Date deadline;
    private UUID projectId;
    private UUID sprintId;
    private UUID assignedUserId;
    private UserDto assignedUser;
} 
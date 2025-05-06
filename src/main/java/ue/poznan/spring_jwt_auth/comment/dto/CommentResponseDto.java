package ue.poznan.spring_jwt_auth.comment.dto;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class CommentResponseDto {
    private UUID id;
    private String content;
    private UUID authorId;
    private Date createdAt;
    private UUID taskId;
} 
package ue.poznan.spring_jwt_auth.comment.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ue.poznan.spring_jwt_auth.comment.domain.Comment;
import ue.poznan.spring_jwt_auth.comment.dto.CommentRequestDto;
import ue.poznan.spring_jwt_auth.comment.dto.CommentResponseDto;
import ue.poznan.spring_jwt_auth.comment.repository.CommentRepository;
import ue.poznan.spring_jwt_auth.task.domain.Task;
import ue.poznan.spring_jwt_auth.task.repository.TaskRepository;
import ue.poznan.spring_jwt_auth.user.domain.User;
import ue.poznan.spring_jwt_auth.user.service.UserService;
import ue.poznan.spring_jwt_auth.workplace.domain.UserRoleType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private boolean canComment(User user) {
        // User must have at least one non-VIEWER role in any workplace
        return user.getWorkplaceRoles().stream()
                .anyMatch(role -> role.getRoleType() != UserRoleType.VIEWER);
    }

    @Transactional
    public CommentResponseDto createComment(UUID taskId, CommentRequestDto dto) {
        User currentUser = userService.getCurrentUser();
        if (!canComment(currentUser)) {
            throw new AccessDeniedException("Not authorized to comment");
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Comment comment = Comment.builder()
                .content(dto.getContent())
                .author(currentUser)
                .task(task)
                .build();
        comment = commentRepository.save(comment);
        return toResponseDto(comment);
    }

    @Transactional
    public void deleteComment(UUID commentId) {
        User currentUser = userService.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        if (!canComment(currentUser)) {
            throw new AccessDeniedException("Not authorized to delete comment");
        }
        commentRepository.delete(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(UUID commentId, CommentRequestDto dto) {
        User currentUser = userService.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        if (!canComment(currentUser)) {
            throw new AccessDeniedException("Not authorized to update comment");
        }
        comment.setContent(dto.getContent());
        comment = commentRepository.save(comment);
        return toResponseDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByTask(UUID taskId) {
        return commentRepository.findByTaskId(taskId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private CommentResponseDto toResponseDto(Comment comment) {
        return modelMapper.map(comment, CommentResponseDto.class);
    }
} 
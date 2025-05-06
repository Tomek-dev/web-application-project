package ue.poznan.spring_jwt_auth.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ue.poznan.spring_jwt_auth.comment.dto.CommentRequestDto;
import ue.poznan.spring_jwt_auth.comment.dto.CommentResponseDto;
import ue.poznan.spring_jwt_auth.comment.service.CommentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("task/{taskId}")
    public CommentResponseDto createComment(@PathVariable UUID taskId, @RequestBody CommentRequestDto dto) {
        return commentService.createComment(taskId, dto);
    }

    @PutMapping("{id}")
    public CommentResponseDto updateComment(@PathVariable UUID id, @RequestBody CommentRequestDto dto) {
        return commentService.updateComment(id, dto);
    }

    @DeleteMapping("{id}")
    public void deleteComment(@PathVariable UUID id) {
        commentService.deleteComment(id);
    }

    @GetMapping("task/{taskId}")
    public List<CommentResponseDto> getCommentsByTask(@PathVariable UUID taskId) {
        return commentService.getCommentsByTask(taskId);
    }
} 
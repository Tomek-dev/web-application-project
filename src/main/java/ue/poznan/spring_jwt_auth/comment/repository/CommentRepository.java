package ue.poznan.spring_jwt_auth.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ue.poznan.spring_jwt_auth.comment.domain.Comment;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByTaskId(UUID taskId);
} 
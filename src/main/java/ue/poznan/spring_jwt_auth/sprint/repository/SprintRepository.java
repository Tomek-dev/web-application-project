package ue.poznan.spring_jwt_auth.sprint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ue.poznan.spring_jwt_auth.sprint.domain.Sprint;
import java.util.List;
import java.util.UUID;

public interface SprintRepository extends JpaRepository<Sprint, UUID> {
    List<Sprint> findByProjectId(UUID projectId);
} 
package ue.poznan.spring_jwt_auth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ue.poznan.spring_jwt_auth.project.domain.Project;
import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByWorkplaceId(UUID workplaceId);
    List<Project> findByWorkplaceRolesUserId(UUID userId);
} 
package ue.poznan.spring_jwt_auth.workplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ue.poznan.spring_jwt_auth.workplace.domain.WorkplaceRole;
import java.util.List;
import java.util.UUID;

public interface WorkplaceRoleRepository extends JpaRepository<WorkplaceRole, UUID> {
    List<WorkplaceRole> findByUserId(UUID userId);
    List<WorkplaceRole> findByWorkplaceId(UUID workplaceId);
} 
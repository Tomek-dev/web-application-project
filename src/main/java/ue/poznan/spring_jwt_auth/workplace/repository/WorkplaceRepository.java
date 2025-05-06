package ue.poznan.spring_jwt_auth.workplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ue.poznan.spring_jwt_auth.workplace.domain.Workplace;
import java.util.List;
import java.util.UUID;

public interface WorkplaceRepository extends JpaRepository<Workplace, UUID> {
    List<Workplace> findByRolesUserId(UUID userId);
    List<Workplace> findByUserId(UUID userId);
}

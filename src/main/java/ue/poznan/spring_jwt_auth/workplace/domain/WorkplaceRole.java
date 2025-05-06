package ue.poznan.spring_jwt_auth.workplace.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ue.poznan.spring_jwt_auth.user.domain.User;
import ue.poznan.spring_jwt_auth.utils.BaseEntity;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "workplace_roles")
public class WorkplaceRole extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "workplace_id")
    private Workplace workplace;

    @Enumerated(EnumType.STRING)
    private UserRoleType roleType;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private Date assignedAt;
}

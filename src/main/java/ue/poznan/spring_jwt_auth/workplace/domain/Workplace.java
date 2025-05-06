package ue.poznan.spring_jwt_auth.workplace.domain;

import jakarta.persistence.*;
import lombok.*;
import ue.poznan.spring_jwt_auth.project.domain.Project;
import ue.poznan.spring_jwt_auth.user.domain.User;
import ue.poznan.spring_jwt_auth.utils.BaseEntity;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "workplaces")
public class Workplace extends BaseEntity {
    private String name;
    private String description;

    @OneToMany(mappedBy = "workplace")
    private List<Project> projects;

    @OneToMany(mappedBy = "workplace")
    private List<WorkplaceRole> roles;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User user;
}

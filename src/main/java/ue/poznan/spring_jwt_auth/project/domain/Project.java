package ue.poznan.spring_jwt_auth.project.domain;

import jakarta.persistence.*;
import lombok.*;
import ue.poznan.spring_jwt_auth.sprint.domain.Sprint;
import ue.poznan.spring_jwt_auth.task.domain.Task;
import ue.poznan.spring_jwt_auth.utils.BaseEntity;
import ue.poznan.spring_jwt_auth.workplace.domain.Workplace;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projects")
public class Project extends BaseEntity {
    private String name;
    private String description;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "workplace_id")
    private Workplace workplace;

    @OneToMany(mappedBy = "project")
    private List<Sprint> sprints;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;
}

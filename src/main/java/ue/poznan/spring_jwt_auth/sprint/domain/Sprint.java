package ue.poznan.spring_jwt_auth.sprint.domain;

import jakarta.persistence.*;
import lombok.*;
import ue.poznan.spring_jwt_auth.task.domain.Task;
import ue.poznan.spring_jwt_auth.utils.BaseEntity;
import ue.poznan.spring_jwt_auth.project.domain.Project;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sprints")
public class Sprint extends BaseEntity {
    private String name;
    private String description;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "sprint")
    private List<Task> tasks;
}

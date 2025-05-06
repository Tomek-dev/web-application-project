package ue.poznan.spring_jwt_auth.task.domain;

import jakarta.persistence.*;
import lombok.*;
import ue.poznan.spring_jwt_auth.comment.domain.Comment;
import ue.poznan.spring_jwt_auth.project.domain.Project;
import ue.poznan.spring_jwt_auth.utils.BaseEntity;
import ue.poznan.spring_jwt_auth.user.domain.User;
import ue.poznan.spring_jwt_auth.sprint.domain.Sprint;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class Task extends BaseEntity {
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Temporal(TemporalType.DATE)
    private Date deadline;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;
}

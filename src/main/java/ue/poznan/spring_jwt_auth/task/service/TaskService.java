package ue.poznan.spring_jwt_auth.task.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ue.poznan.spring_jwt_auth.project.domain.Project;
import ue.poznan.spring_jwt_auth.project.repository.ProjectRepository;
import ue.poznan.spring_jwt_auth.sprint.domain.Sprint;
import ue.poznan.spring_jwt_auth.sprint.repository.SprintRepository;
import ue.poznan.spring_jwt_auth.task.domain.Task;
import ue.poznan.spring_jwt_auth.task.dto.TaskRequestDto;
import ue.poznan.spring_jwt_auth.task.dto.TaskResponseDto;
import ue.poznan.spring_jwt_auth.task.repository.TaskRepository;
import ue.poznan.spring_jwt_auth.user.domain.User;
import ue.poznan.spring_jwt_auth.user.UserRepository;
import ue.poznan.spring_jwt_auth.user.service.UserService;
import ue.poznan.spring_jwt_auth.workplace.domain.UserRoleType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private boolean hasTaskPermission(Project project, User user) {
        return project.getWorkplace().getRoles().stream()
                .anyMatch(role -> role.getUser().getId().equals(user.getId()) &&
                        (role.getRoleType() == UserRoleType.ADMIN
                                || role.getRoleType() == UserRoleType.PROJECT_MANAGER
                                || role.getRoleType() == UserRoleType.DEVELOPER
                                || role.getRoleType() == UserRoleType.TESTER));
    }

    @Transactional
    public TaskResponseDto createTask(UUID projectId, TaskRequestDto dto) {
        User currentUser = userService.getCurrentUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        if (!hasTaskPermission(project, currentUser)) {
            throw new AccessDeniedException("Not authorized to create task in this project");
        }
        Sprint sprint = null;
        if (dto.getSprintId() != null) {
            sprint = sprintRepository.findById(dto.getSprintId())
                    .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));
        }
        User assignedUser = null;
        if (dto.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(dto.getAssignedUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found"));
        }
        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .deadline(dto.getDeadline())
                .project(project)
                .sprint(sprint)
                .assignedUser(assignedUser)
                .build();
        task = taskRepository.save(task);
        return toResponseDto(task);
    }

    @Transactional
    public void deleteTask(UUID taskId) {
        User currentUser = userService.getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!hasTaskPermission(task.getProject(), currentUser)) {
            throw new AccessDeniedException("Not authorized to delete this task");
        }
        taskRepository.delete(task);
    }

    @Transactional
    public TaskResponseDto updateTask(UUID taskId, TaskRequestDto dto) {
        User currentUser = userService.getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!hasTaskPermission(task.getProject(), currentUser)) {
            throw new AccessDeniedException("Not authorized to update this task");
        }
        Sprint sprint = null;
        if (dto.getSprintId() != null) {
            sprint = sprintRepository.findById(dto.getSprintId())
                    .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));
        }
        User assignedUser = null;
        if (dto.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(dto.getAssignedUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found"));
        }
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        task.setSprint(sprint);
        task.setAssignedUser(assignedUser);
        task = taskRepository.save(task);
        return toResponseDto(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByProject(UUID projectId) {
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return taskRepository.findByAssignedUserId(currentUser.getId())
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskResponseDto assignUser(UUID taskId, UUID userId) {
        User currentUser = userService.getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!hasTaskPermission(task.getProject(), currentUser)) {
            throw new AccessDeniedException("Not authorized to assign user to this task");
        }
        User assignedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Assigned user not found"));
        task.setAssignedUser(assignedUser);
        task = taskRepository.save(task);
        return toResponseDto(task);
    }

    @Transactional
    public TaskResponseDto assignSprint(UUID taskId, UUID sprintId) {
        User currentUser = userService.getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!hasTaskPermission(task.getProject(), currentUser)) {
            throw new AccessDeniedException("Not authorized to assign sprint to this task");
        }
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));
        task.setSprint(sprint);
        task = taskRepository.save(task);
        return toResponseDto(task);
    }

    @Transactional
    public TaskResponseDto deassignUser(UUID taskId) {
        User currentUser = userService.getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!hasTaskPermission(task.getProject(), currentUser)) {
            throw new AccessDeniedException("Not authorized to deassign user from this task");
        }
        task.setAssignedUser(null);
        task = taskRepository.save(task);
        return toResponseDto(task);
    }

    @Transactional
    public TaskResponseDto deassignSprint(UUID taskId) {
        User currentUser = userService.getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!hasTaskPermission(task.getProject(), currentUser)) {
            throw new AccessDeniedException("Not authorized to deassign sprint from this task");
        }
        task.setSprint(null);
        task = taskRepository.save(task);
        return toResponseDto(task);
    }

    private TaskResponseDto toResponseDto(Task task) {
        TaskResponseDto dto = modelMapper.map(task, TaskResponseDto.class);
        if (task.getAssignedUser() != null) {
            dto.setAssignedUser(modelMapper.map(task.getAssignedUser(), ue.poznan.spring_jwt_auth.user.dto.UserDto.class));
        }
        return dto;
    }
} 
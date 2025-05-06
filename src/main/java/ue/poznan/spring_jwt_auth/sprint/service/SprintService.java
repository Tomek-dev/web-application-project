package ue.poznan.spring_jwt_auth.sprint.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ue.poznan.spring_jwt_auth.project.domain.Project;
import ue.poznan.spring_jwt_auth.project.repository.ProjectRepository;
import ue.poznan.spring_jwt_auth.sprint.domain.Sprint;
import ue.poznan.spring_jwt_auth.sprint.dto.SprintRequestDto;
import ue.poznan.spring_jwt_auth.sprint.dto.SprintResponseDto;
import ue.poznan.spring_jwt_auth.sprint.repository.SprintRepository;
import ue.poznan.spring_jwt_auth.user.domain.User;
import ue.poznan.spring_jwt_auth.user.service.UserService;
import ue.poznan.spring_jwt_auth.workplace.domain.UserRoleType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SprintService {
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Transactional
    public SprintResponseDto createSprint(UUID projectId, SprintRequestDto dto) {
        User currentUser = userService.getCurrentUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        boolean isAdminOrManager = project.getWorkplace().getRoles().stream()
                .anyMatch(role -> role.getUser().getId().equals(currentUser.getId()) &&
                        (role.getRoleType() == UserRoleType.ADMIN || role.getRoleType() == UserRoleType.PROJECT_MANAGER));
        if (!isAdminOrManager) {
            throw new AccessDeniedException("Not authorized to create sprint in this project");
        }
        Sprint sprint = Sprint.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .project(project)
                .build();
        sprint = sprintRepository.save(sprint);
        return toResponseDto(sprint);
    }

    @Transactional
    public void deleteSprint(UUID sprintId) {
        User currentUser = userService.getCurrentUser();
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));
        Project project = sprint.getProject();
        boolean isAdminOrManager = project.getWorkplace().getRoles().stream()
                .anyMatch(role -> role.getUser().getId().equals(currentUser.getId()) &&
                        (role.getRoleType() == UserRoleType.ADMIN || role.getRoleType() == UserRoleType.PROJECT_MANAGER));
        if (!isAdminOrManager) {
            throw new AccessDeniedException("Not authorized to delete sprint in this project");
        }
        sprintRepository.delete(sprint);
    }

    @Transactional
    public SprintResponseDto updateSprint(UUID sprintId, SprintRequestDto dto) {
        User currentUser = userService.getCurrentUser();
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));
        Project project = sprint.getProject();
        boolean isAdminOrManager = project.getWorkplace().getRoles().stream()
                .anyMatch(role -> role.getUser().getId().equals(currentUser.getId()) &&
                        (role.getRoleType() == UserRoleType.ADMIN || role.getRoleType() == UserRoleType.PROJECT_MANAGER));
        if (!isAdminOrManager) {
            throw new AccessDeniedException("Not authorized to update sprint in this project");
        }
        sprint.setName(dto.getName());
        sprint.setDescription(dto.getDescription());
        sprint.setStartDate(dto.getStartDate());
        sprint.setEndDate(dto.getEndDate());
        sprint = sprintRepository.save(sprint);
        return toResponseDto(sprint);
    }

    @Transactional(readOnly = true)
    public List<SprintResponseDto> getSprintsByProject(UUID projectId) {
        return sprintRepository.findByProjectId(projectId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private SprintResponseDto toResponseDto(Sprint sprint) {
        return modelMapper.map(sprint, SprintResponseDto.class);
    }
} 
package ue.poznan.spring_jwt_auth.project.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ue.poznan.spring_jwt_auth.project.domain.Project;
import ue.poznan.spring_jwt_auth.project.dto.ProjectRequestDto;
import ue.poznan.spring_jwt_auth.project.dto.ProjectResponseDto;
import ue.poznan.spring_jwt_auth.project.repository.ProjectRepository;
import ue.poznan.spring_jwt_auth.user.domain.User;
import ue.poznan.spring_jwt_auth.user.service.UserService;
import ue.poznan.spring_jwt_auth.workplace.domain.UserRoleType;
import ue.poznan.spring_jwt_auth.workplace.domain.Workplace;
import ue.poznan.spring_jwt_auth.workplace.domain.WorkplaceRole;
import ue.poznan.spring_jwt_auth.workplace.repository.WorkplaceRepository;
import ue.poznan.spring_jwt_auth.workplace.repository.WorkplaceRoleRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final WorkplaceRepository workplaceRepository;
    private final WorkplaceRoleRepository workplaceRoleRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Transactional
    public ProjectResponseDto createProject(UUID workplaceId, ProjectRequestDto dto) {
        User currentUser = userService.getCurrentUser();
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(() -> new IllegalArgumentException("Workplace not found"));
        boolean isAdminOrManager = workplace.getRoles().stream()
                .anyMatch(role -> role.getUser().getId().equals(currentUser.getId()) &&
                        (role.getRoleType() == UserRoleType.ADMIN || role.getRoleType() == UserRoleType.PROJECT_MANAGER));
        if (!isAdminOrManager) {
            throw new AccessDeniedException("Not authorized to create project in this workplace");
        }
        Project project = Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .workplace(workplace)
                .build();
        project = projectRepository.save(project);
        return toResponseDto(project);
    }

    @Transactional
    public void deleteProject(UUID projectId) {
        User currentUser = userService.getCurrentUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Workplace workplace = project.getWorkplace();
        boolean isAdminOrManager = workplace.getRoles().stream()
                .anyMatch(role -> role.getUser().getId().equals(currentUser.getId()) &&
                        (role.getRoleType() == UserRoleType.ADMIN || role.getRoleType() == UserRoleType.PROJECT_MANAGER));
        if (!isAdminOrManager) {
            throw new AccessDeniedException("Not authorized to delete project in this workplace");
        }
        projectRepository.delete(project);
    }

    @Transactional
    public ProjectResponseDto updateProject(UUID projectId, ProjectRequestDto dto) {
        User currentUser = userService.getCurrentUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Workplace workplace = project.getWorkplace();
        boolean isAdminOrManager = workplace.getRoles().stream()
                .anyMatch(role -> role.getUser().getId().equals(currentUser.getId()) &&
                        (role.getRoleType() == UserRoleType.ADMIN || role.getRoleType() == UserRoleType.PROJECT_MANAGER));
        if (!isAdminOrManager) {
            throw new AccessDeniedException("Not authorized to update project in this workplace");
        }
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project = projectRepository.save(project);
        return toResponseDto(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getProjectsByCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return projectRepository.findByWorkplaceRolesUserId(currentUser.getId())
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getProjectsByWorkplace(UUID workplaceId) {
        return projectRepository.findByWorkplaceId(workplaceId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        return toResponseDto(project);
    }

    private ProjectResponseDto toResponseDto(Project project) {
        return modelMapper.map(project, ProjectResponseDto.class);
    }
} 
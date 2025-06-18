package ue.poznan.spring_jwt_auth.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ue.poznan.spring_jwt_auth.project.dto.ProjectRequestDto;
import ue.poznan.spring_jwt_auth.project.dto.ProjectResponseDto;
import ue.poznan.spring_jwt_auth.project.service.ProjectService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("workplace/{workplaceId}")
    public ProjectResponseDto createProject(@PathVariable UUID workplaceId, @RequestBody ProjectRequestDto dto) {
        return projectService.createProject(workplaceId, dto);
    }

    @GetMapping("{id}")
    public ProjectResponseDto getProjectById(@PathVariable UUID id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("{id}")
    public ProjectResponseDto updateProject(@PathVariable UUID id, @RequestBody ProjectRequestDto dto) {
        return projectService.updateProject(id, dto);
    }

    @DeleteMapping("{id}")
    public void deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
    }

    @GetMapping("my")
    public List<ProjectResponseDto> getProjectsByCurrentUser() {
        return projectService.getProjectsByCurrentUser();
    }

    @GetMapping("workplace/{workplaceId}")
    public List<ProjectResponseDto> getProjectsByWorkplace(@PathVariable UUID workplaceId) {
        return projectService.getProjectsByWorkplace(workplaceId);
    }
} 
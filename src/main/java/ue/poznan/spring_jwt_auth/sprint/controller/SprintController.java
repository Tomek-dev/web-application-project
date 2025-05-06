package ue.poznan.spring_jwt_auth.sprint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ue.poznan.spring_jwt_auth.sprint.dto.SprintRequestDto;
import ue.poznan.spring_jwt_auth.sprint.dto.SprintResponseDto;
import ue.poznan.spring_jwt_auth.sprint.service.SprintService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/sprints")
@RequiredArgsConstructor
public class SprintController {
    private final SprintService sprintService;

    @PostMapping("project/{projectId}")
    public SprintResponseDto createSprint(@PathVariable UUID projectId, @RequestBody SprintRequestDto dto) {
        return sprintService.createSprint(projectId, dto);
    }

    @PutMapping("{id}")
    public SprintResponseDto updateSprint(@PathVariable UUID id, @RequestBody SprintRequestDto dto) {
        return sprintService.updateSprint(id, dto);
    }

    @DeleteMapping("{id}")
    public void deleteSprint(@PathVariable UUID id) {
        sprintService.deleteSprint(id);
    }

    @GetMapping("project/{projectId}")
    public List<SprintResponseDto> getSprintsByProject(@PathVariable UUID projectId) {
        return sprintService.getSprintsByProject(projectId);
    }
} 
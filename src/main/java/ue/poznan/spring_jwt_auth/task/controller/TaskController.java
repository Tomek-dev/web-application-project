package ue.poznan.spring_jwt_auth.task.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ue.poznan.spring_jwt_auth.task.dto.TaskRequestDto;
import ue.poznan.spring_jwt_auth.task.dto.TaskResponseDto;
import ue.poznan.spring_jwt_auth.task.service.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("project/{projectId}")
    public TaskResponseDto createTask(@PathVariable UUID projectId, @RequestBody TaskRequestDto dto) {
        return taskService.createTask(projectId, dto);
    }

    @PutMapping("{id}")
    public TaskResponseDto updateTask(@PathVariable UUID id, @RequestBody TaskRequestDto dto) {
        return taskService.updateTask(id, dto);
    }

    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
    }

    @GetMapping("project/{projectId}")
    public List<TaskResponseDto> getTasksByProject(@PathVariable UUID projectId) {
        return taskService.getTasksByProject(projectId);
    }

    @GetMapping("my")
    public List<TaskResponseDto> getTasksByCurrentUser() {
        return taskService.getTasksByCurrentUser();
    }

    @PatchMapping("{taskId}/assign-user/{userId}")
    public TaskResponseDto assignUser(@PathVariable UUID taskId, @PathVariable UUID userId) {
        return taskService.assignUser(taskId, userId);
    }

    @PatchMapping("{taskId}/assign-sprint/{sprintId}")
    public TaskResponseDto assignSprint(@PathVariable UUID taskId, @PathVariable UUID sprintId) {
        return taskService.assignSprint(taskId, sprintId);
    }

    @PatchMapping("{taskId}/deassign-user")
    public TaskResponseDto deassignUser(@PathVariable UUID taskId) {
        return taskService.deassignUser(taskId);
    }

    @PatchMapping("{taskId}/deassign-sprint")
    public TaskResponseDto deassignSprint(@PathVariable UUID taskId) {
        return taskService.deassignSprint(taskId);
    }
} 
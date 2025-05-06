package ue.poznan.spring_jwt_auth.workplace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ue.poznan.spring_jwt_auth.workplace.dto.WorkplaceRequestDto;
import ue.poznan.spring_jwt_auth.workplace.dto.WorkplaceResponseDto;
import ue.poznan.spring_jwt_auth.workplace.service.WorkplaceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/workplaces")
@RequiredArgsConstructor
public class WorkplaceController {
    private final WorkplaceService workplaceService;

    @PostMapping
    public WorkplaceResponseDto addWorkplace(@RequestBody WorkplaceRequestDto dto) {
        return workplaceService.addWorkplace(dto);
    }

    @DeleteMapping("{id}")
    public void deleteWorkplace(@PathVariable UUID id) {
        workplaceService.deleteWorkplace(id);
    }

    @PutMapping("{id}")
    public WorkplaceResponseDto updateWorkplace(@PathVariable UUID id, @RequestBody WorkplaceRequestDto dto) {
        return workplaceService.updateWorkplace(id, dto);
    }

    @GetMapping("my")
    public List<WorkplaceResponseDto> getMyWorkplaces() {
        return workplaceService.getMyWorkplaces();
    }
} 
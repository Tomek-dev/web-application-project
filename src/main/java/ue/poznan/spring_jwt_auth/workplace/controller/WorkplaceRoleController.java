package ue.poznan.spring_jwt_auth.workplace.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ue.poznan.spring_jwt_auth.workplace.domain.WorkplaceRole;
import ue.poznan.spring_jwt_auth.workplace.domain.UserRoleType;
import ue.poznan.spring_jwt_auth.workplace.repository.WorkplaceRoleRepository;
import ue.poznan.spring_jwt_auth.user.UserRepository;
import ue.poznan.spring_jwt_auth.workplace.domain.Workplace;
import ue.poznan.spring_jwt_auth.workplace.repository.WorkplaceRepository;
import ue.poznan.spring_jwt_auth.user.domain.User;
import ue.poznan.spring_jwt_auth.workplace.dto.WorkplaceRoleDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/workplaces/{workplaceId}/roles")
@RequiredArgsConstructor
public class WorkplaceRoleController {
    private final WorkplaceRoleRepository workplaceRoleRepository;
    private final UserRepository userRepository;
    private final WorkplaceRepository workplaceRepository;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<WorkplaceRoleDto> getRoles(@PathVariable UUID workplaceId) {
        return workplaceRoleRepository.findByWorkplaceId(workplaceId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public WorkplaceRoleDto addRole(@PathVariable UUID workplaceId, @RequestParam UUID userId, @RequestParam UserRoleType roleType) {
        User user = userRepository.findById(userId).orElseThrow();
        Workplace workplace = workplaceRepository.findById(workplaceId).orElseThrow();
        WorkplaceRole role = WorkplaceRole.builder()
                .user(user)
                .workplace(workplace)
                .roleType(roleType)
                .build();
        return toDto(workplaceRoleRepository.save(role));
    }

    @PutMapping("{roleId}")
    public WorkplaceRoleDto updateRole(@PathVariable UUID roleId, @RequestParam UserRoleType roleType) {
        WorkplaceRole role = workplaceRoleRepository.findById(roleId).orElseThrow();
        role.setRoleType(roleType);
        return toDto(workplaceRoleRepository.save(role));
    }

    @DeleteMapping("{roleId}")
    public void deleteRole(@PathVariable UUID roleId) {
        workplaceRoleRepository.deleteById(roleId);
    }

    private WorkplaceRoleDto toDto(WorkplaceRole role) {
        WorkplaceRoleDto dto = modelMapper.map(role, WorkplaceRoleDto.class);
        dto.setUserId(role.getUser().getId());
        dto.setUsername(role.getUser().getUsername());
        dto.setEmail(role.getUser().getEmail());
        return dto;
    }
} 
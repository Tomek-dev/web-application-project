package ue.poznan.spring_jwt_auth.workplace.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ue.poznan.spring_jwt_auth.user.domain.User;
import ue.poznan.spring_jwt_auth.user.service.UserService;
import ue.poznan.spring_jwt_auth.workplace.domain.*;
import ue.poznan.spring_jwt_auth.workplace.dto.WorkplaceRequestDto;
import ue.poznan.spring_jwt_auth.workplace.dto.WorkplaceResponseDto;
import ue.poznan.spring_jwt_auth.workplace.repository.WorkplaceRepository;
import ue.poznan.spring_jwt_auth.workplace.repository.WorkplaceRoleRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkplaceService {
    private final WorkplaceRepository workplaceRepository;
    private final WorkplaceRoleRepository workplaceRoleRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Transactional
    public WorkplaceResponseDto addWorkplace(WorkplaceRequestDto dto) {
        User currentUser = userService.getCurrentUser();
        Workplace workplace = Workplace.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .user(currentUser)
                .build();
        workplace = workplaceRepository.save(workplace);
        WorkplaceRole role = WorkplaceRole.builder()
                .workplace(workplace)
                .user(currentUser)
                .roleType(UserRoleType.ADMIN)
                .build();
        workplaceRoleRepository.save(role);
        return toResponseDto(workplace);
    }

    @Transactional
    public void deleteWorkplace(UUID workplaceId) {
        User currentUser = userService.getCurrentUser();
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(() -> new IllegalArgumentException("Workplace not found"));
        if (!workplace.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the owner can delete this workplace");
        }
        workplaceRepository.delete(workplace);
    }

    @Transactional
    public WorkplaceResponseDto updateWorkplace(UUID workplaceId, WorkplaceRequestDto dto) {
        User currentUser = userService.getCurrentUser();
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(() -> new IllegalArgumentException("Workplace not found"));
        boolean isOwner = workplace.getUser().getId().equals(currentUser.getId());
        boolean isAdminOrManager = workplace.getRoles().stream()
                .anyMatch(role -> role.getUser().getId().equals(currentUser.getId()) &&
                        (role.getRoleType() == UserRoleType.ADMIN || role.getRoleType() == UserRoleType.PROJECT_MANAGER));
        if (!isOwner && !isAdminOrManager) {
            throw new AccessDeniedException("Not authorized to update this workplace");
        }
        workplace.setName(dto.getName());
        workplace.setDescription(dto.getDescription());
        workplace = workplaceRepository.save(workplace);
        return toResponseDto(workplace);
    }

    @Transactional(readOnly = true)
    public List<WorkplaceResponseDto> getMyWorkplaces() {
        User currentUser = userService.getCurrentUser();
        return workplaceRepository.findByRolesUserId(currentUser.getId())
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private WorkplaceResponseDto toResponseDto(Workplace workplace) {
        return modelMapper.map(workplace, WorkplaceResponseDto.class);
    }
} 
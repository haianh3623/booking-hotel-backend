package group.assignment.booking_hotel_backend.mapper;

import group.assignment.booking_hotel_backend.dto.RoleDto;
import group.assignment.booking_hotel_backend.models.Role;

public class RoleMapper {
    public static RoleDto mapToRoleDto(Role role, RoleDto dto) {
        dto.setRoleId(role.getRoleId());
        dto.setName(role.getName());
        return dto;
    }

    public static Role mapToRole(RoleDto dto, Role role) {
        role.setName(dto.getName());
        return role;
    }
}

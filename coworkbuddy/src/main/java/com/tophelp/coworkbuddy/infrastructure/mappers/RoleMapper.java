package com.tophelp.coworkbuddy.infrastructure.mappers;

import com.tophelp.coworkbuddy.domain.Role;
import com.tophelp.coworkbuddy.infrastructure.dto.input.RoleInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto roleToRoleDto(Role role);
    RoleInputDto roleToRoleInputDto(Role role);

    @Mapping(target = "users", ignore = true)
    Role roleInputDtoToRole(RoleInputDto roleInputDto);
}

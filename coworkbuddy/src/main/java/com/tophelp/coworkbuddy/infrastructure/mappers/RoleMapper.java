package com.tophelp.coworkbuddy.infrastructure.mappers;

import com.tophelp.coworkbuddy.domain.Role;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  RoleDto roleToRoleDto(Role role);

}

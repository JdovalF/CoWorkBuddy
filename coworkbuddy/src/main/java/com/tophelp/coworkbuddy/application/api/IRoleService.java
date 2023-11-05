package com.tophelp.coworkbuddy.application.api;

import com.tophelp.coworkbuddy.infrastructure.dto.output.RoleDto;

import java.util.List;

public interface IRoleService {
  List<RoleDto> retrieveAllRoles();

  RoleDto retrieveRoleById(String id);
}

package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.application.api.IRoleService;
import com.tophelp.coworkbuddy.application.utils.CrudUtils;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoleDto;
import com.tophelp.coworkbuddy.infrastructure.exceptions.DatabaseNotFoundException;
import com.tophelp.coworkbuddy.infrastructure.mappers.RoleMapper;
import com.tophelp.coworkbuddy.infrastructure.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleDto> retrieveAllRoles() {
        log.info("RoleService - retrieveAllRoles");
        return roleRepository.findAll().stream().map(roleMapper::roleToRoleDto).toList();
    }

    @Override
    public RoleDto retrieveRoleById(String id) {
        log.info("RoleService - retrieveRoleById - Id: {}", id);
        CrudUtils.throwExceptionWhenNull(id, "Id", true);
        return roleMapper.roleToRoleDto(roleRepository.findById(CrudUtils.uuidFromString(id))
                .orElseThrow(() -> new DatabaseNotFoundException(String.format("Id: %s not found in Database", id))));
    }
}

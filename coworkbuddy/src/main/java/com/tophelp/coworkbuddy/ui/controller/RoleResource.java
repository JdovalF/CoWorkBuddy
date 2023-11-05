package com.tophelp.coworkbuddy.ui.controller;

import com.tophelp.coworkbuddy.application.api.IRoleService;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleResource {

  private final IRoleService roleService;

  @GetMapping
  @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
  public ResponseEntity<List<RoleDto>> retrieveAllRoles() {
    log.info("RoleController - retrieveAllRoles");
    return ResponseEntity.ok(roleService.retrieveAllRoles());
  }

  @GetMapping("/{id}")
  public ResponseEntity<RoleDto> retrieveRoleById(@PathVariable String id) {
    log.info("RoleController - retrieveRoleById - Id: {}", id);
    return ResponseEntity.ok(roleService.retrieveRoleById(id));
  }
}

package com.tophelp.coworkbuddy.infrastructure.dto.output;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private Set<RoleDto> roles;
}

package com.tophelp.coworkbuddy.infrastructure.dto.input;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserInputDto {
    private String username;
    private String email;
    private String password;
    private Set<RoleInputDto> roles;
}

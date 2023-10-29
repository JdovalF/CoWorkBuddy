package com.tophelp.coworkbuddy.infrastructure.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class RoleDto {
    private UUID id;
    private String name;
}

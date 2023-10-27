package com.tophelp.coworkbuddy.infrastructure.dto.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleInputDto {
    private String id;
    private String name;
}

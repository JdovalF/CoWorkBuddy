package com.tophelp.coworkbuddy.infrastructure.dto.input;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class RoleInputDto {
  private String id;
  private String name;
}

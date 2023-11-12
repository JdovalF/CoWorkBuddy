package com.tophelp.coworkbuddy.infrastructure.dto.output;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UserDto {
  private UUID id;
  private String username;
  private String email;
  private Set<RoleDto> roles;
  private Set<RoomDto> rooms;
  private LocalDateTime creationDate;
}

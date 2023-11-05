package com.tophelp.coworkbuddy.infrastructure.dto.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserInputDto {
  private String id;
  private String username;
  private String email;
  private String password;
  private Set<String> roles;
  private Set<String> rooms;
}

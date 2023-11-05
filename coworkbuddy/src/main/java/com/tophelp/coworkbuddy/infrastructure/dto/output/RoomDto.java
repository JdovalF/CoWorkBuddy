package com.tophelp.coworkbuddy.infrastructure.dto.output;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class RoomDto {
  private UUID id;
  private String name;
}

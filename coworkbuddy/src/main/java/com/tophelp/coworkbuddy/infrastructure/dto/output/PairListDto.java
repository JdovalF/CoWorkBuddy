package com.tophelp.coworkbuddy.infrastructure.dto.output;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class PairListDto {
  private UUID room;
  private boolean saved;
  private Set<TaskDto> tasks;
}

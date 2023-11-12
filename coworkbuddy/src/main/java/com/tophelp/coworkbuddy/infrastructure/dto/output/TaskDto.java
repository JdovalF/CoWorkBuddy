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
public class TaskDto {
  private UUID id;
  private String name;
  private Boolean active;
  private Set<WorkerDto> workers;
  private LocalDateTime creationDate;
}

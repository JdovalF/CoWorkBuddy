package com.tophelp.coworkbuddy.infrastructure.dto.input;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class WorkerInputDto {
  private String id;
  private String name;
  private boolean active;
  private String roomId;
  private String taskId;
}

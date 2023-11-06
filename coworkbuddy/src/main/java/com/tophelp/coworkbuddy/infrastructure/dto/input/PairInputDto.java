package com.tophelp.coworkbuddy.infrastructure.dto.input;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class PairInputDto {
  private String id;
  private String pairId;
  private String taskId;
  private List<String> workerIds;
}

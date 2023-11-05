package com.tophelp.coworkbuddy.infrastructure.mappers;

import com.tophelp.coworkbuddy.domain.Worker;
import com.tophelp.coworkbuddy.infrastructure.dto.input.WorkerInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.WorkerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkerMapper {

  WorkerDto workerToWorkerDto(Worker worker);

  List<WorkerDto> workersToListWorkerDto(List<Worker> worker);

  @Mapping(target = "room", ignore = true)
  @Mapping(target = "task", ignore = true)
  @Mapping(target = "id", source = "id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "name", source = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "active", source = "active", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateWorkerFromWorkerInputDto(WorkerInputDto workerInputDto, @MappingTarget Worker worker);
}

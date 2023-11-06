package com.tophelp.coworkbuddy.infrastructure.mappers;

import com.tophelp.coworkbuddy.domain.Task;
import com.tophelp.coworkbuddy.infrastructure.dto.input.TaskInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TaskMapper {
  TaskDto taskToTaskDto(Task task);

  List<TaskDto> tasksToListTaskDto(List<Task> tasks);
  Set<TaskDto> tasksToSetTaskDto(Set<Task> tasks);

  @Mapping(target = "room", ignore = true)
  @Mapping(target = "workers", ignore = true)
  @Mapping(target = "id", source = "id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "name", source = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "active", source = "active", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateTaskFromTaskInputDto(TaskInputDto taskInputDto, @MappingTarget Task task);
}

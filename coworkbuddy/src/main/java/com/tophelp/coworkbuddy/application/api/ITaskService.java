package com.tophelp.coworkbuddy.application.api;

import com.tophelp.coworkbuddy.infrastructure.dto.input.TaskInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.TaskDto;

public interface ITaskService {

  TaskDto createTask(TaskInputDto taskInputDto);

  TaskDto updateTask(TaskInputDto taskInputDto);

  void deleteTaskById(String id);

}

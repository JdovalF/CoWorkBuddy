package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.application.api.ITaskService;
import com.tophelp.coworkbuddy.application.utils.CrudUtils;
import com.tophelp.coworkbuddy.domain.Room;
import com.tophelp.coworkbuddy.domain.Task;
import com.tophelp.coworkbuddy.infrastructure.dto.input.TaskInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.TaskDto;
import com.tophelp.coworkbuddy.infrastructure.exceptions.DatabaseNotFoundException;
import com.tophelp.coworkbuddy.infrastructure.mappers.TaskMapper;
import com.tophelp.coworkbuddy.infrastructure.repository.RoomRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

  private final TaskRepository taskRepository;
  private final RoomRepository roomRepository;
  private final TaskMapper taskMapper;

  @Override
  public TaskDto createTask(TaskInputDto taskInputDto) {
    log.info("TaskService - createTask");
    CrudUtils.throwExceptionWhenNull(taskInputDto.getId(), "Id", false);
    CrudUtils.throwExceptionWhenNull(taskInputDto.getName(), "Name", true);
    CrudUtils.throwExceptionWhenNull(taskInputDto.getRoomId(), "Room Id", true);
    return taskMapper.taskToTaskDto(taskRepository.save(Task.builder().id(UUID.randomUUID())
            .name(taskInputDto.getName()).active(taskInputDto.isActive())
            .room(retrieveRoomById(taskInputDto.getRoomId()))
        .build()));
  }

  @Override
  public TaskDto updateTask(TaskInputDto taskInputDto) {
    log.info("TaskService - updateTask");
    CrudUtils.throwExceptionWhenNull(taskInputDto.getId(), "Id", true);
    var oldTask = retrieveTaskById(taskInputDto.getId());
    taskMapper.updateTaskFromTaskInputDto(taskInputDto, oldTask);
    if (nonNull(taskInputDto.getRoomId()) && !taskInputDto.getRoomId().equals(oldTask.getRoom().getId())) {
      oldTask.setRoom(retrieveRoomById(taskInputDto.getRoomId()));
    }
    return taskMapper.taskToTaskDto(taskRepository.save(oldTask));
  }

  @Override
  public void deleteTaskById(String id) {
    log.info("TaskService - deleteTaskById - Id {}", id);
    taskRepository.delete(retrieveTaskById(id));
  }

  private Room retrieveRoomById(String id) {
    return roomRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Room Id: %s not found in Database", id)));
  }

  private Task retrieveTaskById(String id) {
    return taskRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Task Id: %s not found in Database", id)));
  }
}

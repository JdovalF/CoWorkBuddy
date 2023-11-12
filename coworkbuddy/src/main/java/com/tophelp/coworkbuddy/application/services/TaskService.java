package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.application.api.ITaskService;
import com.tophelp.coworkbuddy.application.utils.CrudUtils;
import com.tophelp.coworkbuddy.domain.Room;
import com.tophelp.coworkbuddy.domain.Task;
import com.tophelp.coworkbuddy.domain.Worker;
import com.tophelp.coworkbuddy.infrastructure.dto.input.TaskInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.TaskDto;
import com.tophelp.coworkbuddy.infrastructure.exceptions.DatabaseNotFoundException;
import com.tophelp.coworkbuddy.infrastructure.mappers.TaskMapper;
import com.tophelp.coworkbuddy.infrastructure.repository.RoomRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.TaskRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

  private final TaskRepository taskRepository;
  private final RoomRepository roomRepository;
  private final WorkerRepository workerRepository;
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
            .creationDate(LocalDateTime.now())
        .build()));
  }

  @Override
  public TaskDto updateTask(TaskInputDto taskInputDto) {
    log.info("TaskService - updateTask");
    CrudUtils.throwExceptionWhenNull(taskInputDto.getId(), "Id", true);
    var oldTask = retrieveTaskById(taskInputDto.getId());
    taskMapper.updateTaskFromTaskInputDto(taskInputDto, oldTask);
    if (nonNull(taskInputDto.getRoomId())) {
      oldTask.setRoom(retrieveRoomById(taskInputDto.getRoomId()));
    }
    if (nonNull(taskInputDto.getWorkers()) && !taskInputDto.getWorkers().isEmpty()) {
      oldTask.setWorkers(taskInputDto.getWorkers().stream().map(this::retrieveWorkerById).collect(Collectors.toSet()));
    }
    return taskMapper.taskToTaskDto(taskRepository.save(oldTask));
  }

  @Override
  public void deleteTaskById(String id) {
    log.info("TaskService - deleteTaskById - Id {}", id);
    taskRepository.delete(retrieveTaskById(id));
  }

  private Worker retrieveWorkerById(String id) {
    return workerRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Worker Id: %s not found in Database", id)));
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

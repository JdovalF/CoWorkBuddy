package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.application.api.IWorkerService;
import com.tophelp.coworkbuddy.application.utils.CrudUtils;
import com.tophelp.coworkbuddy.domain.Pair;
import com.tophelp.coworkbuddy.domain.Room;
import com.tophelp.coworkbuddy.domain.Task;
import com.tophelp.coworkbuddy.domain.Worker;
import com.tophelp.coworkbuddy.infrastructure.dto.input.WorkerInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.WorkerDto;
import com.tophelp.coworkbuddy.infrastructure.exceptions.DatabaseNotFoundException;
import com.tophelp.coworkbuddy.infrastructure.mappers.WorkerMapper;
import com.tophelp.coworkbuddy.infrastructure.repository.PairRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.RoomRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.TaskRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerService implements IWorkerService {

  private final WorkerRepository workerRepository;
  private final RoomRepository roomRepository;
  private final TaskRepository taskRepository;
  private final PairRepository pairRepository;
  private final WorkerMapper workerMapper;

  @Override
  public WorkerDto createWorker(WorkerInputDto workerInputDto) {
    log.info("WorkerService - createWorker");
    CrudUtils.throwExceptionWhenNull(workerInputDto.getId(), "Id", false);
    CrudUtils.throwExceptionWhenNull(workerInputDto.getName(), "Name", true);
    CrudUtils.throwExceptionWhenNull(workerInputDto.getRoomId(), "Room Id", true);
    return workerMapper.workerToWorkerDto(workerRepository.save(Worker.builder().id(UUID.randomUUID())
        .name(workerInputDto.getName()).active(workerInputDto.isActive())
        .room(retrieveRoomById(workerInputDto.getRoomId()))
        .task(nonNull(workerInputDto.getTaskId()) ? retrieveTaskById(workerInputDto.getTaskId()) : null)
        .build()));
  }

  @Override
  public WorkerDto updateWorker(WorkerInputDto workerInputDto) {
    log.info("WorkerService - updateWorker");
    CrudUtils.throwExceptionWhenNull(workerInputDto.getId(), "Id", true);
    var oldWorker = retrieveWorkerById(workerInputDto.getId());
    workerMapper.updateWorkerFromWorkerInputDto(workerInputDto, oldWorker);
    if (nonNull(workerInputDto.getRoomId())) {
      oldWorker.setRoom(retrieveRoomById(workerInputDto.getRoomId()));
    }
    if (nonNull(workerInputDto.getTaskId())) {
      oldWorker.setTask(retrieveTaskById(workerInputDto.getTaskId()));
    }
    if (nonNull(workerInputDto.getPairs()) && !workerInputDto.getPairs().isEmpty()) {
      oldWorker.setPairs(workerInputDto.getPairs().stream().map(this::retrievePairById).toList());
    }
    return workerMapper.workerToWorkerDto(workerRepository.save(oldWorker));
  }

  @Override
  public void deleteWorkerById(String id) {
    log.info("WorkerService - deleteWorkerById");
    workerRepository.delete(retrieveWorkerById(id));
  }

  private Task retrieveTaskById(String id) {
    return taskRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Task Id: %s not found in Database", id)));
  }

  private Room retrieveRoomById(String id) {
    return roomRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Room Id: %s not found in Database", id)));
  }

  private Worker retrieveWorkerById(String id) {
    return workerRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Worker Id: %s not found in Database", id)));
  }

  private Pair retrievePairById(String id) {
    return pairRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Pair Id: %s not found in Database", id)));
  }

}

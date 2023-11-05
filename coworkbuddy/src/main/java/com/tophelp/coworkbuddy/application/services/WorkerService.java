package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.application.api.IWorkerService;
import com.tophelp.coworkbuddy.application.utils.CrudUtils;
import com.tophelp.coworkbuddy.domain.Room;
import com.tophelp.coworkbuddy.domain.Worker;
import com.tophelp.coworkbuddy.infrastructure.dto.input.WorkerInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.WorkerDto;
import com.tophelp.coworkbuddy.infrastructure.exceptions.DatabaseNotFoundException;
import com.tophelp.coworkbuddy.infrastructure.mappers.WorkerMapper;
import com.tophelp.coworkbuddy.infrastructure.repository.RoomRepository;
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
        .build()));
  }

  @Override
  public WorkerDto updateWorker(WorkerInputDto workerInputDto) {
    log.info("WorkerService - updateWorker");
    CrudUtils.throwExceptionWhenNull(workerInputDto.getId(), "Id", true);
    var oldWorker = retrieveWorkerById(workerInputDto.getId());
    workerMapper.updateWorkerFromWorkerInputDto(workerInputDto, oldWorker);
    if (nonNull(workerInputDto.getRoomId()) && !workerInputDto.getRoomId().equals(oldWorker.getRoom().getId())) {
      oldWorker.setRoom(retrieveRoomById(workerInputDto.getRoomId()));
    }
    return workerMapper.workerToWorkerDto(workerRepository.save(oldWorker));
  }

  @Override
  public void deleteWorkerById(String id) {
    log.info("WorkerService - deleteWorkerById");
    workerRepository.delete(retrieveWorkerById(id));
  }

  private Room retrieveRoomById(String id) {
    return roomRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Room Id: %s not found in Database", id)));
  }

  private Worker retrieveWorkerById(String id) {
    return workerRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Worker Id: %s not found in Database", id)));
  }
}

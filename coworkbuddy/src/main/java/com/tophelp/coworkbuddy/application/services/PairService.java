package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.application.api.IPairService;
import com.tophelp.coworkbuddy.application.utils.CrudUtils;
import com.tophelp.coworkbuddy.domain.Pair;
import com.tophelp.coworkbuddy.domain.Room;
import com.tophelp.coworkbuddy.domain.Task;
import com.tophelp.coworkbuddy.domain.Worker;
import com.tophelp.coworkbuddy.infrastructure.dto.input.PairListInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.PairListDto;
import com.tophelp.coworkbuddy.infrastructure.exceptions.DatabaseNotFoundException;
import com.tophelp.coworkbuddy.infrastructure.mappers.TaskMapper;
import com.tophelp.coworkbuddy.infrastructure.repository.PairRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.RoomRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.TaskRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class PairService implements IPairService {

  private final PairRepository pairRepository;
  private final RoomRepository roomRepository;
  private final TaskRepository taskRepository;
  private final WorkerRepository workerRepository;
  private final TaskMapper taskMapper;

  @Override
  public PairListDto recommendPairs() {
    return null;
  }

  @Override
  public PairListDto createOrUpdatePairs(PairListInputDto pairListInputDto) {
    log.info("PairService - createOrUpdatePairs");
    List<Pair> tmpPairs = new ArrayList<>();
    pairListInputDto.getPairs().stream().forEach(pair -> {
      pair.getWorkerIds().stream().forEach(workerId -> {
        var id = StringUtils.isBlank(pair.getId()) ? UUID.randomUUID() : CrudUtils.uuidFromString(pair.getId());
        var pairId = StringUtils.isBlank(pair.getPairId()) ? UUID.randomUUID() : CrudUtils.uuidFromString(pair.getPairId());
        var worker = retrieveWorkerById(workerId);
        worker.setTask(retrieveTaskById(pair.getTaskId()));
        workerRepository.save(worker);
        var lastPairingDate = LocalDateTime.now();
        tmpPairs.add(Pair.builder().id(id).pairId(pairId).worker(worker).lastPairingDate(lastPairingDate).build());
      });
    });
    pairRepository.saveAll(tmpPairs);
    var tasks = retrieveRoomById(pairListInputDto.getRoom()).getTasks();
    return PairListDto.builder().room(CrudUtils.uuidFromString(pairListInputDto.getRoom()))
        .tasks(taskMapper.tasksToSetTaskDto(tasks))
        .build();
  }

  @Override
  public void deleteAllPairs() {
    log.info("PairService - deleteAllPairs");
    pairRepository.deleteAll();
  }

  private Room retrieveRoomById(String id) {
    return roomRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Room Id: %s not found in Database", id)));
  }

  private Task retrieveTaskById(String id) {
    return taskRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Task Id: %s not found in Database", id)));
  }

  private Worker retrieveWorkerById(String id) {
    return workerRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Worker Id: %s not found in Database", id)));
  }

  private Pair retrievePairById(String id) {
    return pairRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
        () -> new DatabaseNotFoundException(format("Pair Id: %s not found in Database", id)));
  }

  private List<Pair> retrievePairsByPairId(String id) {
    return pairRepository.findAllByPairId(CrudUtils.uuidFromString(id));
  }

  private List<Pair> retrieveAllPairsByWorkerRoomId(String id) {
    return pairRepository.findAllPairsByWorkerRoomId(CrudUtils.uuidFromString(id));
  }

}

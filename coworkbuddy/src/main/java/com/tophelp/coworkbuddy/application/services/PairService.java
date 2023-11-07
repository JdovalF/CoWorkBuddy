package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.application.api.IPairService;
import com.tophelp.coworkbuddy.application.utils.CrudUtils;
import com.tophelp.coworkbuddy.application.utils.PairGenerator;
import com.tophelp.coworkbuddy.domain.Pair;
import com.tophelp.coworkbuddy.domain.Room;
import com.tophelp.coworkbuddy.domain.Task;
import com.tophelp.coworkbuddy.domain.Worker;
import com.tophelp.coworkbuddy.infrastructure.dto.input.PairInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.input.PairListInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.PairListDto;
import com.tophelp.coworkbuddy.infrastructure.exceptions.DatabaseNotFoundException;
import com.tophelp.coworkbuddy.infrastructure.mappers.TaskMapper;
import com.tophelp.coworkbuddy.infrastructure.repository.PairRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.RoomRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.TaskRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.WorkerRepository;
import com.tophelp.coworkbuddy.shared.exceptions.CoworkBuddyTechnicalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class PairService implements IPairService {

  private final PairGenerator pairGenerator;
  private final PairRepository pairRepository;
  private final RoomRepository roomRepository;
  private final TaskRepository taskRepository;
  private final WorkerRepository workerRepository;
  private final TaskMapper taskMapper;

  @Override
  public PairListDto recommendPairs(String roomId) {
    var room = retrieveRoomById(roomId);
    var partitionedWorkers = room.getWorkers().stream().collect(Collectors.partitioningBy(Worker::isActive));
    var partitionedTasks = room.getTasks().stream().collect(Collectors.partitioningBy(Task::isActive));
    checkSpaceForWorkers(partitionedTasks, partitionedWorkers);

    var pairInputListDto = PairListInputDto.builder().room(roomId).pairs(new ArrayList<>()).build();
    var activeWorkers = partitionedWorkers.get(true);
    var removedWorkers = new ArrayList<Worker>();
    var oddWorker = getOddWorker(activeWorkers);
    for (int i = activeWorkers.size() - 1; i >= 0; i--) {
      if(removedWorkers.contains(activeWorkers.get(i))) {
       break;
      }
      var possiblePairs = pairGenerator.generatePossiblePairsForWorker(activeWorkers, removedWorkers);
      var weightings = new HashMap<Integer, Long>();
      possiblePairs.forEach(pair -> weightings.put(possiblePairs.indexOf(pair), getWeighting(pair)));
      var workerToPairWith = ofNullable(getBestPairForWorker(activeWorkers.get(i), possiblePairs, weightings))
          .orElseThrow(() -> new CoworkBuddyTechnicalException("Impossible to get The suitable pair for the current worker"));
      log.info("pair created: {} and {}", activeWorkers.get(i).getName(), workerToPairWith.getName());

      removedWorkers.add(activeWorkers.get(i));
      removedWorkers.add(workerToPairWith);

      var pairInputDto = PairInputDto.builder()
          .workerIds(List.of(String.valueOf(activeWorkers.get(i).getId()), String.valueOf(workerToPairWith.getId())))
          .build();
      pairInputListDto.getPairs().add(pairInputDto);
    }

    oddWorker.ifPresent(ew -> pairInputListDto.getPairs()
        .add(PairInputDto.builder().workerIds(List.of(String.valueOf(ew.getId()))).build()));

    var activeTaskIds = partitionedTasks.get(true).stream().map(Task::getId).toList();
    for (int i = 0; i < activeTaskIds.size(); i++) {
      if (nonNull(pairInputListDto.getPairs().get(i))) {
        pairInputListDto.getPairs().get(i).setTaskId(String.valueOf(activeTaskIds.get(i)));
      }
    }

    if (!partitionedWorkers.get(false).isEmpty()) {
      var inactiveTaskId = partitionedTasks.get(false).stream().findFirst().map(Task::getId).map(UUID::toString)
          .orElseThrow(() -> new CoworkBuddyTechnicalException("Not enough inactive Tasks for the current inactive Workers."));
      var inactiveWorkerIds = partitionedWorkers.get(false).stream().map(Worker::getId).map(UUID::toString).toList();
      pairInputListDto.getPairs().add(PairInputDto.builder().taskId(inactiveTaskId).workerIds(inactiveWorkerIds).build());
    }

    return createOrUpdatePairs(pairInputListDto);
  }

  @Override
  public PairListDto createOrUpdatePairs(PairListInputDto pairListInputDto) {
    log.info("PairService - createOrUpdatePairs");
    List<Pair> tmpPairs = new ArrayList<>();
    pairListInputDto.getPairs().forEach(pair -> {
      var pairId = StringUtils.isBlank(pair.getPairId()) ? UUID.randomUUID() : CrudUtils.uuidFromString(pair.getPairId());
      var lastPairingDate = LocalDateTime.now();
      pair.getWorkerIds().forEach(workerId -> {
        var id = StringUtils.isBlank(pair.getId()) ? UUID.randomUUID() : CrudUtils.uuidFromString(pair.getId());
        var worker = retrieveWorkerById(workerId);
        var task = retrieveTaskById(pair.getTaskId());
        worker.setTask(task);
        workerRepository.save(worker);
        if (task.isActive() && worker.isActive()) {
          tmpPairs.add(Pair.builder().id(id).pairId(pairId).worker(worker).lastPairingDate(lastPairingDate).build());
        }
      });
    });
    pairRepository.saveAll(tmpPairs);
    var tasks = retrieveRoomById(pairListInputDto.getRoom()).getTasks();
    return PairListDto.builder().room(CrudUtils.uuidFromString(pairListInputDto.getRoom()))
        .saved(true)
        .tasks(taskMapper.tasksToSetTaskDto(tasks))
        .build();
  }

  @Override
  public void deleteAllPairs() {
    log.info("PairService - deleteAllPairs");
    pairRepository.deleteAll();
  }

  private Worker getBestPairForWorker(Worker worker, List<List<Worker>> possiblePairs, Map<Integer, Long> weightings) {

    var workerId = worker.getId();
    var indexes = new ArrayList<Integer>();
    possiblePairs.forEach(pair -> pair.forEach(e -> {
      if ( workerId.equals(e.getId())) {
        indexes.add(possiblePairs.indexOf(pair));
      }
    }));

    var suitableForThisWorker = new HashMap<Integer, Long>();
    indexes.forEach(index -> suitableForThisWorker.put(index, weightings.get(index)));
    var highestWeight = suitableForThisWorker.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .limit(1)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, HashMap::new));
    var pairIndex = highestWeight.entrySet().stream().findFirst().map(Map.Entry::getKey).orElse(0);
    var lastPair = indexes.stream().filter(e -> e.equals(pairIndex)).map(possiblePairs::get).toList();

    for (List<Worker> workers : lastPair) {
      for (Worker suitableWorker : workers) {
        if (!suitableWorker.getId().equals(workerId)) {
          return suitableWorker;
        }
      }
    }
    return null;
  }

  private Long getWeighting(List<Worker> workers) {
    var uuids = workers.stream().map(Worker::getId).toList();
    var mostRecentPairing = pairRepository.findMostRecentPairing(uuids, uuids.size());
    var currentTime = LocalDateTime.now();
    return ChronoUnit.SECONDS.between(currentTime, mostRecentPairing
        .orElse(Pair.builder().lastPairingDate(currentTime).build()).getLastPairingDate());
  }

  private Optional<Worker> getOddWorker(List<Worker> activeWorkers) {
    Optional<Worker> evenWorker = Optional.empty();
    if(activeWorkers.size() % 2 != 0) {
      evenWorker = Optional.of(activeWorkers.remove(activeWorkers.size() - 1));
    }
    return evenWorker;
  }

  private void checkSpaceForWorkers(Map<Boolean, List<Task>> partitionedTasks,
                                    Map<Boolean, List<Worker>> partitionedWorkers) {
    if(partitionedTasks.get(true).size() * 2 < partitionedWorkers.get(true).size()) {
      throw new CoworkBuddyTechnicalException("Not enough active Tasks for the current active Workers.");
    }
    if(!partitionedWorkers.get(false).isEmpty() && partitionedTasks.get(false).isEmpty()) {
      throw new CoworkBuddyTechnicalException("Not enough inactive Tasks for the current inactive Workers.");
    }
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

}

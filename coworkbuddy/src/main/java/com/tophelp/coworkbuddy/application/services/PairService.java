package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.application.api.IPairService;
import com.tophelp.coworkbuddy.application.utils.CrudUtils;
import com.tophelp.coworkbuddy.application.utils.PairGenerator;
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
    Room room = retrieveRoomById(roomId);
    Map<Boolean, List<Worker>> partitionedWorkers = room.getWorkers().stream().collect(Collectors.partitioningBy(Worker::isActive));
    var activeWorkers = partitionedWorkers.get(true);
    var workersSize = activeWorkers.size() -1;
    var removedWorkers = new ArrayList<Worker>();
    for (int i = workersSize; i >= 0; i--) {
      if(removedWorkers.contains(activeWorkers.get(i))) {
       break;
      }
      var possiblePairs = pairGenerator.generateAllPossiblePairsForWorker(activeWorkers.get(i), activeWorkers, removedWorkers);
      var weightings = new HashMap<Integer, Long>();
      possiblePairs.forEach(pair -> {
        weightings.put(possiblePairs.indexOf(pair), getWeighting(pair));
      });
      var workerToPairWith = getBestPairForWorker(activeWorkers.get(i), possiblePairs, weightings);
      //todo: add workers to tasks and save pair
      removedWorkers.add(activeWorkers.get(i));
      removedWorkers.add(workerToPairWith.get());
      System.out.println("new pair: " + activeWorkers.get(i).getName() + " and " + workerToPairWith.get().getName());
    }
    return null;
  }

  private Optional<Worker> getBestPairForWorker(Worker worker, List<List<Worker>> possiblePairs, Map<Integer, Long> weightings) {
    var workerId = worker.getId();
    var indexes = new ArrayList<Integer>();

    possiblePairs.forEach(pair -> pair.forEach(e -> {
      if ( workerId.equals(e.getId())) {
        indexes.add(possiblePairs.indexOf(pair));
      }
    }));

    Map<Integer, Long> suitableForThisWorker = new HashMap<>();
    indexes.forEach(index -> suitableForThisWorker.put(index, weightings.get(index)));

    Map<Integer, Long> biggestOne = suitableForThisWorker.entrySet()
        .stream()
        .sorted(Map.Entry.<Integer, Long>comparingByValue(Comparator.reverseOrder()))
        .limit(1)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, HashMap::new));

    var pairIndex = biggestOne.entrySet().stream().findFirst().map(Map.Entry::getKey).orElse(0);
    var lastPair = indexes.stream().filter(e -> e.equals(pairIndex)).map(possiblePairs::get).toList();

    Optional<Worker> lastWorker = Optional.empty();
    for (List<Worker> workers : lastPair) {
      for (Worker value : workers) {
        if (!value.getId().equals(workerId)) {
          lastWorker = Optional.of(value);
          break;
        }
      }
    }

    return lastWorker;
  }

  private Long getWeighting(List<Worker> workers) {
    var uuids = workers.stream().map(Worker::getId).toList();
    var mostRecentPairing = pairRepository.findMostRecentPairing(uuids, uuids.size());
    var currentTime = LocalDateTime.now();
    return ChronoUnit.SECONDS.between(currentTime, mostRecentPairing
        .orElse(Pair.builder().lastPairingDate(currentTime).build()).getLastPairingDate());
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

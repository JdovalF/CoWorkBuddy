package com.tophelp.coworkbuddy.application.utils;

import com.tophelp.coworkbuddy.domain.Worker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class PairGenerator {

  public List<List<Worker>> generateAllPossiblePairsForWorker(Worker worker, List<Worker> workers,
                                                              List<Worker> removedWorkers) {
    List<List<Worker>> pairs = new ArrayList<>();

    List<Worker> filteredWorkers = workers.stream()
        .filter(w -> removedWorkers.stream()
            .noneMatch(rw -> rw.equals(w))).toList();

    for (int i = 0; i < filteredWorkers.size() - 1; i++) {
      for (int j = 1; j < filteredWorkers.size(); j++) {
        if(filteredWorkers.get(i).equals(worker) || filteredWorkers.get(j).equals(worker)) {
          pairs.add(List.of(filteredWorkers.get(i), filteredWorkers.get(j)));
        }
      }
    }

    return pairs;
  }

public List<List<String>> generateAllPossibleStringPairs(List<String> workers) {
    List<List<String>> pairs = new ArrayList<>();
    IntStream.range(0, workers.size())
        .forEachOrdered(i -> IntStream.range(i + 1, workers.size())
            .forEachOrdered(j -> pairs.add(List.of(workers.get(i), workers.get(j)))));
    return pairs;
  }
}

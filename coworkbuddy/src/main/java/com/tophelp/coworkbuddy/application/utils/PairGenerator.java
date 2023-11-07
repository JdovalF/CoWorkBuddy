package com.tophelp.coworkbuddy.application.utils;

import com.tophelp.coworkbuddy.domain.Worker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class PairGenerator {

  public List<List<Worker>> generatePossiblePairsForWorker(List<Worker> workers, List<Worker> removedWorkers) {
    List<Worker> filteredWorkers = workers.stream()
        .filter(w -> removedWorkers.stream()
            .noneMatch(rw -> rw.equals(w))).toList();
    return generateAllPossiblePairs(filteredWorkers);
  }

  public List<List<String>> generateAllPossibleStringPairs(List<String> workers) {
    return generateAllPossiblePairs(workers);
  }

  private <T> List<List<T>> generateAllPossiblePairs(List<T> elements) {
    List<List<T>> pairs = new ArrayList<>();
    IntStream.range(0, elements.size())
        .forEachOrdered(i -> IntStream.range(i + 1, elements.size())
            .forEachOrdered(j -> pairs.add(List.of(elements.get(i), elements.get(j)))));
    return pairs;
  }

}

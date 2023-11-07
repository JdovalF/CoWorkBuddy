package com.tophelp.coworkbuddy.application.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class PairGeneratorTest {

  private final PairGenerator pairGenerator =  new PairGenerator();

  @Test
  void generateAllPossibleStringPairs() {
    List<String> workers = new ArrayList<>();
    workers.add("worker1");
    workers.add("worker2");
    workers.add("worker3");
    workers.add("worker4");
    List<List<String>> pairs = pairGenerator.generateAllPossibleStringPairs(workers);
    System.out.println(pairs);
    for (List<String> pair : pairs) {
      System.out.println(pair);
    }
    assertTrue(true);
  }
}
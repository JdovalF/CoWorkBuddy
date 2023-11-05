package com.tophelp.coworkbuddy.ui.controller;

import com.tophelp.coworkbuddy.application.api.IWorkerService;
import com.tophelp.coworkbuddy.infrastructure.dto.input.WorkerInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.WorkerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/api/v1/workers")
@RequiredArgsConstructor
public class WorkerResource {

  private final IWorkerService workerService;

  @PostMapping
  public ResponseEntity<WorkerDto> createWorker(@RequestBody WorkerInputDto workerInputDto) {
    log.info("WorkerResource - createWorker");
    var savedWorker = workerService.createWorker(workerInputDto);
    var location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(savedWorker.getId())
        .toUri();
    return ResponseEntity.created(location).body(savedWorker);
  }

  @PatchMapping
  public ResponseEntity<WorkerDto> updateWorker(@RequestBody WorkerInputDto workerInputDto) {
    log.info("WorkerResource - createWorker");
    return ResponseEntity.ok(workerService.updateWorker(workerInputDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteWorkerById(@PathVariable String id) {
    log.info("WorkerResource - deleteWorkerById - Id {}", id);
    workerService.deleteWorkerById(id);
    return ResponseEntity.noContent().build();
  }

}

package com.tophelp.coworkbuddy.ui.controller;

import com.tophelp.coworkbuddy.application.api.ITaskService;
import com.tophelp.coworkbuddy.infrastructure.dto.input.TaskInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.TaskDto;
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
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskResource {

  private final ITaskService taskService;

  @PostMapping
  public ResponseEntity<TaskDto> createTask(@RequestBody TaskInputDto taskInputDto) {
    log.info("TaskResource - createTask");
    var savedTask = taskService.createTask(taskInputDto);
    var location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(savedTask.getId())
        .toUri();
    return ResponseEntity.created(location).body(savedTask);
  }

  @PatchMapping
  public ResponseEntity<TaskDto> updateTask(@RequestBody TaskInputDto taskInputDto) {
    log.info("TaskResource - updateTask");
    return ResponseEntity.ok(taskService.updateTask(taskInputDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTaskById(@PathVariable String id) {
    log.info("TaskResource - deleteTaskById - Id {}", id);
    taskService.deleteTaskById(id);
    return ResponseEntity.noContent().build();
  }

}

package com.tophelp.coworkbuddy.application.api;

import com.tophelp.coworkbuddy.infrastructure.dto.input.WorkerInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.WorkerDto;

public interface IWorkerService {
  WorkerDto createWorker(WorkerInputDto workerInputDto);

  WorkerDto updateWorker(WorkerInputDto workerInputDto);

  void deleteWorkerById(String id);
}

package com.tophelp.coworkbuddy.application.api;

import com.tophelp.coworkbuddy.infrastructure.dto.input.RoomInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoomDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.TaskDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.WorkerDto;

import java.util.List;

public interface IRoomService {

  RoomDto createRoom(RoomInputDto roomInputDto);

  RoomDto updateRoom(RoomInputDto roomInputDto);

  void deleteRoomById(String id);

  List<TaskDto> retrieveTasksByRoomId(String id);

  List<WorkerDto> retrieveWorkersByRoomId(String id);

  RoomDto retrieveRoomDtoById(String id);
}

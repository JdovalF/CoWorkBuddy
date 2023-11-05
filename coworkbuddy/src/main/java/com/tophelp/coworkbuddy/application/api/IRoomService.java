package com.tophelp.coworkbuddy.application.api;

import com.tophelp.coworkbuddy.infrastructure.dto.input.RoomInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoomDto;

public interface IRoomService {

  RoomDto createRoom(RoomInputDto roomInputDto);

  RoomDto updateRoom(RoomInputDto roomInputDto);

  void deleteRoomById(String id);

}

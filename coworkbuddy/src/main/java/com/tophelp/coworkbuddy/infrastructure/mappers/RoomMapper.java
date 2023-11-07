package com.tophelp.coworkbuddy.infrastructure.mappers;

import com.tophelp.coworkbuddy.domain.Room;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoomDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
  RoomDto roomToRoomDto(Room room);

}

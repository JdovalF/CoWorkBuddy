package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.application.api.IRoomService;
import com.tophelp.coworkbuddy.application.utils.CrudUtils;
import com.tophelp.coworkbuddy.domain.Room;
import com.tophelp.coworkbuddy.domain.User;
import com.tophelp.coworkbuddy.infrastructure.dto.input.RoomInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoomDto;
import com.tophelp.coworkbuddy.infrastructure.exceptions.DatabaseNotFoundException;
import com.tophelp.coworkbuddy.infrastructure.mappers.RoomMapper;
import com.tophelp.coworkbuddy.infrastructure.repository.RoomRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomMapper roomMapper;

    @Override
    public RoomDto createRoom(RoomInputDto roomInputDto) {
        log.info("RoomService - createRoom");
        CrudUtils.throwExceptionWhenNull(roomInputDto.getId(), "Id", false);
        CrudUtils.throwExceptionWhenNull(roomInputDto.getName(), "Name", true);
        var actualUser = retriveUserFromRoomInputDto(roomInputDto.getId());
        var actualRoom = Room.builder().id(UUID.randomUUID()).name(roomInputDto.getName()).user(actualUser).build();
        return roomMapper.roomToRoomDto(roomRepository.save(actualRoom));
    }

    @Override
    public RoomDto updateRoom(RoomInputDto roomInputDto) {
        log.info("RoomService - updateRoom");
        CrudUtils.throwExceptionWhenNull(roomInputDto.getId(), "Id", true);
        CrudUtils.throwExceptionWhenNull(roomInputDto.getName(), "Name", true);
        var oldRoom = retrieveRoomById(roomInputDto.getId());
        oldRoom.setName(roomInputDto.getName());
        if(nonNull(roomInputDto.getUserId())
           && !CrudUtils.uuidFromString(roomInputDto.getUserId()).equals(oldRoom.getUser().getId())) {
            oldRoom.setUser(retriveUserFromRoomInputDto(roomInputDto.getId()));
        }
        return roomMapper.roomToRoomDto(roomRepository.save(oldRoom));
    }

    @Override
    public void deleteRoomById(String id) {
        log.info("RoomService - deleteRoomById - Id: {}", id);
        roomRepository.delete(retrieveRoomById(id));
    }

    private Room retrieveRoomById(String id) {
        return roomRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
                () -> new DatabaseNotFoundException(format("Room Id: %s not found in Database", id)));
    }

    private User retriveUserFromRoomInputDto(String id) {
        return userRepository.findById(CrudUtils.uuidFromString(id)).orElseThrow(
                () -> new DatabaseNotFoundException(format("User Id: %s not found in Database", id)));
    }

}
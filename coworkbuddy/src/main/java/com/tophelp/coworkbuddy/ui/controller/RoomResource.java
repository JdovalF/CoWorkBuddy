package com.tophelp.coworkbuddy.ui.controller;

import com.tophelp.coworkbuddy.application.api.IRoomService;
import com.tophelp.coworkbuddy.infrastructure.dto.input.RoomInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoomDto;
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
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomResource {

  private final IRoomService roomService;

  @PostMapping()
  public ResponseEntity<RoomDto> createRoom(@RequestBody RoomInputDto roomInputDto) {
    log.info("RoomResource - createRoom");
    var savedRoom = roomService.createRoom(roomInputDto);
    var location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(savedRoom.getId())
        .toUri();
    return ResponseEntity.created(location).body(savedRoom);
  }

  @PatchMapping
  public ResponseEntity<RoomDto> updateRoom(@RequestBody RoomInputDto roomInputDto) {
    log.info("RoomResource - updateRoom");
    return ResponseEntity.ok(roomService.updateRoom(roomInputDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRoomById(@PathVariable String id) {
    log.info("RoomResource - deleteRoomById - Id: {}", id);
    roomService.deleteRoomById(id);
    return ResponseEntity.noContent().build();
  }

}

package com.tophelp.coworkbuddy.application.api;

import com.tophelp.coworkbuddy.infrastructure.dto.input.UserInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoomDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.UserDto;

import java.util.List;

public interface IUserService {
  List<UserDto> retrieveAllUsers();

  UserDto retrieveUserById(String id);

  UserDto createUser(UserInputDto userInputDto);

  UserDto updateUser(UserInputDto userInputDto);

  List<RoomDto> findAllRoomsByUserId(String id);

  UserDto retrieveUserByUsername(String username);
}

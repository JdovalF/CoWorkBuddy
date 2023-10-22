package com.tophelp.coworkbuddy.infrastructure.mappers;

import com.tophelp.coworkbuddy.domain.User;
import com.tophelp.coworkbuddy.infrastructure.dto.input.UserInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDTO(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    User userInputDtoToUser(UserInputDto userInputDto);
}

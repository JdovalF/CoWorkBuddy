package com.tophelp.coworkbuddy.infrastructure.mappers;

import com.tophelp.coworkbuddy.domain.User;
import com.tophelp.coworkbuddy.infrastructure.dto.input.UserInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDTO(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "id", ignore = true)
    User userInputDtoToUser(UserInputDto userInputDto);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", source = "id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "username", source = "username", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", source = "email", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserInputDto(UserInputDto userInputDto, @MappingTarget User user);
}

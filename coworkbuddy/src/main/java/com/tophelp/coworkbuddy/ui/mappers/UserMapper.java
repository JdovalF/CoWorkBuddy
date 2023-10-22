package com.tophelp.coworkbuddy.ui.mappers;

import com.tophelp.coworkbuddy.domain.User;
import com.tophelp.coworkbuddy.ui.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDTO(User user);
}

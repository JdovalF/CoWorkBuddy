package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.application.api.IUserService;
import com.tophelp.coworkbuddy.application.utils.CrudUtils;
import com.tophelp.coworkbuddy.domain.Role;
import com.tophelp.coworkbuddy.domain.User;
import com.tophelp.coworkbuddy.infrastructure.dto.input.RoleInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.input.UserInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.UserDto;
import com.tophelp.coworkbuddy.infrastructure.exceptions.DatabaseNotFoundException;
import com.tophelp.coworkbuddy.infrastructure.mappers.RoleMapper;
import com.tophelp.coworkbuddy.infrastructure.mappers.UserMapper;
import com.tophelp.coworkbuddy.infrastructure.repository.RoleRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> retrieveAllUsers() {
        log.info("UserService - retrieveAllUsers");
        return userRepository.findAll().stream().map(userMapper::userToUserDTO).toList();
    }

    @Override
    public UserDto retrieveUserById(String id) {
        log.info("UserService - retrieveUserById - Id: {}", id);
        CrudUtils.throwExceptionWhenNull(id, "Id", true);
        return userMapper.userToUserDTO(userRepository.findById(CrudUtils.uuidFromString(id))
                .orElseThrow(() -> new DatabaseNotFoundException(format("Id: %s not found in Database", id))));
    }

    @Override
    public UserDto createUser(UserInputDto userInputDto) {
        log.info("UserService - createUser");
        CrudUtils.throwExceptionWhenNull(userInputDto.getId(), "Id", false);
        CrudUtils.throwExceptionWhenNull(userInputDto.getPassword(), "Password", true);
        var newUser = userMapper.userInputDtoToUser(userInputDto);
        newUser.setRoles(isNull(userInputDto.getRoles()) || userInputDto.getRoles().isEmpty()
                ? Set.of(roleRepository.findRoleByName("USER").orElseThrow(() -> new DatabaseNotFoundException(
                        format("Role: %s not found in Database", "USER"))))
                : userInputDto.getRoles().stream().map(roleMapper::roleInputDtoToRole).collect(Collectors.toSet()));
        newUser.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        newUser.setId(UUID.randomUUID());
        return userMapper.userToUserDTO(userRepository.save(newUser));
    }

    @Override
    public UserDto updateUser(UserInputDto userInputDto) {
        log.info("UserService - updateUser - Id: {}", userInputDto.getId());
        CrudUtils.throwExceptionWhenNull(userInputDto.getId(), "Id", true);
        User actualUser = userRepository.findById(CrudUtils.uuidFromString(userInputDto.getId())).orElseThrow(
              () -> new DatabaseNotFoundException(format("Id: %s not found in Database", userInputDto.getId())));
        userMapper.updateUserFromUserInputDto(userInputDto, actualUser);
        if(nonNull(userInputDto.getPassword())
           && !passwordEncoder.matches(userInputDto.getPassword(), actualUser.getPassword())) {
            actualUser.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        }
        if(nonNull(userInputDto.getRoles()) && !userInputDto.getRoles().isEmpty()) {
            Set<Role> roles = userInputDto.getRoles().stream().map(RoleInputDto::getId)
                    .map(this::findRoleById).collect(Collectors.toSet());
            actualUser.setRoles(roles);
        }
        return userMapper.userToUserDTO(userRepository.save(actualUser));
    }

    private Role findRoleById(String id) {
        return roleRepository.findById(CrudUtils.uuidFromString(id))
                .orElseThrow(() -> new DatabaseNotFoundException(format("Role with id: %s not found in Database", id)));
    }

}

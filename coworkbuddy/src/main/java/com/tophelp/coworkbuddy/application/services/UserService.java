package com.tophelp.coworkbuddy.application.services;

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
import com.tophelp.coworkbuddy.shared.exceptions.CoworkBuddyTechnicalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> retrieveAllUsers() {
        log.info("UserService - retrieveAllUsers");
        return userRepository.findAll().stream().map(userMapper::userToUserDTO).toList();
    }

    public UserDto retrieveUserById(String id) {
        log.info("UserService - retrieveUserById - id: {}", id);
        throwExceptionWhenNull(id, "Id", true);
        return userMapper.userToUserDTO(userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DatabaseNotFoundException(String.format("Id: %s not found in Database", id))));
    }

    public UserDto createUser(UserInputDto userInputDto) {
        log.info("UserService - createUser");
        throwExceptionWhenNull(userInputDto.getId(), "Id", false);
        throwExceptionWhenNull(userInputDto.getPassword(), "Password", true);
        var newUser = userMapper.userInputDtoToUser(userInputDto);
        newUser.setRoles(isNull(userInputDto.getRoles()) || userInputDto.getRoles().isEmpty()
                ? Set.of(roleRepository.findRoleByName("USER").orElseThrow(() -> new DatabaseNotFoundException(
                        String.format("Role: %s not found in Database", "USER"))))
                : userInputDto.getRoles().stream().map(roleMapper::roleInputDtoToRole).collect(Collectors.toSet()));
        newUser.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        newUser.setId(UUID.randomUUID());
        return userMapper.userToUserDTO(userRepository.save(newUser));
    }

    public UserDto updateUser(UserInputDto userInputDto) {
        log.info("UserService - updateUser - id: {}", userInputDto.getId());
        throwExceptionWhenNull(userInputDto.getId(), "Id", true);
        User actualUser = userRepository.findById(UUID.fromString(userInputDto.getId())).orElseThrow(
              () -> new DatabaseNotFoundException(String.format("Id: %s not found in Database", userInputDto.getId())));
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
        return roleRepository.findById(UUID.fromString(id)).orElseThrow(() -> new DatabaseNotFoundException(String.format("Role with id: %s not found in Database", id)));
    }

    private void throwExceptionWhenNull(String parameter, String parameterName, boolean needsToBeNotNull) {
        if(isNull(parameter) == needsToBeNotNull) {
            throw new CoworkBuddyTechnicalException(String.format("%s %s required", parameterName,
                    needsToBeNotNull ? "is" : "is not"));
        }
    }

}

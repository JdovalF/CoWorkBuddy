package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.domain.User;
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
        return userRepository.findAll().stream().map(userMapper::userToUserDTO).toList();
    }

    public UserDto retrieveUserById(String id) {
        return userMapper.userToUserDTO(userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DatabaseNotFoundException(String.format("id: %s not found in Database", id))));
    }

    public UserDto createUser(UserInputDto userInputDto) {
        if(isNull(userInputDto.getPassword())) {
            throw new CoworkBuddyTechnicalException("Password is required");
        }
        User newUser = userMapper.userInputDtoToUser(userInputDto);
        newUser.setRoles(isNull(userInputDto.getRoles()) || userInputDto.getRoles().isEmpty()
                ? Set.of(roleRepository.findRoleByName("USER").orElseThrow(() -> new DatabaseNotFoundException(
                        String.format("Role: %s not found in Database", "USER"))))
                : userInputDto.getRoles().stream().map(roleMapper::roleInputDtoToRole).collect(Collectors.toSet()));
        newUser.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        newUser.setId(UUID.randomUUID());
        return userMapper.userToUserDTO(userRepository.save(newUser));
    }
}

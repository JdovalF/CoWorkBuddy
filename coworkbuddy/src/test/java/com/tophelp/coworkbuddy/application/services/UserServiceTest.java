package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.domain.Role;
import com.tophelp.coworkbuddy.domain.Room;
import com.tophelp.coworkbuddy.domain.User;
import com.tophelp.coworkbuddy.infrastructure.dto.input.RoleInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.input.UserInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoleDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoomDto;
import com.tophelp.coworkbuddy.infrastructure.exceptions.DatabaseNotFoundException;
import com.tophelp.coworkbuddy.infrastructure.mappers.UserMapper;
import com.tophelp.coworkbuddy.infrastructure.repository.RoleRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.RoomRepository;
import com.tophelp.coworkbuddy.infrastructure.repository.UserRepository;
import com.tophelp.coworkbuddy.shared.exceptions.CoworkBuddyTechnicalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Spy
  private PasswordEncoder passwordEncoder;
  @Spy
  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Mock
  private UserRepository userRepository;
  @Mock
  private RoleRepository roleRepository;
  @Mock
  private RoomRepository roomRepository;

  @InjectMocks
  private UserService userService;

  @Test
  void shouldRetrieveAllUsers_whenRetrieveAllUsersCalled() {
    var expectedUsers = List.of(buildMinimalUser(UUID.randomUUID()), buildMinimalUser(UUID.randomUUID()));
    when(userRepository.findAll()).thenReturn(expectedUsers);
    var actualUserDtos = userService.retrieveAllUsers();
    IntStream.rangeClosed(0, expectedUsers.size() - 1)
        .forEach(index -> assertTrue(reflectionEquals(userMapper.userToUserDTO(expectedUsers.get(index)), actualUserDtos.get(index))));
  }

  @Test
  void shouldReturnEmptyList_whenRetrieveAllUsersCalled() {
    when(userRepository.findAll()).thenReturn(emptyList());
    var actualUserDtos = userService.retrieveAllUsers();
    assertTrue(actualUserDtos.isEmpty());
  }

  @Test
  void shouldReturnUserDto_whenRetrieveUserByIdCalled_withId() {
    var givenId = String.valueOf(UUID.randomUUID());
    var expectedUser = buildMinimalUser(UUID.fromString(givenId));
    when(userRepository.findById(UUID.fromString(givenId))).thenReturn(Optional.of(expectedUser));
    var actualUserDto = userService.retrieveUserById(givenId);
    assertTrue(reflectionEquals(userMapper.userToUserDTO(expectedUser), actualUserDto));
  }

  @Test
  void shouldThrowCoworkBuddyTechnicalException_whenRetrieveUserByIdCalled_withNullId() {
    var expectedException = new CoworkBuddyTechnicalException("Id is required");
    assertEquals(expectedException.getMessage(),
        assertThrows(CoworkBuddyTechnicalException.class,
            () -> userService.retrieveUserById(null)).getMessage());
  }

  @Test
  void shouldThrowDataBaseNotFoundException_whenRetrieveUserByIdCalled_byId() {
    var givenId = String.valueOf(UUID.randomUUID());
    var expectedException = new DatabaseNotFoundException(String.format("Id: %s not found in Database", givenId));
    when(userRepository.findById(UUID.fromString(givenId))).thenReturn(Optional.empty());
    assertEquals(expectedException.getMessage(),
        assertThrows(DatabaseNotFoundException.class,
            () -> userService.retrieveUserById(givenId)).getMessage());
  }

  @Test
  void shouldThrowCoworkBuddyTechnicalException_whenCreateUserCalled_withInValidUserInputDto_withNotNullId() {
    var givenUserInputDto = buildMinimalUserInputDto(String.valueOf(UUID.randomUUID()));
    var expectedException = new CoworkBuddyTechnicalException("Id is not required");
    assertEquals(expectedException.getMessage(),
        assertThrows(CoworkBuddyTechnicalException.class,
            () -> userService.createUser(givenUserInputDto)).getMessage());
  }

  @Test
  void shouldThrowCoworkBuddyTechnicalException_whenCreateUserCalled_withInValidUserInputDto_null_password() {
    var givenUserInputDto = buildMinimalUserInputDto(null);
    var expectedException = new CoworkBuddyTechnicalException("Password is required");
    assertEquals(expectedException.getMessage(),
        assertThrows(CoworkBuddyTechnicalException.class,
            () -> userService.createUser(givenUserInputDto)).getMessage());
  }

  @Test
  void shouldReturnUserDto_whenCreateUserCalled_withValidUserInputDto_withoutRoles() {
    var password = "test-password";
    var role = buildMinimalRole();
    var givenUserInputDto = buildMinimalUserInputDto(null);
    givenUserInputDto.setPassword(password);
    var expectedSavedUser = buildMinimalUser(UUID.randomUUID());
    expectedSavedUser.setPassword(password);
    when(roleRepository.findRoleByName("USER")).thenReturn(Optional.of(role));
    when(userRepository.save(any(User.class))).thenReturn(expectedSavedUser);
    var actualUserDto = userService.createUser(givenUserInputDto);
    assertTrue(reflectionEquals(userMapper.userToUserDTO(expectedSavedUser), actualUserDto));
  }

  @Test
  void shouldReturnUserDto_whenCreateUserCalled_withValidUserInputDto_withRoles() {
    var password = "test-password";
    Role role = buildMinimalRole();
    UUID roleId = role.getId();
    var givenUserInputDto = buildMinimalUserInputDto(null);
    givenUserInputDto.setPassword(password);
    givenUserInputDto.setRoles(Set.of(String.valueOf(roleId)));
    var expectedSavedUser = buildMinimalUser(UUID.randomUUID());
    expectedSavedUser.setPassword(password);
    when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
    when(userRepository.save(any(User.class))).thenReturn(expectedSavedUser);
    var actualUserDto = userService.createUser(givenUserInputDto);
    assertTrue(reflectionEquals(userMapper.userToUserDTO(expectedSavedUser), actualUserDto));
  }

  @Test
  void shouldThrowDatabaseNotFoundException_whenCreateUserCalled_withValidUserInputDto_withoutRoles_and_ROLE_USER_notFound() {
    var password = "test-password";
    var givenUserInputDto = buildMinimalUserInputDto(null);
    givenUserInputDto.setPassword(password);
    var expectedSavedUser = buildMinimalUser(UUID.randomUUID());
    expectedSavedUser.setPassword(password);
    var expectedException = new DatabaseNotFoundException("Role: USER not found in Database");
    when(roleRepository.findRoleByName("USER")).thenReturn(Optional.empty());
    var actualException = assertThrows(DatabaseNotFoundException.class,
        () -> userService.createUser(givenUserInputDto));
    assertEquals(expectedException.getMessage(), actualException.getMessage());
  }

  @Test
  void shouldThrowCoworkBuddyTechnicalException_whenUpdateUserCalled_withInValidUserInputDto_NoId() {
    var expectedMessage = "Id is required";
    var givenUserInputDto = buildMinimalUserInputDto(null);
    var actualException = assertThrows(CoworkBuddyTechnicalException.class,
        () -> userService.updateUser(givenUserInputDto));
    assertEquals(expectedMessage, actualException.getMessage());
  }

  @Test
  void shouldThrowDatabaseNotFoundException_whenUpdateUserCalled_withInValidUserInputDto_IdNotFound() {
    var uuid = String.valueOf(UUID.randomUUID());
    var expectedMessage = String.format("Id: %s not found in Database", uuid);
    var givenUserInputDto = buildMinimalUserInputDto(uuid);
    when(userRepository.findById(UUID.fromString(uuid))).thenReturn(Optional.empty());
    var actualException = assertThrows(DatabaseNotFoundException.class,
        () -> userService.updateUser(givenUserInputDto));
    assertEquals(expectedMessage, actualException.getMessage());
  }

  @Test
  void shouldReturnUpdateUser_whenCreateUserCalled_withValidUserInputDto_withoutRoles() {
    var uuid = String.valueOf(UUID.randomUUID());
    var givenUserInputDto = buildMinimalUserInputDto(uuid);
    givenUserInputDto.setUsername("new-username");
    var givenUser = buildMinimalUser(UUID.fromString(uuid));
    givenUser.setUsername("old-username");
    when(userRepository.findById(UUID.fromString(uuid))).thenReturn(Optional.of(givenUser));
    givenUser.setUsername("new-username");
    when(userRepository.save(givenUser)).thenReturn(givenUser);
    var actualUserDto = userService.updateUser(givenUserInputDto);
    assertTrue(reflectionEquals(userMapper.userToUserDTO(givenUser), actualUserDto));
  }

  @Test
  void shouldReturnUpdateUser_whenCreateUserCalled_withValidUserInputDto_withNewRoles() {
    var uuid = String.valueOf(UUID.randomUUID());
    var givenUserInputDto = buildMinimalUserInputDto(uuid);
    givenUserInputDto.setUsername("new-username");
    var adminId = UUID.randomUUID();
    var adminInputdto = RoleInputDto.builder().id(String.valueOf(adminId)).name("ADMIN").build();
    var inputDtoRoles = Set.of(adminInputdto.getId());
    givenUserInputDto.setRoles(inputDtoRoles);
    var givenUser = buildMinimalUser(UUID.fromString(uuid));
    givenUser.setUsername("old-username");
    var user = Role.builder().id(UUID.randomUUID()).name("USER").build();
    givenUser.setRoles(Set.of(user));
    var admin = Role.builder().id(adminId).name("ADMIN").build();
    when(userRepository.findById(UUID.fromString(uuid))).thenReturn(Optional.of(givenUser));
    when(roleRepository.findById(adminId)).thenReturn(Optional.of(admin));
    givenUser.setUsername("new-username");
    givenUser.setRoles(Set.of(admin));
    when(userRepository.save(givenUser)).thenReturn(givenUser);
    var actualUserDto = userService.updateUser(givenUserInputDto);
    var actualAdminIdRole = actualUserDto.getRoles().stream().findFirst().map(RoleDto::getId);
    var expectedAdminIdRole = userMapper.userToUserDTO(givenUser).getRoles().stream().findFirst().map(RoleDto::getId);
    assertEquals(expectedAdminIdRole, actualAdminIdRole);
  }

  @Test
  void shouldReturnUpdateUser_whenCreateUserCalled_withValidUserInputDto_withoutRoles_withRooms_andNewPassword() {
    var uuid = String.valueOf(UUID.randomUUID());
    var givenUserInputDto = buildMinimalUserInputDto(uuid);
    givenUserInputDto.setPassword("new-password");
    UUID roomId = UUID.randomUUID();
    givenUserInputDto.setRooms(Set.of(String.valueOf(roomId)));
    var givenUser = buildMinimalUser(UUID.fromString(uuid));
    givenUser.setPassword(passwordEncoder.encode("old-password"));
    when(userRepository.findById(UUID.fromString(uuid))).thenReturn(Optional.of(givenUser));
    when(userRepository.save(givenUser)).thenReturn(givenUser);
    when(roomRepository.findById(roomId)).thenReturn(Optional.of(buildMinimalRoom(roomId)));
    var actualUserDto = userService.updateUser(givenUserInputDto);
    assertTrue(reflectionEquals(userMapper.userToUserDTO(givenUser), actualUserDto));
  }

  @Test
  void shouldReturnRoomDtos_whenFindAllRoomsByUserId() {
    UUID userUuid = UUID.randomUUID();
    UUID roomUuid = UUID.randomUUID();
    var givenUser = buildMinimalUser(userUuid);
    var expectedRooms = List.of(RoomDto.builder().id(roomUuid).name("DeathStar").build());
    givenUser.setRooms(Set.of(buildMinimalRoom(roomUuid)));
    when(userRepository.findById(userUuid)).thenReturn(Optional.of(givenUser));
    var actualRooms = userService.findAllRoomsByUserId(String.valueOf(userUuid));
    assertIterableEquals(expectedRooms, actualRooms);
  }

  private User buildMinimalUser(UUID uuid) {
    return User.builder().id(uuid).build();
  }

  private UserInputDto buildMinimalUserInputDto(String uuid) {
    return UserInputDto.builder().id(uuid).build();
  }

  private Role buildMinimalRole() {
    return Role.builder().id(UUID.randomUUID()).name("USER").build();
  }

  private Room buildMinimalRoom(UUID roomId) {
    return Room.builder().id(roomId).name("DeathStar").build();
  }
}

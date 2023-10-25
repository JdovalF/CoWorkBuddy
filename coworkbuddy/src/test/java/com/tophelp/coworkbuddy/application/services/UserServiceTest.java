package com.tophelp.coworkbuddy.application.services;

import com.tophelp.coworkbuddy.domain.User;
import com.tophelp.coworkbuddy.infrastructure.dto.input.UserInputDto;
import com.tophelp.coworkbuddy.infrastructure.exceptions.DatabaseNotFoundException;
import com.tophelp.coworkbuddy.infrastructure.mappers.RoleMapper;
import com.tophelp.coworkbuddy.infrastructure.mappers.UserMapper;
import com.tophelp.coworkbuddy.infrastructure.repository.RoleRepository;
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
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Spy
    private PasswordEncoder passwordEncoder;
    @Spy
    private final UserMapper userMapper =  Mappers.getMapper(UserMapper.class);
    @Spy
    private final RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;


    @InjectMocks
    private UserService userService;

    @Test
    void shouldRetrieveAllUsers_whenRetrieveAllUsersCalled() {
        //Given
        var expectedUsers = List.of(buildMinimalUser(UUID.randomUUID()), buildMinimalUser(UUID.randomUUID()));
        //When
        when(userRepository.findAll()).thenReturn(expectedUsers);
        var actualUserDtos = userService.retrieveAllUsers();
        //Then
        IntStream.rangeClosed(0, expectedUsers.size() - 1)
                .forEach(index -> {
                    assertTrue(reflectionEquals(userMapper.userToUserDTO(expectedUsers.get(index)), actualUserDtos.get(index)));
                });
    }

    @Test
    void shouldReturnEmptyList_whenRetrieveAllUsersCalled() {
        //Given
        //When
        when(userRepository.findAll()).thenReturn(emptyList());
        var actualUserDtos = userService.retrieveAllUsers();
        //Then
        assertTrue(actualUserDtos.isEmpty());
    }

    @Test
    void shouldReturnUserDto_whenRetrieveUserByIdCalled_withId() {
        //Given
        var givenId = String.valueOf(UUID.randomUUID());
        var expectedUser = buildMinimalUser(UUID.fromString(givenId));
        //When
        when(userRepository.findById(UUID.fromString(givenId))).thenReturn(Optional.of(expectedUser));
        var actualUserDto = userService.retrieveUserById(givenId);
        //Then
        assertTrue(reflectionEquals(userMapper.userToUserDTO(expectedUser), actualUserDto));
    }

    @Test
    void shouldThrowCoworkBuddyTechnicalException_whenRetrieveUserByIdCalled_withNullId() {
        //Given
        String givenId = null;
        var expectedException = new CoworkBuddyTechnicalException("Id is required");
        //When
        //Then
        assertEquals(expectedException.getMessage(),
                assertThrows(CoworkBuddyTechnicalException.class,
                        () -> userService.retrieveUserById(givenId)).getMessage());
    }

    @Test
    void shouldThrowDataBaseNotFoundException_whenRetrieveUserByIdCalled_byId() {
        //Given
        var givenId = String.valueOf(UUID.randomUUID());
        var expectedException = new DatabaseNotFoundException(String.format("Id: %s not found in Database", givenId));
        //When
        when(userRepository.findById(UUID.fromString(givenId))).thenReturn(Optional.empty());
        //Then
        assertEquals(expectedException.getMessage(),
                assertThrows(DatabaseNotFoundException.class,
                        () -> userService.retrieveUserById(givenId)).getMessage());
    }


    //shouldReturnUserDto_whenCalled_withValidUserInputDto
    //shouldThrowCoworkBuddyTechnicalException_whenCalled_withInValidUserInputDto_not_null_id

    @Test
    void shouldThrowCoworkBuddyTechnicalException_whenCreateUserCalled_withInValidUserInputDto_withNotNullId() {
        //Given
        var givenUserInputDto = buildMinimalUserInputDto(String.valueOf(UUID.randomUUID()));
        var expectedException = new CoworkBuddyTechnicalException("Id is not required");
        //When
        //Then
        assertEquals(expectedException.getMessage(),
                assertThrows(CoworkBuddyTechnicalException.class,
                        () -> userService.createUser(givenUserInputDto)).getMessage());
    }
    //shouldThrowCoworkBuddyTechnicalException_whenCalled_withInValidUserInputDto_null_password
    @Test
    void shouldThrowCoworkBuddyTechnicalException_whenCreateUserCalled_withInValidUserInputDto_null_password() {
        //Given
        var givenUserInputDto = buildMinimalUserInputDto(null);
        var expectedException = new CoworkBuddyTechnicalException("Password is required");
        //When
        //Then
        assertEquals(expectedException.getMessage(),
                assertThrows(CoworkBuddyTechnicalException.class,
                        () -> userService.createUser(givenUserInputDto)).getMessage());
    }

    private User buildMinimalUser(UUID uuid) {
        return User.builder().id(uuid).build();
    }

    private UserInputDto buildMinimalUserInputDto(String uuid) {
        return UserInputDto.builder()
                .id(uuid)
                .build();
    }
}
//Given
//When
//Then
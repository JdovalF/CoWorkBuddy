package com.tophelp.coworkbuddy.ui.controller;

import com.tophelp.coworkbuddy.CoworkbuddyApplication;
import com.tophelp.coworkbuddy.application.services.UserService;
import com.tophelp.coworkbuddy.infrastructure.dto.output.RoleDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.UserDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;
import java.util.UUID;

@SpringBootTest(classes = CoworkbuddyApplication.class)
class ResourcesBaseClass {

  @Autowired
  private UserResource userResource;
  @MockBean
  private UserService userService;

  @BeforeEach
  public void setup() {
    RestAssuredMockMvc.standaloneSetup(userResource);

    Mockito.when(userService.retrieveUserById("8e69ffe0-1864-44b0-8881-ce95f85be14c"))
        .thenReturn(UserDto.builder()
            .id(UUID.fromString("8e69ffe0-1864-44b0-8881-ce95f85be14c"))
            .username("admin")
            .email("admin@coworkbuddy.com")
            .roles(Set.of(RoleDto.builder()
                .id(UUID.fromString("be7c0db8-69f2-4e4e-9c6b-4ede9b38ca50"))
                .name("ADMIN")
                .build()))
            .build());
  }
}
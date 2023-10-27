package com.tophelp.coworkbuddy.ui.resources;

import com.tophelp.coworkbuddy.application.services.UserService;
import com.tophelp.coworkbuddy.infrastructure.dto.input.UserInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserDto>> retrieveAllUsers() {
        log.info("UserResource - retrieveAllUsers");
        return ResponseEntity.ok(userService.retrieveAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> retrieveUserById(@PathVariable String id) {
        log.info("UserResource - retrieveUserById - id: {}", id);
        return ResponseEntity.ok(userService.retrieveUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody UserInputDto userInputDto) {
        log.info("UserResource - createUser");
        UserDto savedUser = userService.createUser(userInputDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedUser);
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserInputDto userInputDto) {
        log.info("UserResource - updateUser - id: {}", userInputDto.getId());
        return ResponseEntity.ok(userService.updateUser(userInputDto));
    }

}

package com.tophelp.coworkbuddy.ui.resources;

import com.tophelp.coworkbuddy.application.services.UserService;
import com.tophelp.coworkbuddy.infrastructure.dto.input.UserInputDto;
import com.tophelp.coworkbuddy.infrastructure.dto.output.UserDto;
import com.tophelp.coworkbuddy.shared.security.JwtTokenRequest;
import com.tophelp.coworkbuddy.shared.security.JwtTokenResponse;
import com.tophelp.coworkbuddy.shared.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserResource {
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenResponse> authenticate(@RequestBody JwtTokenRequest jwtTokenRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(jwtTokenRequest.username(), jwtTokenRequest.password());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var token = jwtTokenService.generateToken(authentication);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserDto>> retrieveAllUsers() {
        return ResponseEntity.ok(userService.retrieveAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> retrieveUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.retrieveUserById(id));
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody UserInputDto userInputDto) {
        UserDto savedUser = userService.createUser(userInputDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedUser);
    }

}

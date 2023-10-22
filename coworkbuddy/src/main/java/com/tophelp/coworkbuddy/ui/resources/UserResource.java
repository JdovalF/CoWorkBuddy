package com.tophelp.coworkbuddy.ui.resources;

import com.tophelp.coworkbuddy.application.services.UserService;
import com.tophelp.coworkbuddy.shared.security.JwtTokenRequest;
import com.tophelp.coworkbuddy.shared.security.JwtTokenResponse;
import com.tophelp.coworkbuddy.shared.security.JwtTokenService;
import com.tophelp.coworkbuddy.ui.dto.UserDto;
import com.tophelp.coworkbuddy.ui.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserResource {
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserMapper userMapper;
//    private final PasswordEncoder passwordEncoder;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenResponse> authenticate(@RequestBody JwtTokenRequest jwtTokenRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(jwtTokenRequest.username(), jwtTokenRequest.password());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var token = jwtTokenService.generateToken(authentication);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> retrieveAllUsers() {
        return ResponseEntity.ok(userService.retrieveAllUsers().stream().map(userMapper::userToUserDTO)
                .collect(Collectors.toList()));
    }

}

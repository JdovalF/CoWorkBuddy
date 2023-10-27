package com.tophelp.coworkbuddy.ui.resources;

import com.tophelp.coworkbuddy.shared.security.JwtTokenRequest;
import com.tophelp.coworkbuddy.shared.security.JwtTokenResponse;
import com.tophelp.coworkbuddy.shared.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/v1")
@RequiredArgsConstructor
public class AuthenticationResource {
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenResponse> authenticate(@RequestBody JwtTokenRequest jwtTokenRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(jwtTokenRequest.username(), jwtTokenRequest.password());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var token = jwtTokenService.generateToken(authentication);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }
}

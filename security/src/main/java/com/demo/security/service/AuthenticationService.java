package com.demo.security.service;

import com.demo.security.config.JwtUtils;
import com.demo.security.models.AuthenticationRequest;
import com.demo.security.models.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var jwtToken = jwtUtils.generateTokenFromUsername(request.getEmail());
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
}

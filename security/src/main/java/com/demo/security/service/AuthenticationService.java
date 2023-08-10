package com.demo.security.service;

import com.demo.security.config.JwtUtils;
import com.demo.security.models.AuthenticationRequest;
import com.demo.security.models.AuthenticationResponse;
import com.demo.security.user.User;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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

    public Optional<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtUtils.getUserNameFromJwtToken(refreshToken);
        if (userEmail != null) {
            User user = new User("Admin", "password");
            if (jwtUtils.validateJwtToken(refreshToken)) {
                String accessToken = jwtUtils.generateTokenFromUsername(user.getUsername());
                return Optional.of(AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build());
            }
        }
        return Optional.empty();
    }
}

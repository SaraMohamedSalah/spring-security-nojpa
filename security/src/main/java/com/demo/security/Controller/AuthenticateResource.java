package com.demo.security.Controller;

import com.demo.security.models.AuthenticationRequest;
import com.demo.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticateResource {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public String authenticateAndGetToken() {
        AuthenticationRequest user = AuthenticationRequest.builder()
                .email("Admin")
                .password("password")
                .build();
       return authenticationService.authenticate(user).getAccessToken();
    }

}

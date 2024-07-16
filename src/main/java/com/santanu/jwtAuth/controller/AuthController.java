package com.santanu.jwtAuth.controller;

import com.santanu.jwtAuth.model.*;
import com.santanu.jwtAuth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterUser request){
        // Get the response
        RegisterResponse response = authenticationService.register(request);

        // if user exists send status code 400 else status code 200
        if(response.getStatusCode()==400)
            return ResponseEntity.status(400).body(response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}

package com.santanu.chatlynk.controller;

import com.santanu.chatlynk.model.AuthenticationResponse;
import com.santanu.chatlynk.model.AuthenticationRequest;
import com.santanu.chatlynk.model.RegisterResponse;
import com.santanu.chatlynk.model.RegisterUser;
import com.santanu.chatlynk.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterUser request){
        // Get the response
        RegisterResponse response = authenticationService.register(request);

        // if user exists send status code 400 else status code 200
        if(response.getStatusCode()==400)
            return ResponseEntity.status(400).body(response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}

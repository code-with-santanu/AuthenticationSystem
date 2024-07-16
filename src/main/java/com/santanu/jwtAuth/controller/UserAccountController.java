package com.santanu.jwtAuth.controller;

import com.santanu.jwtAuth.model.PasswordResetRequest;
import com.santanu.jwtAuth.model.RegisterResponse;
import com.santanu.jwtAuth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class UserAccountController {

    private final AuthenticationService authenticationService;

    // Expose endpoint to reset password
    @PutMapping("/resetPassword")
    public ResponseEntity<RegisterResponse> resetPassword(@Valid @RequestBody PasswordResetRequest request, Authentication authentication) {
        return ResponseEntity.ok(authenticationService.updatePassword(request,authentication));
    }
}

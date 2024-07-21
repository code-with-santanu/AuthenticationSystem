package com.santanu.jwtAuth.controller;

import com.santanu.jwtAuth.entity.User;
import com.santanu.jwtAuth.model.CurrentUserInfo;
import com.santanu.jwtAuth.model.PasswordResetRequest;
import com.santanu.jwtAuth.model.RegisterResponse;
import com.santanu.jwtAuth.service.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class UserAccountController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    // Expose endpoint to reset password
    @PutMapping("/resetPassword")
    public ResponseEntity<RegisterResponse> resetPassword(@Valid @RequestBody PasswordResetRequest request, Authentication authentication) {
        return ResponseEntity.ok(authenticationServiceImpl.updatePassword(request,authentication));
    }

    // Expose endpoint to fetch current-user info
    @GetMapping("/getUserInfo")
    public ResponseEntity<CurrentUserInfo> getUserInfo(Authentication authentication) {
        return ResponseEntity.ok(authenticationServiceImpl.getLoggedInSUserInfo(authentication));
    }
}

package com.santanu.jwtAuth.service;

import com.santanu.jwtAuth.entity.User;
import com.santanu.jwtAuth.model.*;
import org.springframework.security.core.Authentication;

import java.nio.file.AccessDeniedException;

public interface AuthenticationService {
    RegisterResponse register(RegisterUser request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void revokeAllTokenByUser(User user);

    void saveUserToken(String tokenValue, User user);

    RegisterResponse updatePassword(PasswordResetRequest request, Authentication authentication);

    String getLoggedInUsername(Authentication authentication) throws AccessDeniedException;

    String getLoggedInPassword(Authentication authentication) throws AccessDeniedException;

    User getLoggedInSUserInfo(Authentication authentication) ;

}

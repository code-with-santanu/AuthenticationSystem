package com.santanu.chatlynk.config;

import com.santanu.chatlynk.entity.Token;
import com.santanu.chatlynk.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class CustomLogouthandler implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        // First Extract the token from the requested header
        String authHeader = request.getHeader("Authorization");

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            return;
        }

        // JWT Token is in the form "Bearer token". Remove Bearer word and get  only the Token
        String token = authHeader.substring(7);

        /* Here Perform 3 steps:
           (1) Get the token from the db
           (2) Mark the token as logout
           (3) Save the updated token
         */

        // (1)
        Token tokenObj = tokenRepository.findByToken(token).orElse(null);

        if(tokenObj !=null) {
            // (2)
            tokenObj.setLoggedOut(true);
            tokenObj.setLoggedOutTime(new Date());

            // (3)
            tokenRepository.save(tokenObj);
        }
    }
}

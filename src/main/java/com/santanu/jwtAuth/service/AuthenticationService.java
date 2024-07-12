package com.santanu.jwtAuth.service;

import com.santanu.jwtAuth.entity.Token;
import com.santanu.jwtAuth.entity.User;
import com.santanu.jwtAuth.model.AuthenticationResponse;
import com.santanu.jwtAuth.model.AuthenticationRequest;
import com.santanu.jwtAuth.model.RegisterResponse;
import com.santanu.jwtAuth.model.RegisterUser;
import com.santanu.jwtAuth.repository.TokenRepository;
import com.santanu.jwtAuth.repository.UserRepository;
import com.santanu.jwtAuth.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class AuthenticationService {
    private  final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterUser request){

        // Checking user existence
        User existUser = userRepository.findByUsername(request.getEmail()).orElse(new User());

        // For already exists user
        if(existUser.getUsername()!=null)
            return (new RegisterResponse(400,"Username Already Exists"));

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        System.out.println("saving");
        user = userRepository.save(user);

//        String token = jwtService.generateToken(user);

        return (new RegisterResponse(200,"Registration Successful"));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        // handle here which token to be generated(accessToken or refreshToken)
        String token = jwtUtils.generateAccessToken(user);

        /* Using Session based mechanism here,
            Only one session will be logged in at once for a user */
        revokeAllTokenByUser(user);  // make all the previous generated token logged out
        saveUserToken(token, user); // saved the generated token to db

        return new AuthenticationResponse(token, "User login was successful");
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokenByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true); // make the user logged out
            t.setLoggedOutTime(new Date());
        });

        tokenRepository.saveAll(validTokens);
    }
    private void saveUserToken(String tokenValue, User user) {
        Token token = new Token();
        token.setToken(tokenValue);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}
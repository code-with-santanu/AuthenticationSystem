package com.santanu.jwtAuth.service;

import com.santanu.jwtAuth.entity.Token;
import com.santanu.jwtAuth.entity.User;
import com.santanu.jwtAuth.model.*;
import com.santanu.jwtAuth.repository.TokenRepository;
import com.santanu.jwtAuth.repository.UserRepository;
import com.santanu.jwtAuth.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService{
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

    public void revokeAllTokenByUser(User user) {
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
    public void saveUserToken(String tokenValue, User user) {
        Token token = new Token();
        token.setToken(tokenValue);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    public RegisterResponse updatePassword(PasswordResetRequest request, Authentication authentication) {

        try{
            String username = getLoggedInUsername(authentication);
            System.out.println("Current logged in username: "+ username);
            String curPassword = getLoggedInPassword(authentication);
            System.out.println("Password received...");

            // Check if username present and given old-password and stored password matches
            if(username!=null && isPasswordMatch(request.getOldPassword(), curPassword) ){
                // Find the user
                User existUser = userRepository.findByUsername(username).orElse(new User());
                // Update the password
                existUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
                // Save the updated user
                userRepository.save(existUser);
                // Forcibly log out all the session the current user
                revokeAllTokenByUser(existUser);


                return (new RegisterResponse(200,"Password is updated Successfully"));
            }
            else {
                throw new RuntimeException("Couldn't update the password");
            }
        }catch (Exception ex){

            return (new RegisterResponse(500,"Couldn't update the password"));
        }
    }

    // Retrieve username of current logged-in user
    public String getLoggedInUsername(Authentication authentication) throws AccessDeniedException {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return ((User) principal).getUsername();
            } else {
                return principal.toString();
                // throw authentication not found error
            }
        }
        else {
            throw new AccessDeniedException("Unauthenticated user");
        }
    }

    // Retrieve password of current logged-in user
    public String getLoggedInPassword(Authentication authentication) throws AccessDeniedException {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return ((User) principal).getPassword();
            } else {
                return principal.toString();
                // throw authentication not found error
            }
        }
        else {
            throw new AccessDeniedException("Unauthenticated user");
        }

    }

    @Override
    public CurrentUserInfo getLoggedInSUserInfo(Authentication authentication){
        CurrentUserInfo userInfo= new CurrentUserInfo();
        try{
            String username = getLoggedInUsername(authentication);
            User user= userRepository.findByUsername(username).orElse(new User());

            userInfo.setFullName(user.getFirstName() + " " + user.getLastName());
            userInfo.setUserName(user.getUsername());
            userInfo.setPassword(user.getPassword());
            userInfo.setRole(user.getRole().name());

            return userInfo;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // check given password and stored password is same or not
    private boolean isPasswordMatch(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword,encodedPassword);
    }
}
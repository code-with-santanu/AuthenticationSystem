package com.santanu.jwtAuth.utils;

import com.santanu.jwtAuth.entity.Role;
import com.santanu.jwtAuth.entity.User;
import com.santanu.jwtAuth.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class JwtUtils {

    // creation of logger
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.access-token-expiration-time}")
    private long jwtAccessTokenExpiration;

    @Value("${security.jwt.access-token-expiration-time}")
    private long jwtRefreshTokenExpiration;

    private final TokenRepository tokenRepository;

    // Retrieve jwt token from the request
    public String getJwtTokenFromHeader(HttpServletRequest request) {

        // retrieve Bearer token from the request header
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}",bearerToken);

        String token=null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get  only the Token
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        logger.debug("Jwt Token Received: {}",token);
        return token;
    }

    // Token generation using username
    public String generateTokenFromUsername(User user, long expirationTime) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("roles", user.getRole().name());
        String token = Jwts
                .builder()
//                .setClaims(claims)  // TO set the other claims (roles) to the jwt-token
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey())
                .compact();

        return token;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    // Extract all-claims from token i.e, subject,issuedAt, expiry,signInKey
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract Specific claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Extract username/email from the claim
    public String extractUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Check the token validity
    public boolean isTokenValid(String token, UserDetails user) {
        final String username = extractUsernameFromToken(token);

        // check if the current token is logged out or not
        boolean isLoggedOut = tokenRepository.findByToken(token)
                .map(t -> !t.isLoggedOut()).orElse(false);

        // return true if (1)username is correct , (2)token is not expired and (3)token is not logged out
        return (username.equals(user.getUsername()) && !isTokenExpired(token) && isLoggedOut);
    }

    // Extract the specific claim(expiration time)
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Check token expiration
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    // method for generating token of validation 24hrs
    public String generateAccessToken(User user){
        return generateTokenFromUsername(user,jwtAccessTokenExpiration);
    }

    // method for generating token of validation 7days
    public String generateRefreshToken(User user){
        return generateTokenFromUsername(user,jwtRefreshTokenExpiration);
    }

}





















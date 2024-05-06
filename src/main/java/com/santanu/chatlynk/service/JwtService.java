package com.santanu.chatlynk.service;

import com.santanu.chatlynk.entity.User;
import com.santanu.chatlynk.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.access-token-expiration-time}")
    private long jwtAccessTokenExpiration;

    @Value("${security.jwt.access-token-expiration-time}")
    private long jwtRefreshTokenExpiration;

    private final TokenRepository tokenRepository;

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
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Check the token validity
    public boolean isTokenValid(String token, UserDetails user) {
        final String username = extractUsername(token);

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
        return generateToken(user,jwtAccessTokenExpiration);
    }

    // method for generating token of validation 7days
    public String generateRefreshToken(User user){
        return generateToken(user,jwtRefreshTokenExpiration);
    }

    // Generating jwt token
    public String generateToken(User user,long expirationTime) {
        String token = Jwts
                .builder()
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
}



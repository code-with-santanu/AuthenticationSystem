package com.santanu.chatlynk.filter;

import com.santanu.chatlynk.service.JwtService;
import com.santanu.chatlynk.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private  final JwtService jwtService;

    private final UserDetailsServiceImpl userDetailService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // JWT Token is in the form "Bearer token". Remove Bearer word and get  only the Token
            String token = authHeader.substring(7);

            String username = jwtService.extractUsername(token); // extract the username

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                /* After setting the Authentication in the context, we specify that the current user is authenticated.
                   So it passes the Spring Security Configurations successfully. */
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("Cannot set the Security Context");
                }

            }
            
        } catch (ExpiredJwtException ex) {
            request.setAttribute("exception", ex);
            throw ex;
        } catch (BadCredentialsException ex) {
            request.setAttribute("exception", ex);
            throw ex;
        }

        filterChain.doFilter(request, response);
    }
}

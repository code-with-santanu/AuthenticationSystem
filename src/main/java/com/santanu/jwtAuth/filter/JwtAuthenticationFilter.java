package com.santanu.jwtAuth.filter;

import com.santanu.jwtAuth.service.UserDetailsServiceImpl;
import com.santanu.jwtAuth.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private  final JwtUtils jwtUtils;

    private final UserDetailsServiceImpl userDetailService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {

            String token = jwtUtils.getJwtTokenFromHeader(request); // step1: Retrieve the jwt token

            if(token != null) {
                String username = jwtUtils.extractUsernameFromToken(token); // step2: extract the username
                logger.debug("Username received {}", username);

                /* step 3: Check if there is no existing authentication in the security context
                   i.e, this username is not yet authenticated to make a request */
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // step 3.1: load the userDetails object
                    UserDetails userDetails = userDetailService.loadUserByUsername(username);

                    // step 3.2: if token is valid, generate UsernamePasswordAuthenticationToken using userDetails and the authorities
                    if (jwtUtils.isTokenValid(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );

                        // step 3.2.1: Adding request-specific details to the authentication token
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        /* step 3.2.2: After setting the Authentication in the context, we specify that the current user is authenticated.
                           So it passes the Spring Security Configurations successfully. */
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        logger.debug("Couldn't set authentication to Security Context...");
                    }

                } else if (SecurityContextHolder.getContext().getAuthentication() != null){
                    logger.debug("User is already authenticated...");
                } else {
                    logger.debug("UserName is received...");
                }
            } else {
                logger.debug("Token is not received...");
            }
            
        }
        // Set the exceptions to the request to handle by AuthenticationEntryPoint
        catch (ExpiredJwtException ex) {
            // Set the exception
            request.setAttribute("exception", ex);

            // Log the exception
            logger.error("ExpiredJwtException exception: {}",ex.getMessage());

        } catch (BadCredentialsException ex) {
            // Set the exception
            request.setAttribute("exception", ex);

            // Log the exception
            logger.error("BadCredentialsException exception: {}",ex.getMessage());
        }

        // Now continue the filter-chain
        filterChain.doFilter(request, response);
    }
}

package com.santanu.jwtAuth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

/* Custom Handler for unauthorized request
   i.e, This class is used to return a 401 unauthorized error to clients that try to access a protected resource without proper authentication. */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationEntryPoint.class);
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Log the exception
        logger.error("Unauthorized error: {}",authException.getMessage());

        // Step 1: Set the response
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String message;
        // Step 2: Check if the request has any exception that we have stored in Request from the auth-filter
        final Exception exception = (Exception) request.getAttribute("exception");

        // Step 2.1: If yes then use it to create the response message else use the authException
        if (exception != null) {

            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("cause", exception.toString()));
            response.getOutputStream().write(body); // write the response body and return to the client
        }else {
            if (authException.getCause() != null) {
                message = authException.getCause().toString() + " " + authException.getMessage();
            } else {
                message = authException.getMessage();
            }

            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));
            response.getOutputStream().write(body); // write the response body and return to the client
        }
    }


}

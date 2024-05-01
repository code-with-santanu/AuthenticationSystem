package com.santanu.chatlynk.config;


import com.santanu.chatlynk.entity.Role;
import com.santanu.chatlynk.filter.JwtAuthenticationFilter;
import com.santanu.chatlynk.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private  final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Login success handler object
    private final LoginSuccessHandler loginSuccessHandler;


    @Value("${CLIENT_URL}")
    private String client_url;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf->csrf.disable())
                .cors(cors-> cors.configurationSource(corsConfigurationSource())) // handling cors policy
                .authorizeHttpRequests(req->req
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/adminOnly").hasAuthority(Role.ADMIN.name())
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage(client_url+"/auth") // custom login form for security
                        .successHandler(loginSuccessHandler) // Redirect to this url on successful authentication using jwt auth
                )
                .userDetailsService(userDetailsServiceImpl) // adding custom userDetailsService to filter-chain
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // making the app stateless i.e, no session & cookies is created by spring
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add the jwt filter before any security filter


        return  http.build();

    }

    // Implementing cors configuration for frontend client url
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(client_url));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // max-time limit that option request is accepted

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        // applying specified property to all the requests
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",configuration);

        return urlBasedCorsConfigurationSource;

    }

}

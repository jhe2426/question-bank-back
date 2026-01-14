package com.jhe.question_bank.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jhe.question_bank.common.constant.RequestPattern;
import com.jhe.question_bank.filter.JwtAuthenticationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity security) throws Exception {
        
        security
            .httpBasic(HttpBasicConfigurer::disable)
            .sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .csrf(CsrfConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(request -> request
               .requestMatchers(RequestPattern.AUTH_API, RequestPattern.AUTH_API + "/**").permitAll()
               .anyRequest().authenticated() 
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new AuthenticationFailEntryPoint())
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        ArrayList<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("https://localhost:3000");
        configuration.setAllowedOrigins(allowedOriginPatterns);

        ArrayList<String> allowedHttpMethods = new ArrayList<>();
        allowedHttpMethods.add("GET");
        allowedHttpMethods.add("POST");
        allowedHttpMethods.add("PUT");
        allowedHttpMethods.add("DELETE");
        configuration.setAllowedMethods(allowedHttpMethods);

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

}

class AuthenticationFailEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        authException.printStackTrace();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{ \"code\": \"AF\", \"message\": \"Authentication failed.\" }");
        
    }

}
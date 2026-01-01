package com.ace.productservice.config;

import jakarta.ws.rs.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // 👀 Consultation produits → CLIENT & ADMIN
                        .requestMatchers(HttpMethod.GET, "/products/**", "/images/**")
                        .permitAll()

                        // 🔒 ADMIN SEULEMENT
                        .requestMatchers(HttpMethod.POST, "/products/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/products/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/products/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(); // temporaire (JWT après)

        return http.build();
    }
}


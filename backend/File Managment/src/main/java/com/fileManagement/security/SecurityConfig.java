package com.fileManagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/code/**")
                        .ignoringRequestMatchers("/comment/**")
                        .ignoringRequestMatchers("/project/**")
                        .ignoringRequestMatchers("/log/**")

                )
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("http://localhost:8080/**").permitAll()
                        .requestMatchers("/code/**").permitAll()
                        .requestMatchers("/comment/**").permitAll()
                        .requestMatchers("/project/**").permitAll()
                        .requestMatchers("/log/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }


    }



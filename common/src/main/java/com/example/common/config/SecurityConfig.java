package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
     SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .cors(Customizer.withDefaults())         
          .csrf(csrf -> csrf.disable())            
          .authorizeHttpRequests(auth -> auth
              .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/healthz",
                "/actuator/**"
              ).permitAll()
              .anyRequest().authenticated()
          );
        return http.build();
    }

    @Bean
     CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("*"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Authorization","Content-Type"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
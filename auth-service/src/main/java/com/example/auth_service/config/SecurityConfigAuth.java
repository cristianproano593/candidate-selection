package com.example.auth_service.config;

import com.example.auth_service.repository.AuthRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Collections;
import java.util.List;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableWebSecurity
public class SecurityConfigAuth {


    private final AuthRepository userRepo;

    public SecurityConfigAuth(
                              AuthRepository userRepo) {
 
        this.userRepo = userRepo;
    }

    @Bean
     UserDetailsService userDetailsService() {
        return username -> userRepo.findByUsername(username)
            .map(u -> (UserDetails) org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(Collections.emptyList())
                .accountLocked(!u.getEnabled())
                .build()
            )
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
     DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider prov = new DaoAuthenticationProvider(userDetailsService());
     
        prov.setPasswordEncoder(passwordEncoder());
        return prov;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/auth/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/healthz",
                    "/actuator/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider());
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
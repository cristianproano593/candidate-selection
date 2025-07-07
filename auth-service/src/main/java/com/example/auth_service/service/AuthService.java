package com.example.auth_service.service;


import com.example.auth_service.config.JwtTokenProvider;
import com.example.auth_service.exception.InvalidCredentialsException;
import com.example.auth_service.exception.UserNotFoundException;
import com.example.auth_service.model.User;
import com.example.auth_service.repository.AuthRepository;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final AuthRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authManager,
                       JwtTokenProvider tokenProvider,
                       AuthRepository userRepo,
                       PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.tokenProvider = tokenProvider;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String username, String password) {
       
    	Optional<User> optUser = userRepo.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException("Usuario no encontrado");
        }
        User u = optUser.get();
        System.out.println("DEBUG: Hash almacenado para '" + username + "': " + u.getPassword());

        
        boolean matches = passwordEncoder.matches(password, u.getPassword());
        System.out.println("DEBUG: passwordEncoder.matches = " + matches);

      
        if (!matches) {
            throw new InvalidCredentialsException("La contraseña no coincide (match=false)");
        }

       
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

     
        return tokenProvider.createToken(username);
    }
}
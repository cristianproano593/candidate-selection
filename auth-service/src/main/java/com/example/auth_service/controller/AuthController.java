package com.example.auth_service.controller;

import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.LoginResponse;
import com.example.auth_service.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Service", description = "Endpoints de autenticación y autorización")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest req
    ) {
        String token = authService.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(new LoginResponse(token, 3600000));
    }

  
}


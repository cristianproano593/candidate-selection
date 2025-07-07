package com.example.auth_service.service;

import com.example.auth_service.config.JwtTokenProvider;
import com.example.auth_service.exception.InvalidCredentialsException;
import com.example.auth_service.exception.UserNotFoundException;
import com.example.auth_service.model.User;
import com.example.auth_service.repository.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService service;

    private final String username = "admin";
    private final String rawPassword = "secret";
    private final String hashedPassword = "$2a$10$HLCU7Azr7j2myu4DKV4tru1IOB0mFf094QN8Cn3ct2FNKfHSbWBfi";

    @BeforeEach
    void setUp() {
        // por si queremos reusar en varios tests
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsValid() {
        // 1) usuario existe en BDD
        User u = new User();
        u.setUsername(username);
        u.setPassword(hashedPassword);
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(u));

        // 2) el encoder acepta la contraseña
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);

        // 3) simulamos que authenticate(...) devuelve un Authentication válido
        Authentication auth = new UsernamePasswordAuthenticationToken(
            username, null, Collections.emptyList()
        );
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(auth);

        // 4) simulamos la creación de token
        String expectedToken = "jwt-xyz";
        when(tokenProvider.createToken(username)).thenReturn(expectedToken);

        // 5) ejecutamos
        String actual = service.login(username, rawPassword);

        assertEquals(expectedToken, actual);

        // verificamos que authenticate se llamó con el token correcto
        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
            ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authManager).authenticate(captor.capture());
        assertEquals(username, captor.getValue().getPrincipal());
        assertEquals(rawPassword, captor.getValue().getCredentials());
    }

    @Test
    void login_ShouldThrowUserNotFound_WhenUsernameInvalid() {
        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(
            UserNotFoundException.class,
            () -> service.login(username, rawPassword)
        );
        assertTrue(ex.getMessage().contains("Usuario no encontrado"));
    }

    @Test
    void login_ShouldThrowInvalidCredentials_WhenPasswordWrong() {
        User u = new User();
        u.setUsername(username);
        u.setPassword(hashedPassword);
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(u));

        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false);

        InvalidCredentialsException ex = assertThrows(
            InvalidCredentialsException.class,
            () -> service.login(username, rawPassword)
        );
        assertTrue(ex.getMessage().contains("match=false"));
        // no debe haber llamado a authManager ni a tokenProvider
        verify(authManager, never()).authenticate(any());
        verify(tokenProvider, never()).createToken(anyString());
    }

    @Test
    void login_ShouldPropagateBadCredentialsException() {
        // suponemos que el passwordEncoder devuelve true
        User u = new User();
        u.setUsername(username);
        u.setPassword(hashedPassword);
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(u));
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);

        // pero authManager arroja BadCredentialsException
        when(authManager.authenticate(any()))
            .thenThrow(new org.springframework.security.authentication.BadCredentialsException("bad"));

        assertThrows(
            org.springframework.security.authentication.BadCredentialsException.class,
            () -> service.login(username, rawPassword)
        );
        // tokenProvider no debe ser invocado
        verify(tokenProvider, never()).createToken(anyString());
    }
}

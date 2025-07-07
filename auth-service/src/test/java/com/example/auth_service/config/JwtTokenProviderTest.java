package com.example.auth_service.config;
import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class JwtTokenProviderTest {

    private JwtTokenProvider provider;
    // Usamos 1 hora de validez para que los tests de creación/validación pasen sin esperas
    private final String secret = "my-very-secret-key-which-should-be-at-least-256-bits-long";
    private final long longValidityMs = 3_600_000L; // 1 hora

    @BeforeEach
    void setUp() {
        provider = new JwtTokenProvider(secret, longValidityMs);
    }

    @Test
    void createToken_ShouldContainUsername() {
        String username = "test-user";
        String token = provider.createToken(username);

        assertNotNull(token, "El token no debe ser null");
        assertEquals(username, provider.getUsername(token),
                     "El username extraído debe coincidir");
    }

    @Test
    void validateToken_ShouldReturnTrue_ForValidToken() {
        String token = provider.createToken("user1");
        assertTrue(provider.validateToken(token),
                   "Un token recién creado debe ser válido");
    }

    @Test
    void validateToken_ShouldReturnFalse_ForMalformedToken() {
        String badToken = "this.is.not.a.jwt";
        assertFalse(provider.validateToken(badToken),
                    "Un token malformado debe invalidarse");
    }

    @Test
    void getUsername_ShouldThrow_ForInvalidToken() {
        assertThrows(MalformedJwtException.class,
                     () -> provider.getUsername("bad.token"),
                     "Extraer username de token inválido debe fallar");
    }

    @Test
    void validateToken_ShouldReturnFalse_WhenExpired() throws InterruptedException {
        // Para este caso creamos un provider con expiración muy corta
        JwtTokenProvider shortLivedProvider = new JwtTokenProvider(secret, 500L);
        String token = shortLivedProvider.createToken("exp-user");
        TimeUnit.MILLISECONDS.sleep(600);
        assertFalse(shortLivedProvider.validateToken(token),
                    "Token vencido debe invalidarse");
    }

    @Test
    void getClaims_ShouldContainIssuedAtAndExpiration() {
        String token = provider.createToken("demo");
        Claims claims = provider.getClaims(token);

        assertNotNull(claims.getIssuedAt(), "Debe tener fecha de emisión");
        assertNotNull(claims.getExpiration(), "Debe tener fecha de expiración");
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()),
                   "Expiración debe ser después de emisión");
    }

    @Test
    void validateToken_ShouldThrowExpiredJwtException_WhenParsingExpired() throws InterruptedException {
        JwtTokenProvider shortLivedProvider = new JwtTokenProvider(secret, 200L);
        String token = shortLivedProvider.createToken("exp2");
        TimeUnit.MILLISECONDS.sleep(300);
        assertThrows(ExpiredJwtException.class,
                     () -> shortLivedProvider.getClaims(token),
                     "Acceder a claims de token expirado debe lanzar ExpiredJwtException");
    }
}

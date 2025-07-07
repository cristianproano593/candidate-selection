package com.example.customer_service.config;


import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtDecoderConfig {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
     JwtDecoder jwtDecoder() {
  
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);

        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }
}

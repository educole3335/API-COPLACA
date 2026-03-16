package com.coplaca.apirest.security;

import com.coplaca.apirest.config.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        AppProperties properties = new AppProperties();
        properties.setJwtSecret("CoplacaJwtSecretKey2026CoplacaJwtSecretKey2026ExtraLongForTestsSecureEnough1234");
        properties.setJwtExpirationMs(60_000);
        jwtTokenProvider = new JwtTokenProvider(properties);
    }

    @Test
    void generateTokenFromEmailAndExtractEmail() {
        String token = jwtTokenProvider.generateTokenFromEmail("cliente@coplaca.com");

        String email = jwtTokenProvider.getEmailFromJWT(token);

        assertEquals("cliente@coplaca.com", email);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void generateTokenFromAuthentication() {
        User principal = new User("logistica@coplaca.com", "pwd", List.of());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        String token = jwtTokenProvider.generateToken(authentication);

        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals("logistica@coplaca.com", jwtTokenProvider.getEmailFromJWT(token));
    }

    @Test
    void validateTokenReturnsFalseForInvalidToken() {
        assertFalse(jwtTokenProvider.validateToken("token-invalido"));
    }
}

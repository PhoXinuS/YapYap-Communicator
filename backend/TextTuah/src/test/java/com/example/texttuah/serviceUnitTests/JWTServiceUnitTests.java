package com.example.texttuah.serviceUnitTests;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.texttuah.service.JWTService;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceUnitTests {

    private JWTService jwtService;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        jwtService = new JWTService();
        ReflectionTestUtils.setField(jwtService, "expire_time", 60);
        ReflectionTestUtils.invokeMethod(jwtService, "initialize_expire_time");
    }

    @Test
    void generateToken_ValidInput() {
        String email = "test@example.com";
        String token = jwtService.generateToken(email);
        assertNotNull(token);

        SecretKey secretKey = ReflectionTestUtils.invokeMethod(jwtService, "getKey");
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals(email, claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }

    @Test
    void generateToken_CheckExpirationTime() {
        String email = "test@example.com";
        String token = jwtService.generateToken(email);

        SecretKey secretKey = ReflectionTestUtils.invokeMethod(jwtService, "getKey");
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();

        assertEquals(60 * 60 * 1000, expiration.getTime() - issuedAt.getTime());
    }
}

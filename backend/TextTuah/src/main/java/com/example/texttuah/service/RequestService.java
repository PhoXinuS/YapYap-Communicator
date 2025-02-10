package com.example.texttuah.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    public String getJwtToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = null;
        if (header != null && header.startsWith("Bearer ")) {
            // JWT token like : Bearer [token]
            // So the token starts from 7th index
            token = header.substring(7);
        }
        return token;
    }
}

package com.example.texttuah.serviceUnitTests;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import com.example.texttuah.service.RequestService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestServiceUnitTests {

    private final RequestService requestService = new RequestService();

    @Test
    void getJwtToken_Valid() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validJwtToken");

        String token = requestService.getJwtToken(request);

        assertEquals("validJwtToken", token);
        verify(request, times(1)).getHeader("Authorization");
    }

    @Test
    void getJwtToken_NullReturn() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        String token = requestService.getJwtToken(request);

        assertNull(token);
        verify(request, times(1)).getHeader("Authorization");
    }

    @Test
    void getJwtToken_InvalidHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader validJwtToken");

        String token = requestService.getJwtToken(request);

        assertNull(token);
        verify(request, times(1)).getHeader("Authorization");
    }
}

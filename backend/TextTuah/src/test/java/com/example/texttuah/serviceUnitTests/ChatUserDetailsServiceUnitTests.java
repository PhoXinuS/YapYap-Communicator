package com.example.texttuah.serviceUnitTests;

import com.example.texttuah.entity.ChatUser;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.texttuah.service.ChatUserDetailsService;
import com.example.texttuah.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatUserDetailsServiceUnitTests {

    @Test
    void loadUserByUsername_ExistingUser() {
        UserService mockUserService = mock(UserService.class);
        ChatUserDetailsService userDetailsService = new ChatUserDetailsService(mockUserService);
        String email = "test@example.com";
        ChatUser mockUser = new ChatUser();
        mockUser.setEmail(email);
        mockUser.setPassword("password123");
        when(mockUserService.findByEmail(email)).thenReturn(mockUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        verify(mockUserService, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_ShouldThrowException() {
        UserService mockUserService = mock(UserService.class);
        ChatUserDetailsService userDetailsService = new ChatUserDetailsService(mockUserService);
        String email = "nonexistent@example.com";
        when(mockUserService.findByEmail(email)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
        verify(mockUserService, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_NullEmail() {
        UserService mockUserService = mock(UserService.class);
        ChatUserDetailsService userDetailsService = new ChatUserDetailsService(mockUserService);

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(null));
    }

    @Test
    void loadUserByUsername_EmptyEmail() {
        UserService mockUserService = mock(UserService.class);
        ChatUserDetailsService userDetailsService = new ChatUserDetailsService(mockUserService);
        String email = "";

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
    }

    @Test
    void loadUserByUsername_BlankEmail() {
        UserService mockUserService = mock(UserService.class);
        ChatUserDetailsService userDetailsService = new ChatUserDetailsService(mockUserService);
        String email = " ";

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
    }
}
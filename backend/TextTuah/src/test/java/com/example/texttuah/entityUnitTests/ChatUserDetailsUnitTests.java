package com.example.texttuah.entityUnitTests;

import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.ChatUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ChatUserDetailsUnitTests {

    @Mock
    private ChatUser chatUser;

    private ChatUserDetails chatUserDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatUserDetails = new ChatUserDetails(chatUser);
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = chatUserDetails.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testGetPassword() {
        when(chatUser.getPassword()).thenReturn("password");
        String password = chatUserDetails.getPassword();

        assertEquals("password", password);
    }

    @Test
    void testGetUsername() {
        when(chatUser.getEmail()).thenReturn("user@example.com");
        String username = chatUserDetails.getUsername();

        assertEquals("user@example.com", username);
    }

    @Test
    void testIsAccountNonExpired() {
        boolean isAccountNonExpired = chatUserDetails.isAccountNonExpired();

        assertTrue(isAccountNonExpired);
    }

    @Test
    void testIsAccountNonLocked() {
        boolean isAccountNonLocked = chatUserDetails.isAccountNonLocked();

        assertTrue(isAccountNonLocked);
    }

    @Test
    void testIsCredentialsNonExpired() {
        boolean isCredentialsNonExpired = chatUserDetails.isCredentialsNonExpired();

        assertTrue(isCredentialsNonExpired);
    }

    @Test
    void testIsEnabled() {
        boolean isEnabled = chatUserDetails.isEnabled();

        assertTrue(isEnabled);
    }
}
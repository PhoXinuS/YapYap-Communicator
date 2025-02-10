package com.example.texttuah.serviceUnitTests;


import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.repository.UserRepository;
import com.example.texttuah.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findByEmail_ExistingUser() {
        ChatUser mockUser = new ChatUser();
        mockUser.setEmail("test@example.com");
        mockUser.setName("Kamil Zdun");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        ChatUser result = userService.findByEmail("test@example.com");
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Kamil Zdun", result.getName());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void findByEmail_NonExistingUser() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        ChatUser result = userService.findByEmail("nonexistent@example.com");
        assertNull(result);
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void findByEmail_NullInput() {
        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());
        ChatUser result = userService.findByEmail(null);
        assertNull(result);
        verify(userRepository, times(1)).findByEmail(null);
    }

    @Test
    void findById_ExistingUser() {
        ChatUser mockUser = new ChatUser();
        mockUser.setId(1L);
        mockUser.setName("Kamil Zdun");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        ChatUser result = userService.findById(1L);
        assertEquals(1L, result.getId());
        assertEquals("Kamil Zdun", result.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NonExistingUser() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        ChatUser result = userService.findById(2L);
        assertNull(result);
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void findById_NullInput() {
        when(userRepository.findById(-1L)).thenReturn(Optional.empty());
        ChatUser result = userService.findById(-1L);
        assertNull(result);
        verify(userRepository, times(1)).findById(-1L);
    }

    @Test
    void findByName_ListOfUsers() {
        ChatUser user1 = new ChatUser();
        user1.setId(1L);
        user1.setName("Grzegorz");
        ChatUser user2 = new ChatUser();
        user2.setId(2L);
        user2.setName("Grzegorz");
        List<ChatUser> mockUsers = List.of(user1, user2);
        when(userRepository.findByName("Grzegorz")).thenReturn(Optional.of(mockUsers));

        List<ChatUser> result = userService.findByName("Grzegorz");
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Grzegorz", result.get(0).getName());
        assertEquals("Grzegorz", result.get(1).getName());
        verify(userRepository, times(1)).findByName("Grzegorz");
    }

    @Test
    void findByName_NonExistingUser() {
        when(userRepository.findByName("Grzegorz")).thenReturn(Optional.empty());
        List<ChatUser> result = userService.findByName("Grzegorz");
        assertNull(result);
        verify(userRepository, times(1)).findByName("Grzegorz");
    }

    @Test
    void findByName_EmptyInput() {
        when(userRepository.findByName("")).thenReturn(Optional.of(Collections.emptyList()));
        List<ChatUser> result = userService.findByName("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByName("");
    }

    @Test
    void findByName_NullInput() {
        when(userRepository.findByName(null)).thenReturn(Optional.empty());
        List<ChatUser> result = userService.findByName(null);
        assertNull(result);
        verify(userRepository, times(1)).findByName(null);
    }

    // find all friends

    // save

    // verify
}

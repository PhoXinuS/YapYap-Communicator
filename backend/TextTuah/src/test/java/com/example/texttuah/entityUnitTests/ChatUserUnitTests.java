package com.example.texttuah.entityUnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import com.example.texttuah.entity.ChatUser;

import static org.junit.jupiter.api.Assertions.*;

class ChatUserUnitTests {

    private ChatUser chatUser;

    @BeforeEach
    void setUp() {
        chatUser = new ChatUser();
        chatUser.setId(1L);
        chatUser.setEmail("test@example.com");
        chatUser.setName("Test User");
        chatUser.setPassword("password123");
        chatUser.setJoinedAt(LocalDateTime.now());
    }

    @Test
    void gettersAndSettersTest() {
        assertEquals(1, chatUser.getId());
        assertEquals("test@example.com", chatUser.getEmail());
        assertEquals("Test User", chatUser.getName());
        assertEquals("password123", chatUser.getPassword());
        assertNotNull(chatUser.getJoinedAt());

        chatUser.setId(2L);
        chatUser.setEmail("new@example.com");
        chatUser.setName("New User");
        chatUser.setPassword("newpassword123");
        chatUser.setJoinedAt(LocalDateTime.of(2022, 1, 1, 12, 0));

        assertEquals(2, chatUser.getId());
        assertEquals("new@example.com", chatUser.getEmail());
        assertEquals("New User", chatUser.getName());
        assertEquals("newpassword123", chatUser.getPassword());
        assertEquals(LocalDateTime.of(2022, 1, 1, 12, 0), chatUser.getJoinedAt());
    }

    @Test
    void constructorTest() {
        ChatUser newUser = new ChatUser(3L, "constructor@example.com", "Constructor User", "password123", LocalDateTime.of(2023, 5, 15, 10, 30));

        assertEquals(3, newUser.getId());
        assertEquals("constructor@example.com", newUser.getEmail());
        assertEquals("Constructor User", newUser.getName());
        assertEquals("password123", newUser.getPassword());
        assertEquals(LocalDateTime.of(2023, 5, 15, 10, 30), newUser.getJoinedAt());
    }
}

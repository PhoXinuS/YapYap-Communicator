package com.example.texttuah.dtoUnitTests;

import com.example.texttuah.entity.ChatUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.texttuah.dto.UserFriendDTO;

import static org.junit.jupiter.api.Assertions.*;

class UserFriendDTOUnitTests {

    private UserFriendDTO userFriendDTO;

    @BeforeEach
    void setUp() {
        userFriendDTO = new UserFriendDTO("Kamil Zdun", "kamilzdun@example.com");
    }

    @Test
    void constructorAndGettersTest() {
        assertEquals("Kamil Zdun", userFriendDTO.getName());
        assertEquals("kamilzdun@example.com", userFriendDTO.getEmail());
    }

    @Test
    void settersTest() {
        userFriendDTO.setName("Damian Wasik");
        userFriendDTO.setEmail("damianwasik@example.com");

        assertEquals("Damian Wasik", userFriendDTO.getName());
        assertEquals("damianwasik@example.com", userFriendDTO.getEmail());
    }

    @Test
    void convertTo_NormalInput() {
        ChatUser chatUser = new ChatUser(3L, "user@example.com", "User", "password", null);

        UserFriendDTO convertedDTO = UserFriendDTO.convertTo(chatUser);

        assertEquals(chatUser.getName(), convertedDTO.getName());
        assertEquals(chatUser.getEmail(), convertedDTO.getEmail());
    }

    @Test
    void convertTo_NullsInChatUser() {
        ChatUser chatUser = new ChatUser();
        chatUser.setId(4L);
        chatUser.setName(null);
        chatUser.setEmail(null);

        UserFriendDTO convertedDTO = UserFriendDTO.convertTo(chatUser);

        assertNull(convertedDTO.getName());
        assertNull(convertedDTO.getEmail());
    }
}
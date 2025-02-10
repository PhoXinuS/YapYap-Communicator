package com.example.texttuah.dtoUnitTests;

import com.example.texttuah.dto.UserDetailsDTO;
import com.example.texttuah.entity.ChatUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsDTOUnitTests {

    private ChatUser chatUser;
    private UserDetailsDTO userDetailsDTO;

    @BeforeEach
    void setUp() {
        chatUser = new ChatUser();
        chatUser.setId(1L);
        chatUser.setName("Test User");
        chatUser.setEmail("test@example.com");
        chatUser.setPassword("password");

        userDetailsDTO = new UserDetailsDTO("Test User", "test@example.com", 1L);
    }

    @Test
    void testUserDetailsDTOCreation() {
        assertNotNull(userDetailsDTO);
        assertEquals("Test User", userDetailsDTO.getName());
        assertEquals("test@example.com", userDetailsDTO.getEmail());
        assertEquals(1L, userDetailsDTO.getId());
    }

    @Test
    void testConvertTo() {
        UserDetailsDTO dto = UserDetailsDTO.convertTo(chatUser);

        assertNotNull(dto);
        assertEquals(chatUser.getName(), dto.getName());
        assertEquals(chatUser.getEmail(), dto.getEmail());
        assertEquals(chatUser.getId(), dto.getId());
    }

    @Test
    void testSettersAndGetters() {
        userDetailsDTO.setName("New Name");
        userDetailsDTO.setEmail("new@example.com");
        userDetailsDTO.setId(2L);

        assertEquals("New Name", userDetailsDTO.getName());
        assertEquals("new@example.com", userDetailsDTO.getEmail());
        assertEquals(2L, userDetailsDTO.getId());
    }

    @Test
    void testNoArgsConstructor() {
        UserDetailsDTO emptyDTO = new UserDetailsDTO();
        assertNotNull(emptyDTO);
        assertNull(emptyDTO.getName());
        assertNull(emptyDTO.getEmail());
        assertNull(emptyDTO.getId());
    }
}
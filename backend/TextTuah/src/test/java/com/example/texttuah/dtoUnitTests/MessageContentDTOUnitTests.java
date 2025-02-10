package com.example.texttuah.dtoUnitTests;

import com.example.texttuah.dto.MessageContentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageContentDTOUnitTests {

    private MessageContentDTO messageContentDTO;

    @BeforeEach
    void setUp() {
        messageContentDTO = new MessageContentDTO("Test content");
    }

    @Test
    void testMessageContentDTOCreation() {
        assertNotNull(messageContentDTO);
        assertEquals("Test content", messageContentDTO.getContent());
    }

    @Test
    void testSettersAndGetters() {
        messageContentDTO.setContent("New content");
        assertEquals("New content", messageContentDTO.getContent());
    }

    @Test
    void testNoArgsConstructor() {
        MessageContentDTO emptyDTO = new MessageContentDTO();
        assertNotNull(emptyDTO);
        assertNull(emptyDTO.getContent());
    }
}
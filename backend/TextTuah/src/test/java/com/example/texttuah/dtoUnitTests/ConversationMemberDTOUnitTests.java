package com.example.texttuah.dtoUnitTests;

import com.example.texttuah.dto.ConversationMemberDTO;
import com.example.texttuah.entity.ChatUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ConversationMemberDTOUnitTests {

    @Mock
    private ChatUser chatUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertTo() {
        when(chatUser.getName()).thenReturn("Test User");

        ConversationMemberDTO conversationMemberDTO = ConversationMemberDTO.convertTo(chatUser);

        assertEquals("Test User", conversationMemberDTO.getName());
    }

    @Test
    void testGettersAndSetters() {
        ConversationMemberDTO conversationMemberDTO = new ConversationMemberDTO();
        conversationMemberDTO.setName("Test User");

        assertEquals("Test User", conversationMemberDTO.getName());
    }
}
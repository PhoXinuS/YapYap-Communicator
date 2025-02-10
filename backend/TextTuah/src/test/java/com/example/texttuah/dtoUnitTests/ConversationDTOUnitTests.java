package com.example.texttuah.dtoUnitTests;

import com.example.texttuah.dto.ConversationDTO;
import com.example.texttuah.dto.ConversationMemberDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConversationDTOUnitTests {

    @Mock
    private ConversationMemberDTO conversationMemberDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGettersAndSetters() {
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setId(1L);
        conversationDTO.setName("Test Conversation");
        conversationDTO.setMembers(Stream.of(conversationMemberDTO).collect(Collectors.toSet()));

        assertEquals(1L, conversationDTO.getId());
        assertEquals("Test Conversation", conversationDTO.getName());
        assertEquals(Collections.singleton(conversationMemberDTO), conversationDTO.getMembers());
    }
}
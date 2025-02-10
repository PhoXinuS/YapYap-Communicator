package com.example.texttuah.dtoUnitTests;

import com.example.texttuah.dto.ChatMessageDTO;
import com.example.texttuah.entity.ChatMessage;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.MessageLike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChatMessageDTOUnitTests {

    private ChatMessage chatMessage;
    private ChatUser liker;

    @BeforeEach
    void setUp() {
        ChatUser sender = new ChatUser();
        sender.setId(1L);
        sender.setEmail("sender@example.com");
        sender.setName("Sender");
        sender.setPassword("password");
        sender.setJoinedAt(LocalDateTime.now());

        liker = new ChatUser();
        liker.setId(2L);
        liker.setEmail("liker@example.com");
        liker.setName("Liker");
        liker.setPassword("password");
        liker.setJoinedAt(LocalDateTime.now());

        chatMessage = new ChatMessage();
        chatMessage.setId(1L);
        chatMessage.setSender(sender);
        chatMessage.setContent("Hello, world!");
        chatMessage.setSentAt(LocalDateTime.now());

        Set<MessageLike> likes = new HashSet<>();
        MessageLike like = new MessageLike(chatMessage, liker);
        likes.add(like);
        chatMessage.setLikes(likes);
    }

    @Test
    void testConvertToDTO() {
        ChatMessageDTO dto = ChatMessageDTO.convertTo(chatMessage, liker);

        assertNotNull(dto);
        assertEquals(chatMessage.getId(), dto.getId());
        assertEquals(chatMessage.getContent(), dto.getContent());
        assertEquals(chatMessage.getSender().getName(), dto.getSenderName());
        assertEquals(chatMessage.getSentAt(), dto.getSentAt());
        assertEquals(chatMessage.getLikes().size(), dto.getNumberOfLikes());
        assertTrue(dto.isLiked());
    }

    @Test
    void testConvertToDTOSet() {
        Set<ChatMessage> messages = new HashSet<>();
        messages.add(chatMessage);

        Set<ChatMessageDTO> dtos = ChatMessageDTO.convertTo(messages, liker);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        ChatMessageDTO dto = dtos.iterator().next();
        assertEquals(chatMessage.getId(), dto.getId());
        assertEquals(chatMessage.getContent(), dto.getContent());
        assertEquals(chatMessage.getSender().getName(), dto.getSenderName());
        assertEquals(chatMessage.getSentAt(), dto.getSentAt());
        assertEquals(chatMessage.getLikes().size(), dto.getNumberOfLikes());
        assertTrue(dto.isLiked());
    }

    @Test
    void testEqualsAndHashCode() {
        ChatMessageDTO dto1 = ChatMessageDTO.convertTo(chatMessage, liker);
        ChatMessageDTO dto2 = ChatMessageDTO.convertTo(chatMessage, liker);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testSettersAndGetters() {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(1L);
        dto.setContent("Test content");
        dto.setSenderName("Test sender");
        dto.setSentAt(LocalDateTime.now());
        dto.setNumberOfLikes(5);
        dto.setLiked(true);

        assertEquals(1L, dto.getId());
        assertEquals("Test content", dto.getContent());
        assertEquals("Test sender", dto.getSenderName());
        assertNotNull(dto.getSentAt());
        assertEquals(5, dto.getNumberOfLikes());
        assertTrue(dto.isLiked());
    }
}
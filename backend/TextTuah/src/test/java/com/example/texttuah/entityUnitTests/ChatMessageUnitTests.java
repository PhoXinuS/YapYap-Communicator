package com.example.texttuah.entityUnitTests;

import com.example.texttuah.entity.ChatMessage;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Conversation;
import com.example.texttuah.entity.MessageLike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ChatMessageUnitTests {

    private ChatMessage chatMessage;
    private ChatUser sender;
    private Conversation conversation;
    private Set<MessageLike> likes;

    @BeforeEach
    void setUp() {
        sender = new ChatUser();
        sender.setId(1L);
        sender.setEmail("sender@example.com");
        sender.setName("Sender");
        sender.setPassword("password");
        sender.setJoinedAt(LocalDateTime.now());

        conversation = new Conversation();
        conversation.setId(1L);
        conversation.setName("Test Conversation");
        conversation.setCreatedAt(LocalDateTime.now());

        likes = new HashSet<>();

        chatMessage = new ChatMessage();
        chatMessage.setId(1L);
        chatMessage.setSender(sender);
        chatMessage.setConversation(conversation);
        chatMessage.setLikes(likes);
        chatMessage.setContent("Hello, world!");
        chatMessage.setSentAt(LocalDateTime.now());
    }

    @Test
    void testChatMessageCreation() {
        assertNotNull(chatMessage);
        assertEquals(1L, chatMessage.getId());
        assertEquals(sender, chatMessage.getSender());
        assertEquals(conversation, chatMessage.getConversation());
        assertEquals(likes, chatMessage.getLikes());
        assertEquals("Hello, world!", chatMessage.getContent());
        assertNotNull(chatMessage.getSentAt());
    }

    @Test
    void testAddLike() {
        ChatUser liker = new ChatUser(1L, "test@example.com", "Test", "password", LocalDateTime.now());
        MessageLike like = new MessageLike(chatMessage, liker);

        assertTrue(chatMessage.addLike(like));
        assertEquals(1, chatMessage.getLikes().size());
        assertTrue(chatMessage.getLikes().contains(like));
    }

    @Test
    void testEqualsAndHashCode() {
        ChatMessage anotherMessage = new ChatMessage();
        anotherMessage.setId(1L);

        assertEquals(chatMessage, anotherMessage);
        assertEquals(chatMessage.hashCode(), anotherMessage.hashCode());
    }

    @Test
    void testPrePersist() {
        ChatMessage newMessage = new ChatMessage();
        newMessage.prePersist();
        assertNotNull(newMessage.getSentAt());
    }
}
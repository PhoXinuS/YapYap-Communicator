package com.example.texttuah.entityUnitTests;

import com.example.texttuah.entity.ChatMessage;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.MessageLike;
import com.example.texttuah.embeddable.MessageLikeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MessageLikeUnitTests {

    private MessageLike messageLike;
    private ChatMessage chatMessage;
    private ChatUser liker;

    @BeforeEach
    void setUp() {
        liker = new ChatUser();
        liker.setId(1L);
        liker.setEmail("liker@example.com");
        liker.setName("Liker");
        liker.setPassword("password");
        liker.setJoinedAt(LocalDateTime.now());

        chatMessage = new ChatMessage();
        chatMessage.setId(1L);
        chatMessage.setSender(liker);
        chatMessage.setContent("Hello, world!");
        chatMessage.setSentAt(LocalDateTime.now());

        messageLike = new MessageLike(chatMessage, liker);
    }

    @Test
    void testMessageLikeCreation() {
        assertNotNull(messageLike);
        assertEquals(chatMessage, messageLike.getMessage());
        assertEquals(liker, messageLike.getLiker());
        assertNotNull(messageLike.getId());
        assertEquals(new MessageLikeId(chatMessage.getId(), liker.getId()), messageLike.getId());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageLike anotherLike = new MessageLike(chatMessage, liker);
        assertEquals(messageLike, anotherLike);
        assertEquals(messageLike.hashCode(), anotherLike.hashCode());
    }

    @Test
    void testSettersAndGetters() {
        ChatUser newLiker = new ChatUser();
        newLiker.setId(2L);
        newLiker.setEmail("newliker@example.com");
        newLiker.setName("New Liker");
        newLiker.setPassword("password");
        newLiker.setJoinedAt(LocalDateTime.now());

        messageLike.setLiker(newLiker);
        assertEquals(newLiker, messageLike.getLiker());

        ChatMessage newMessage = new ChatMessage();
        newMessage.setId(2L);
        newMessage.setSender(newLiker);
        newMessage.setContent("New message");
        newMessage.setSentAt(LocalDateTime.now());

        messageLike.setMessage(newMessage);
        assertEquals(newMessage, messageLike.getMessage());
    }
}
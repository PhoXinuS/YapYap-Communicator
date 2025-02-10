package com.example.texttuah.entityUnitTests;

import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Conversation;
import com.example.texttuah.entity.ConversationMembers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ConversationUnitTests {

    @Mock
    private ConversationMembers member1;

    @Mock
    private ConversationMembers member2;

    @Mock
    private ChatUser chatUser1;

    @Mock
    private ChatUser chatUser2;

    private Conversation conversation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        conversation = new Conversation(1L, "Test Conversation", LocalDateTime.now());
    }

    @Test
    void testGetAllMembers() {
        when(member1.getConversationMember()).thenReturn(chatUser1);
        when(member2.getConversationMember()).thenReturn(chatUser2);
        when(chatUser1.getId()).thenReturn(1L);
        when(chatUser1.getEmail()).thenReturn("user1@example.com");
        when(chatUser2.getId()).thenReturn(2L);
        when(chatUser2.getEmail()).thenReturn("user2@example.com");

        conversation.setConversationMembers(Stream.of(member1, member2).collect(Collectors.toSet()));

        Set<ChatUser> members = conversation.getAllMembers();

        assertEquals(2, members.size());
        assertEquals(Set.of(chatUser1, chatUser2), members);
    }

    @Test
    void testGetId() {
        assertEquals(1L, conversation.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Test Conversation", conversation.getName());
    }

    @Test
    void testGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        conversation.setCreatedAt(createdAt);
        assertEquals(createdAt, conversation.getCreatedAt());
    }

    @Test
    void testGetConversationMembers() {
        ConversationMembers member1 = new ConversationMembers();
        ConversationMembers member2 = new ConversationMembers();
        conversation.setConversationMembers(Stream.of(member1, member2).collect(Collectors.toSet()));
        assertEquals(Set.of(member1, member2), conversation.getConversationMembers());
    }

    @Test
    void testSetId() {
        conversation.setId(2L);
        assertEquals(2L, conversation.getId());
    }

    @Test
    void testSetName() {
        conversation.setName("New Conversation");
        assertEquals("New Conversation", conversation.getName());
    }

    @Test
    void testPrePersist() {
        conversation.setPrePersist();
        assertTrue(SECONDS.between(LocalDateTime.now(), conversation.getCreatedAt()) < 1);
    }

    @Test
    void testConversationString() {
        Conversation conversation = new Conversation("Test Conversation");
        assertEquals("Test Conversation", conversation.getName());
    }

    @Test
    void testConversationLongStringLocalDateTime() {
        LocalDateTime createdAt = LocalDateTime.now();
        Conversation conversation = new Conversation(1L, "Test Conversation", createdAt);
        assertEquals(1L, conversation.getId());
        assertEquals("Test Conversation", conversation.getName());
        assertEquals(createdAt, conversation.getCreatedAt());
    }
}
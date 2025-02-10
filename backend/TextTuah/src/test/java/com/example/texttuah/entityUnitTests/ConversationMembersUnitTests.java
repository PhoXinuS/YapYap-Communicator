package com.example.texttuah.entityUnitTests;

import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Conversation;
import com.example.texttuah.entity.ConversationMembers;
import com.example.texttuah.embeddable.ConversationMembersId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ConversationMembersUnitTests {

    @Mock
    private Conversation conversation;

    @Mock
    private ChatUser chatUser;

    private ConversationMembers conversationMembers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        conversationMembers = new ConversationMembers(new ConversationMembersId(1L, 1L), conversation, chatUser);
    }

    @Test
    void testGetId() {
        ConversationMembersId id = new ConversationMembersId(1L, 1L);
        conversationMembers.setId(id);
        assertEquals(id, conversationMembers.getId());
    }

    @Test
    void testGetJoinedAt() {
        LocalDateTime joinedAt = LocalDateTime.now();
        conversationMembers.setJoinedAt(joinedAt);
        assertEquals(joinedAt, conversationMembers.getJoinedAt());
    }

    @Test
    void testGetConversation() {
        when(conversation.getId()).thenReturn(1L);
        conversationMembers.setConversation(conversation);
        assertEquals(conversation, conversationMembers.getConversation());
    }

    @Test
    void testGetConversationMember() {
        when(chatUser.getId()).thenReturn(1L);
        conversationMembers.setConversationMember(chatUser);
        assertEquals(chatUser, conversationMembers.getConversationMember());
    }

    @Test
    void testOnPrePersist() {
        conversationMembers.onPrePersist();
        assertTrue(SECONDS.between(LocalDateTime.now(), conversationMembers.getJoinedAt()) < 1);
    }

    @Test
    void testConstructorWithConversationMembersIdConversationAndChatUser() {
        ConversationMembersId id = new ConversationMembersId(1L, 1L);
        conversationMembers = new ConversationMembers(id, conversation, chatUser);
        assertEquals(id, conversationMembers.getId());
        assertEquals(conversation, conversationMembers.getConversation());
        assertEquals(chatUser, conversationMembers.getConversationMember());
    }

    @Test
    void testConstructorWithConversationAndChatUser() {
        conversationMembers = new ConversationMembers(conversation, chatUser);
        assertEquals(conversation, conversationMembers.getConversation());
        assertEquals(chatUser, conversationMembers.getConversationMember());
    }

    @Test
    void testConstructorWithConversationMembersIdJoinedAtConversationAndChatUser() {
        ConversationMembersId id = new ConversationMembersId(1L, 1L);
        LocalDateTime joinedAt = LocalDateTime.now();
        conversationMembers = new ConversationMembers(id, joinedAt, conversation, chatUser);
        assertEquals(id, conversationMembers.getId());
        assertEquals(joinedAt, conversationMembers.getJoinedAt());
        assertEquals(conversation, conversationMembers.getConversation());
        assertEquals(chatUser, conversationMembers.getConversationMember());
    }
}
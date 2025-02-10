package com.example.texttuah.embeddableUnitTests;

import com.example.texttuah.embeddable.ConversationMembersId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ConversationMembersIdUnitTests {

    @Test
    void testConstructorAndGetters() {
        ConversationMembersId id = new ConversationMembersId(1L, 2L);
        assertEquals(1L, id.getConversationId());
        assertEquals(2L, id.getChatUserId());
    }

    @Test
    void testSetters() {
        ConversationMembersId id = new ConversationMembersId();
        id.setConversationId(1L);
        id.setChatUserId(2L);
        assertEquals(1L, id.getConversationId());
        assertEquals(2L, id.getChatUserId());
    }

    @Test
    void testHashCode() {
        ConversationMembersId id1 = new ConversationMembersId(1L, 2L);
        ConversationMembersId id2 = new ConversationMembersId(1L, 2L);
        ConversationMembersId id3 = new ConversationMembersId(2L, 1L);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void testEquals() {
        ConversationMembersId id1 = new ConversationMembersId(1L, 2L);
        ConversationMembersId id2 = new ConversationMembersId(1L, 2L);
        ConversationMembersId id3 = new ConversationMembersId(2L, 1L);
        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
    }

    @Test
    void testEqualsWithNull() {
        ConversationMembersId id = new ConversationMembersId(1L, 2L);
        assertNotEquals(null, id);
    }
}
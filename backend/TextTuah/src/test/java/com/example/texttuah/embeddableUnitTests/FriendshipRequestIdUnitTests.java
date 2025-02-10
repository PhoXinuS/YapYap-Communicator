package com.example.texttuah.embeddableUnitTests;

import com.example.texttuah.embeddable.FriendshipRequestId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FriendshipRequestIdUnitTests {

    @Test
    void testConstructorAndGetters() {
        FriendshipRequestId id = new FriendshipRequestId(1L, 2L);
        assertEquals(1L, id.getRequesterId());
        assertEquals(2L, id.getRequesteeId());
    }

    @Test
    void testSetters() {
        FriendshipRequestId id = new FriendshipRequestId();
        id.setRequesterId(1L);
        id.setRequesteeId(2L);
        assertEquals(1L, id.getRequesterId());
        assertEquals(2L, id.getRequesteeId());
    }

    @Test
    void testEquals() {
        FriendshipRequestId id1 = new FriendshipRequestId(1L, 2L);
        FriendshipRequestId id2 = new FriendshipRequestId(1L, 2L);
        FriendshipRequestId id3 = new FriendshipRequestId(2L, 1L);
        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
    }

    @Test
    void testHashCode() {
        FriendshipRequestId id1 = new FriendshipRequestId(1L, 2L);
        FriendshipRequestId id2 = new FriendshipRequestId(1L, 2L);
        FriendshipRequestId id3 = new FriendshipRequestId(2L, 1L);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void testEqualsWithNull() {
        FriendshipRequestId id = new FriendshipRequestId(1L, 2L);
        assertNotEquals(null, id);
    }
}
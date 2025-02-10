package com.example.texttuah.entityUnitTests;

import com.example.texttuah.embeddable.FriendshipRequestId;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.FriendshipRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class FriendshipRequestUnitTests {

    @Mock
    private ChatUser requester;

    @Mock
    private ChatUser requested;

    private FriendshipRequest friendshipRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        friendshipRequest = new FriendshipRequest(new FriendshipRequestId(1L, 2L), requester, requested, LocalDateTime.now());
    }

    @Test
    void testGetId() {
        FriendshipRequestId id = new FriendshipRequestId(1L, 2L);
        friendshipRequest.setId(id);
        assertEquals(id, friendshipRequest.getId());
    }

    @Test
    void testGetRequester() {
        when(requester.getId()).thenReturn(1L);
        friendshipRequest.setRequester(requester);
        assertEquals(requester, friendshipRequest.getRequester());
    }

    @Test
    void testGetRequested() {
        when(requested.getId()).thenReturn(2L);
        friendshipRequest.setRequested(requested);
        assertEquals(requested, friendshipRequest.getRequested());
    }

    @Test
    void testGetRequestedAt() {
        LocalDateTime requestedAt = LocalDateTime.now();
        friendshipRequest.setRequestedAt(requestedAt);
        assertEquals(requestedAt, friendshipRequest.getRequestedAt());
    }

    @Test
    void testConstructor() {
        FriendshipRequestId id = new FriendshipRequestId(1L, 2L);
        LocalDateTime requestedAt = LocalDateTime.now();
        FriendshipRequest friendshipRequest = new FriendshipRequest(id, requester, requested, requestedAt);
        assertEquals(id, friendshipRequest.getId());
        assertEquals(requester, friendshipRequest.getRequester());
        assertEquals(requested, friendshipRequest.getRequested());
        assertEquals(requestedAt, friendshipRequest.getRequestedAt());
    }

    @Test
    void testDefaultConstructor() {
        FriendshipRequest friendshipRequest = new FriendshipRequest();
        assertNull(friendshipRequest.getId());
        assertNull(friendshipRequest.getRequester());
        assertNull(friendshipRequest.getRequested());
        assertNull(friendshipRequest.getRequestedAt());
    }

    @Test
    void testSetId() {
        FriendshipRequestId id = new FriendshipRequestId(1L, 2L);
        friendshipRequest.setId(id);
        assertEquals(id, friendshipRequest.getId());
    }

    @Test
    void testSetRequester() {
        friendshipRequest.setRequester(requester);
        assertEquals(requester, friendshipRequest.getRequester());
    }

    @Test
    void testSetRequestedAt() {
        LocalDateTime requestedAt = LocalDateTime.now();
        friendshipRequest.setRequestedAt(requestedAt);
        assertEquals(requestedAt, friendshipRequest.getRequestedAt());
    }
}
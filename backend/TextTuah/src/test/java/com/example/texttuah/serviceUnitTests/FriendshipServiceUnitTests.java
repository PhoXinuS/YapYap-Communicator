package com.example.texttuah.serviceUnitTests;

import com.example.texttuah.dto.UserIdentificationDTO;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Friendship;
import com.example.texttuah.repository.FriendshipRepository;
import com.example.texttuah.repository.FriendshipRequestRepository;
import com.example.texttuah.service.FriendshipService;
import com.example.texttuah.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FriendshipServiceUnitTests {

    @Mock
    private UserService userService;

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private FriendshipRequestRepository friendshipRequestRepository;

    @InjectMocks
    private FriendshipService friendshipService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFriendship_Success() {
        UserIdentificationDTO userId1 = new UserIdentificationDTO();
        UserIdentificationDTO userId2 = new UserIdentificationDTO();
        ChatUser user1 = new ChatUser();
        ChatUser user2 = new ChatUser();
        Friendship friendship = new Friendship();

        when(userService.findByUserIdentification(userId1)).thenReturn(user1);
        when(userService.findByUserIdentification(userId2)).thenReturn(user2);
        when(friendshipRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.of(friendship));

        Friendship result = friendshipService.getFriendship(userId1, userId2);

        assertNotNull(result);
        assertEquals(friendship, result);
    }

    @Test
    void testGetFriendship_UserNotFound() {
        UserIdentificationDTO userId1 = new UserIdentificationDTO();
        UserIdentificationDTO userId2 = new UserIdentificationDTO();

        when(userService.findByUserIdentification(userId1)).thenReturn(null);

        Friendship result = friendshipService.getFriendship(userId1, userId2);

        assertNull(result);
    }
}
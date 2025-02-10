package com.example.texttuah.entityUnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import com.example.texttuah.entity.Friendship;
import com.example.texttuah.entity.ChatUser;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipUnitTests {

    private Friendship friendship;
    private ChatUser user1;
    private ChatUser user2;

    @BeforeEach
    void setUp() {
        user1 = new ChatUser(1L, "user1@example.com", "Kamil Zdun", "password1", LocalDateTime.now());
        user2 = new ChatUser(2L, "user2@example.com", "Damian Wasik", "password2", LocalDateTime.now());
        friendship = new Friendship();
        friendship.setId(1L);
        friendship.setUser1(user1);
        friendship.setUser2(user2);
        friendship.setFriendsSince(LocalDateTime.of(2023, 5, 1, 12, 0));
    }

    @Test
    void gettersAndSettersTest() {
        assertEquals(1, friendship.getId());
        assertEquals(user1, friendship.getUser1());
        assertEquals(user2, friendship.getUser2());
        assertEquals(LocalDateTime.of(2023, 5, 1, 12, 0), friendship.getFriendsSince());

        ChatUser newUser1 = new ChatUser(3L, "newuser1@example.com", "Docent", "password3", LocalDateTime.now());
        ChatUser newUser2 = new ChatUser(4L, "newuser2@example.com", "Alojzy", "password4", LocalDateTime.now());
        friendship.setId(2L);
        friendship.setUser1(newUser1);
        friendship.setUser2(newUser2);
        friendship.setFriendsSince(LocalDateTime.of(2023, 6, 1, 12, 0));

        assertEquals(2, friendship.getId());
        assertEquals(newUser1, friendship.getUser1());
        assertEquals(newUser2, friendship.getUser2());
        assertEquals(LocalDateTime.of(2023, 6, 1, 12, 0), friendship.getFriendsSince());
    }

    @Test
    void constructorTest() {
        LocalDateTime friendsSince = LocalDateTime.of(2023, 7, 1, 12, 0);
        Friendship newFriendship = new Friendship(3L, user1, user2, friendsSince);

        assertEquals(3, newFriendship.getId());
        assertEquals(user1, newFriendship.getUser1());
        assertEquals(user2, newFriendship.getUser2());
        assertEquals(friendsSince, newFriendship.getFriendsSince());
    }

    @Test
    void constructorTest_EmptyInput() {
        Friendship emptyFriendship = new Friendship();

        assertNull(emptyFriendship.getId());
        assertNull(emptyFriendship.getUser1());
        assertNull(emptyFriendship.getUser2());
        assertNull(emptyFriendship.getFriendsSince());
    }
}
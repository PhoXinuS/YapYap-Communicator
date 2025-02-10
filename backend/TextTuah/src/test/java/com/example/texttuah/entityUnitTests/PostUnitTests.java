package com.example.texttuah.entityUnitTests;

import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PostUnitTests {

    private Post post;
    private ChatUser poster;

    @BeforeEach
    void setUp() {
        poster = new ChatUser();
        poster.setId(1L);
        poster.setEmail("poster@example.com");
        poster.setName("Poster");
        poster.setPassword("password");
        poster.setJoinedAt(LocalDateTime.now());

        post = new Post();
        post.setId(1L);
        post.setPoster(poster);
        post.setContent("Test post content");
        post.setPostedAt(LocalDateTime.now());
    }

    @Test
    void testPostCreation() {
        assertNotNull(post);
        assertEquals(1L, post.getId());
        assertEquals(poster, post.getPoster());
        assertEquals("Test post content", post.getContent());
        assertNotNull(post.getPostedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        Post anotherPost = new Post();
        anotherPost.setId(1L);
        anotherPost.setPoster(poster);
        anotherPost.setContent("Test post content");
        anotherPost.setPostedAt(post.getPostedAt());

        assertEquals(post, anotherPost);
        assertEquals(post.hashCode(), anotherPost.hashCode());
    }

    @Test
    void testSettersAndGetters() {
        ChatUser newPoster = new ChatUser();
        newPoster.setId(2L);
        newPoster.setEmail("newposter@example.com");
        newPoster.setName("New Poster");
        newPoster.setPassword("password");
        newPoster.setJoinedAt(LocalDateTime.now());

        post.setPoster(newPoster);
        assertEquals(newPoster, post.getPoster());

        post.setContent("New post content");
        assertEquals("New post content", post.getContent());
    }
}
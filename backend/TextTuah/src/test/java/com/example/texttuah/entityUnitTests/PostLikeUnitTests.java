package com.example.texttuah.entityUnitTests;

import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Post;
import com.example.texttuah.entity.PostLike;
import com.example.texttuah.embeddable.PostLikeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PostLikeUnitTests {

    private PostLike postLike;
    private Post post;
    private ChatUser liker;

    @BeforeEach
    void setUp() {
        liker = new ChatUser();
        liker.setId(1L);
        liker.setEmail("liker@example.com");
        liker.setName("Liker");
        liker.setPassword("password");
        liker.setJoinedAt(LocalDateTime.now());

        post = new Post();
        post.setId(1L);
        post.setPoster(liker);
        post.setContent("Test post content");
        post.setPostedAt(LocalDateTime.now());

        postLike = new PostLike(post, liker);
    }

    @Test
    void testPostLikeCreation() {
        assertNotNull(postLike);
        assertEquals(post, postLike.getPost());
        assertEquals(liker, postLike.getLiker());
        assertNotNull(postLike.getId());
        assertEquals(new PostLikeId(post.getId(), liker.getId()), postLike.getId());
    }

    @Test
    void testEqualsAndHashCode() {
        PostLike anotherLike = new PostLike(post, liker);
        assertEquals(postLike, anotherLike);
        assertEquals(postLike.hashCode(), anotherLike.hashCode());
    }

    @Test
    void testSettersAndGetters() {
        ChatUser newLiker = new ChatUser();
        newLiker.setId(2L);
        newLiker.setEmail("newliker@example.com");
        newLiker.setName("New Liker");
        newLiker.setPassword("password");
        newLiker.setJoinedAt(LocalDateTime.now());

        postLike.setLiker(newLiker);
        assertEquals(newLiker, postLike.getLiker());

        Post newPost = new Post();
        newPost.setId(2L);
        newPost.setPoster(newLiker);
        newPost.setContent("New post content");
        newPost.setPostedAt(LocalDateTime.now());

        postLike.setPost(newPost);
        assertEquals(newPost, postLike.getPost());
    }
}
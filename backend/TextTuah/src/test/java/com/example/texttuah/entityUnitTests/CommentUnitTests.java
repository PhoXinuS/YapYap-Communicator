package com.example.texttuah.entityUnitTests;

import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Comment;
import com.example.texttuah.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CommentUnitTests {

    private Comment comment;
    private ChatUser commenter;
    private Post post;

    @BeforeEach
    void setUp() {
        commenter = new ChatUser();
        commenter.setId(1L);
        commenter.setEmail("commenter@example.com");
        commenter.setName("Commenter");
        commenter.setPassword("password");
        commenter.setJoinedAt(LocalDateTime.now());

        post = new Post();
        post.setId(1L);
        post.setContent("Test post content");
        post.setPostedAt(LocalDateTime.now());

        comment = new Comment();
        comment.setId(1L);
        comment.setCommenter(commenter);
        comment.setCommentedPost(post);
        comment.setContent("Test comment content");
        comment.setCommentedAt(LocalDateTime.now());
    }

    @Test
    void testCommentCreation() {
        assertNotNull(comment);
        assertEquals(1L, comment.getId());
        assertEquals(commenter, comment.getCommenter());
        assertEquals(post, comment.getCommentedPost());
        assertEquals("Test comment content", comment.getContent());
        assertNotNull(comment.getCommentedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        Comment anotherComment = new Comment();
        anotherComment.setId(1L);
        anotherComment.setCommenter(commenter);
        anotherComment.setCommentedPost(post);
        anotherComment.setContent("Test comment content");
        anotherComment.setCommentedAt(comment.getCommentedAt());

        assertEquals(comment, anotherComment);
        assertEquals(comment.hashCode(), anotherComment.hashCode());
    }

    @Test
    void testSettersAndGetters() {
        ChatUser newCommenter = new ChatUser();
        newCommenter.setId(2L);
        newCommenter.setEmail("newcommenter@example.com");
        newCommenter.setName("New Commenter");
        newCommenter.setPassword("password");
        newCommenter.setJoinedAt(LocalDateTime.now());

        comment.setCommenter(newCommenter);
        assertEquals(newCommenter, comment.getCommenter());

        Post newPost = new Post();
        newPost.setId(2L);
        newPost.setContent("New post content");
        newPost.setPostedAt(LocalDateTime.now());

        comment.setCommentedPost(newPost);
        assertEquals(newPost, comment.getCommentedPost());

        comment.setContent("New comment content");
        assertEquals("New comment content", comment.getContent());
    }
}
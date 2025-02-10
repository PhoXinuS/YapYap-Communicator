package com.example.texttuah.dtoUnitTests;

import com.example.texttuah.dto.CommentDTO;
import com.example.texttuah.dto.UserFriendDTO;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Comment;
import com.example.texttuah.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CommentDTOUnitTests {

    private Comment comment;
    private UserFriendDTO userFriendDTO;
    private Post post;

    @BeforeEach
    void setUp() {
        ChatUser commenter = new ChatUser();
        commenter.setId(1L);
        commenter.setEmail("commenter@example.com");
        commenter.setName("Commenter");
        commenter.setPassword("password");
        commenter.setJoinedAt(LocalDateTime.now());

        userFriendDTO = new UserFriendDTO(commenter.getName(), commenter.getEmail());

        post = new Post();
        post.setId(1L);

        comment = new Comment();
        comment.setId(1L);
        comment.setCommenter(commenter);
        comment.setContent("Test comment content");
        comment.setCommentedAt(LocalDateTime.now());


    }

    @Test
    void testCommentDTOCreation() {
        // Long id, Long postId, UserFriendDTO commenter, String content, LocalDateTime commentedAt, Integer likes, Boolean isLiked
        CommentDTO dto = new CommentDTO(comment.getId(), post.getId(), userFriendDTO, comment.getContent(), comment.getCommentedAt(), 0, false);

        assertNotNull(dto);
        assertEquals(comment.getId(), dto.getId());
        assertEquals(comment.getContent(), dto.getContent());
        assertEquals(userFriendDTO, dto.getCommenter());
        assertEquals(comment.getCommentedAt(), dto.getCommentedAt());
    }

    @Test
    void testSettersAndGetters() {
        CommentDTO dto = new CommentDTO();
        dto.setId(1L);
        dto.setContent("Updated content");
        dto.setCommenter(userFriendDTO);
        dto.setCommentedAt(LocalDateTime.now());

        assertEquals(1L, dto.getId());
        assertEquals("Updated content", dto.getContent());
        assertEquals(userFriendDTO, dto.getCommenter());
        assertNotNull(dto.getCommentedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        // Long id, Long postId, UserFriendDTO commenter, String content, LocalDateTime commentedAt, Integer likes, Boolean isLiked
        CommentDTO dto1 = new CommentDTO(1L, 1L, userFriendDTO, "Test comment content", LocalDateTime.now(), 0, false);
        CommentDTO dto2 = new CommentDTO(1L, 1L, userFriendDTO, "Test comment content", LocalDateTime.now(), 0, false);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}
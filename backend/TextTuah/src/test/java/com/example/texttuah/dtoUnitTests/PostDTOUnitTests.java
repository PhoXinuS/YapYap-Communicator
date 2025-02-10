package com.example.texttuah.dtoUnitTests;

import com.example.texttuah.dto.PostDTO;
import com.example.texttuah.dto.UserFriendDTO;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Post;
import com.example.texttuah.entity.PostLike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PostDTOUnitTests {

    private Post post;
    private ChatUser liker;

    @BeforeEach
    void setUp() {
        ChatUser poster = new ChatUser();
        poster.setId(1L);
        poster.setEmail("poster@example.com");
        poster.setName("Poster");
        poster.setPassword("password");
        poster.setJoinedAt(LocalDateTime.now());

        liker = new ChatUser();
        liker.setId(2L);
        liker.setEmail("liker@example.com");
        liker.setName("Liker");
        liker.setPassword("password");
        liker.setJoinedAt(LocalDateTime.now());

        post = new Post();
        post.setId(1L);
        post.setPoster(poster);
        post.setContent("Test post content");
        post.setPostedAt(LocalDateTime.now());

        Set<PostLike> likes = new HashSet<>();
        PostLike like = new PostLike(post, liker);
        likes.add(like);
        post.setLikes(likes);

        liker.setPostLikes(likes);
    }

    @Test
    void testConvertToDTO() {
        PostDTO dto = PostDTO.convertTo(post, liker);

        assertNotNull(dto);
        assertEquals(post.getId(), dto.getId());
        assertEquals(post.getContent(), dto.getContent());
        assertEquals(post.getPoster().getName(), dto.getPoster().getName());
        assertEquals(post.getPostedAt(), dto.getPostedAt());
        assertEquals(post.getLikes().size(), dto.getLikes());
        assertTrue(dto.getLiked());
    }

    @Test
    void testConvertToDTOSet() {
        Set<Post> posts = new HashSet<>();
        posts.add(post);

        Set<PostDTO> dtos = PostDTO.convertTo(posts, liker);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        PostDTO dto = dtos.iterator().next();
        assertEquals(post.getId(), dto.getId());
        assertEquals(post.getContent(), dto.getContent());
        assertEquals(post.getPoster().getName(), dto.getPoster().getName());
        assertEquals(post.getPostedAt(), dto.getPostedAt());
        assertEquals(post.getLikes().size(), dto.getLikes());
        assertTrue(dto.getLiked());
    }

    @Test
    void testEqualsAndHashCode() {
        PostDTO dto1 = PostDTO.convertTo(post, liker);
        PostDTO dto2 = PostDTO.convertTo(post, liker);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testSettersAndGetters() {
        PostDTO dto = new PostDTO();
        dto.setId(1L);
        dto.setContent("Test content");
        dto.setPoster(new UserFriendDTO("Test Friend", "friend@example.com"));
        dto.setPostedAt(LocalDateTime.now());
        dto.setLikes(5);
        dto.setLiked(true);

        assertEquals(1L, dto.getId());
        assertEquals("Test content", dto.getContent());
        assertEquals("Test Friend", dto.getPoster().getName());
        assertNotNull(dto.getPostedAt());
        assertEquals(5, dto.getLikes());
        assertTrue(dto.getLiked());
    }
}
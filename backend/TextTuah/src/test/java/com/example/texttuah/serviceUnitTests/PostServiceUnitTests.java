package com.example.texttuah.serviceUnitTests;

import com.example.texttuah.dto.PostContentDTO;
import com.example.texttuah.dto.PostDTO;
import com.example.texttuah.dto.UserIdentificationDTO;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Post;
import com.example.texttuah.repository.PostLikeRepository;
import com.example.texttuah.repository.PostRepository;
import com.example.texttuah.service.PostService;
import com.example.texttuah.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceUnitTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @Mock
    private PostLikeRepository postLikeRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPost_Success() {
        PostContentDTO postContentDTO = new PostContentDTO("Test Content");
        ChatUser user = new ChatUser(1L, "test@example.com", "Test", "password", LocalDateTime.now());

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PostDTO postDTO = postService.addPost(postContentDTO, "test@example.com");

        assertNotNull(postDTO);
        assertEquals("Test Content", postDTO.getContent());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testAddPost_UserNotFound() {
        PostContentDTO postContentDTO = new PostContentDTO("Test Content");

        when(userService.findByEmail("test@example.com")).thenReturn(null);

        PostDTO postDTO = postService.addPost(postContentDTO, "test@example.com");

        assertNull(postDTO);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void testGetPostById_Success() {
        Post post = new Post();
        post.setId(1L);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post foundPost = postService.getPostById(1L);

        assertNotNull(foundPost);
        assertEquals(1L, foundPost.getId());
    }

    @Test
    void testGetPostById_NotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Post foundPost = postService.getPostById(1L);

        assertNull(foundPost);
    }

    @Test
    void testDeletePost_Success() {
        ChatUser user = new ChatUser(1L, "test@example.com", "Test", "password", LocalDateTime.now());
        Post post = new Post();
        post.setId(1L);
        post.setPoster(user);

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.deletePost(1L, "test@example.com");

        verify(postRepository, times(1)).delete(post);
    }

    @Test
    void testDeletePost_NotFound() {
        when(userService.findByEmail("test@example.com")).thenReturn(new ChatUser());
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        postService.deletePost(1L, "test@example.com");

        verify(postRepository, never()).delete(any(Post.class));
    }

    @Test
    void testGetUserPosts_Success() {
        ChatUser user = new ChatUser(1L, "test@example.com", "Test", "password", LocalDateTime.now());

        when(userService.findByUserIdentification(any(UserIdentificationDTO.class))).thenReturn(user);

        Set<PostDTO> posts = postService.getUserPosts(new UserIdentificationDTO());

        assertNotNull(posts);
    }

    @Test
    void testToggleLike_Success() {
        ChatUser user = new ChatUser(1L, "test@example.com", "Test", "password", LocalDateTime.now());
        Post post = new Post();

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        boolean liked = postService.toggleLike("test@example.com", 1L);

        assertTrue(liked);
        verify(postLikeRepository, times(1)).save(any());
    }

    @Test
    void testToggleLike_PostNotFound() {
        when(userService.findByEmail("test@example.com")).thenReturn(new ChatUser());
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        boolean liked = postService.toggleLike("test@example.com", 1L);

        assertFalse(liked);
        verify(postLikeRepository, never()).save(any());
    }
}
package com.example.texttuah.serviceUnitTests;

import com.example.texttuah.dto.CommentContentDTO;
import com.example.texttuah.dto.CommentDTO;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Comment;
import com.example.texttuah.entity.Post;
import com.example.texttuah.repository.CommentRepository;
import com.example.texttuah.service.CommentService;
import com.example.texttuah.service.PostService;
import com.example.texttuah.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceUnitTests {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPostComments_Success() {
        Long postId = 1L;
        ChatUser user = new ChatUser();
        user.setId(1L);
        user.setEmail("test@example.com");

        Post post = new Post();
        post.setId(postId);
        Comment comment = new Comment(1L, post, user, "Test Comment", LocalDateTime.now());

        Set<Comment> comments = new HashSet<>();
        comments.add(comment);

        when(userService.findByEmail(user.getEmail())).thenReturn(user);
        when(commentRepository.getCommentsByCommentedPost_Id(postId)).thenReturn(comments);

        Set<CommentDTO> result = commentService.getPostComments(postId, user.getEmail());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCommentPost_Success() {
        String commenterEmail = "test@example.com";
        Long postId = 1L;
        CommentContentDTO contentDTO = new CommentContentDTO();
        contentDTO.setContent("Test Comment");
        ChatUser commenter = new ChatUser();
        commenter.setId(1L);
        commenter.setEmail(commenterEmail);
        Post post = new Post();
        post.setId(postId);
        Comment comment = new Comment(1L, post, new ChatUser(), "Test Comment", LocalDateTime.now());

        when(userService.findByEmail(commenterEmail)).thenReturn(commenter);
        when(postService.getPostById(postId)).thenReturn(post);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDTO result = commentService.commentPost(commenterEmail, postId, contentDTO);

        assertNotNull(result);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testCommentPost_UserNotFound() {
        String commenterEmail = "test@example.com";
        Long postId = 1L;
        CommentContentDTO contentDTO = new CommentContentDTO();
        contentDTO.setContent("Test Comment");

        when(userService.findByEmail(commenterEmail)).thenReturn(null);

        CommentDTO result = commentService.commentPost(commenterEmail, postId, contentDTO);

        assertNull(result);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testCommentPost_PostNotFound() {
        String commenterEmail = "test@example.com";
        Long postId = 1L;
        CommentContentDTO contentDTO = new CommentContentDTO();
        contentDTO.setContent("Test Comment");
        ChatUser commenter = new ChatUser();

        when(userService.findByEmail(commenterEmail)).thenReturn(commenter);
        when(postService.getPostById(postId)).thenReturn(null);

        CommentDTO result = commentService.commentPost(commenterEmail, postId, contentDTO);

        assertNull(result);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testCommentPost_InvalidContent() {
        String commenterEmail = "test@example.com";
        Long postId = 1L;
        CommentContentDTO contentDTO = new CommentContentDTO();
        contentDTO.setContent(null);
        ChatUser commenter = new ChatUser();
        Post post = new Post();

        when(userService.findByEmail(commenterEmail)).thenReturn(commenter);
        when(postService.getPostById(postId)).thenReturn(post);

        CommentDTO result = commentService.commentPost(commenterEmail, postId, contentDTO);

        assertNull(result);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testRemoveComment_Success() {
        String email = "test@example.com";
        Long commentId = 1L;
        ChatUser commenter = new ChatUser();
        commenter.setEmail(email);
        Comment comment = new Comment();
        comment.setCommenter(commenter);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.removeComment(email, commentId);

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void testRemoveComment_CommentNotFound() {
        String email = "test@example.com";
        Long commentId = 1L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        commentService.removeComment(email, commentId);

        verify(commentRepository, never()).delete(any(Comment.class));
    }

    @Test
    void testRemoveComment_UnauthorizedUser() {
        String email = "test@example.com";
        Long commentId = 1L;
        ChatUser commenter = new ChatUser();
        commenter.setEmail("other@example.com");
        Comment comment = new Comment();
        comment.setCommenter(commenter);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.removeComment(email, commentId);

        verify(commentRepository, never()).delete(any(Comment.class));
    }
}
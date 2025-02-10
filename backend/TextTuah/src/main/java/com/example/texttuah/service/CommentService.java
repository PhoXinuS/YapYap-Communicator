package com.example.texttuah.service;

import com.example.texttuah.dto.CommentContentDTO;
import com.example.texttuah.dto.CommentDTO;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Comment;
import com.example.texttuah.entity.CommentLike;
import com.example.texttuah.entity.Post;
import com.example.texttuah.repository.CommentLikeRepository;
import com.example.texttuah.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    private final CommentLikeRepository commentLikeRepository;

    @Autowired
    public CommentService(
            CommentRepository commentRepository,
            UserService userService,
            PostService postService,
            CommentLikeRepository commentLikeRepository
    ) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
        this.commentLikeRepository = commentLikeRepository;
    }

    public Set<CommentDTO> getPostComments(Long postId, String email) {
        ChatUser user = userService.findByEmail(email);
        return CommentDTO.convertTo(commentRepository.getCommentsByCommentedPost_Id(postId), user);
    }

    public CommentDTO commentPost(
            String commenterEmail,
            Long postId,
            CommentContentDTO contentDTO) {
        ChatUser commenter = userService.findByEmail(commenterEmail);
        if (commenter == null) {
            return null;
        }
        Post post = postService.getPostById(postId);
        if (post == null) {
            return null;
        }
        if (contentDTO.getContent() == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setCommenter(commenter);
        comment.setCommentedPost(post);
        comment.setContent(contentDTO.getContent());
        return CommentDTO.convertTo(commentRepository.save(comment), commenter);
    }

    public void removeComment(String email, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return;
        }
        if (!comment.getCommenter().getEmail().equals(email)) {
            return;
        }
        commentRepository.delete(comment);
    }

    public boolean toggleLike(String email, Long commentId) {
        ChatUser user = userService.findByEmail(email);
        Comment comment = commentRepository.findById(commentId).orElse(null);
        assert user != null;
        assert comment != null;
        CommentLike like = new CommentLike(comment, user);
        if (user.getCommentLikes().contains(like)) {
            commentLikeRepository.delete(like);
            return false;
        } else {
            commentLikeRepository.save(like);
            return true;
        }
    }
}

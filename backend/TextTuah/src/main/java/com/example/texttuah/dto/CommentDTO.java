package com.example.texttuah.dto;

import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Comment;
import com.example.texttuah.entity.CommentLike;
import com.example.texttuah.entity.PostLike;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CommentDTO {
    private Long id;
    private Long postId;
    private UserFriendDTO commenter;
    private String content;
    private LocalDateTime commentedAt;
    private Integer likes;
    private Boolean isLiked;

    public CommentDTO() {
    }

    public CommentDTO(Long id, Long postId, UserFriendDTO commenter, String content, LocalDateTime commentedAt, Integer likes, Boolean isLiked) {
        this.id = id;
        this.postId = postId;
        this.commenter = commenter;
        this.content = content;
        this.commentedAt = commentedAt;
        this.likes = likes;
        this.isLiked = isLiked;
    }

    public static CommentDTO convertTo(Comment comment, ChatUser user) {
        return new CommentDTO(
                comment.getId(),
                comment.getCommentedPost().getId(),
                new UserFriendDTO(comment.getCommenter().getName(), comment.getCommenter().getEmail()),
                comment.getContent(),
                comment.getCommentedAt(),
                comment.getLikes().size(),
                user.getCommentLikes().contains(new CommentLike(comment, user))
        );
    }

    public static Set<CommentDTO> convertTo(Set<Comment> comments, ChatUser user) {
        return comments.stream()
                .map(comment -> CommentDTO.convertTo(comment, user))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CommentDTO that = (CommentDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public UserFriendDTO getCommenter() {
        return commenter;
    }

    public void setCommenter(UserFriendDTO commenter) {
        this.commenter = commenter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(LocalDateTime commentedAt) {
        this.commentedAt = commentedAt;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }
}

package com.example.texttuah.embeddable;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CommentLikeId {
    private Long commentId;
    private Long likerId;

    public CommentLikeId() {
    }

    public CommentLikeId(Long commentId, Long likerId) {
        this.commentId = commentId;
        this.likerId = likerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentLikeId that = (CommentLikeId) o;
        return Objects.equals(commentId, that.commentId) && Objects.equals(likerId, that.likerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, likerId);
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getLikerId() {
        return likerId;
    }

    public void setLikerId(Long likerId) {
        this.likerId = likerId;
    }
}

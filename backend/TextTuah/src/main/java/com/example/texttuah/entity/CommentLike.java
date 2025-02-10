package com.example.texttuah.entity;

import com.example.texttuah.embeddable.CommentLikeId;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "COMMENT_LIKES")
public class CommentLike {
    @EmbeddedId
    private CommentLikeId commentLikeId;

    @ManyToOne
    @MapsId("commentId")
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    @ManyToOne
    @MapsId("likerId")
    @JoinColumn(name = "LIKER_ID")
    private ChatUser liker;

    public CommentLike() {
    }

    public CommentLike(Comment comment, ChatUser liker) {
        this.commentLikeId = new CommentLikeId(comment.getId(), liker.getId());
        this.comment = comment;
        this.liker = liker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentLike that = (CommentLike) o;
        return Objects.equals(commentLikeId, that.commentLikeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(commentLikeId);
    }

    public CommentLikeId getCommentLikeId() {
        return commentLikeId;
    }

    public void setCommentLikeId(CommentLikeId commentLikeId) {
        this.commentLikeId = commentLikeId;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public ChatUser getLiker() {
        return liker;
    }

    public void setLiker(ChatUser liker) {
        this.liker = liker;
    }
}

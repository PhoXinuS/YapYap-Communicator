package com.example.texttuah.embeddable;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class PostLikeId {
    private Long postId;
    private Long likerId;

    public PostLikeId() {
    }

    public PostLikeId(Long postId, Long likerId) {
        this.postId = postId;
        this.likerId = likerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostLikeId that = (PostLikeId) o;
        return Objects.equals(postId, that.postId) && Objects.equals(likerId, that.likerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, likerId);
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getLikerId() {
        return likerId;
    }

    public void setLikerId(Long likerId) {
        this.likerId = likerId;
    }
}

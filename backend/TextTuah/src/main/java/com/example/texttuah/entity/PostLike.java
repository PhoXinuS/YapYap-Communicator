package com.example.texttuah.entity;

import com.example.texttuah.embeddable.PostLikeId;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "POST_LIKES")
public class PostLike {
    @EmbeddedId
    private PostLikeId id;

    @MapsId("postId")
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @MapsId("likerId")
    @ManyToOne
    @JoinColumn(name = "LIKER_ID")
    private ChatUser liker;

    public PostLike() {
    }

    public PostLike(Post post, ChatUser liker) {
        this.id = new PostLikeId(post.getId(), liker.getId());
        this.post = post;
        this.liker = liker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostLike postLike = (PostLike) o;
        return Objects.equals(id, postLike.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public PostLikeId getId() {
        return id;
    }

    public void setId(PostLikeId id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public ChatUser getLiker() {
        return liker;
    }

    public void setLiker(ChatUser liker) {
        this.liker = liker;
    }
}

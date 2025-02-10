package com.example.texttuah.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="POSTS")
public class Post {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POSTER_ID", nullable = false)
    private ChatUser poster;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "POSTED_AT")
    private LocalDateTime postedAt;

    @OneToMany(mappedBy = "post")
    private Set<PostLike> likes = new HashSet<>();

    public Post() {
    }

    public Post(Long id, ChatUser poster, String content, LocalDateTime postedAt, Set<PostLike> likes) {
        this.id = id;
        this.poster = poster;
        this.content = content;
        this.postedAt = postedAt;
        this.likes = likes;
    }

    @PrePersist
    public void prePersist() {
        this.postedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
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

    public ChatUser getPoster() {
        return poster;
    }

    public void setPoster(ChatUser poster) {
        this.poster = poster;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public Set<PostLike> getLikes() {
        return likes;
    }

    public void setLikes(Set<PostLike> likes) {
        this.likes = likes;
    }
}


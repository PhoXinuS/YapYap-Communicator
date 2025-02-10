package com.example.texttuah.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "COMMENTS")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post commentedPost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COMMENTER_ID")
    private ChatUser commenter;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "comment")
    private Set<CommentLike> likes = new HashSet<>();

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "COMMENTED_AT")
    private LocalDateTime commentedAt;

    public Comment() {
    }

    public Comment(Long id, Post commentedPost, ChatUser commenter, String content, LocalDateTime commentedAt) {
        this.id = id;
        this.commentedPost = commentedPost;
        this.commenter = commenter;
        this.content = content;
        this.commentedAt = commentedAt;
    }

    @PrePersist
    private void prePersist() {
        commentedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
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

    public Post getCommentedPost() {
        return commentedPost;
    }

    public void setCommentedPost(Post commentedPost) {
        this.commentedPost = commentedPost;
    }

    public ChatUser getCommenter() {
        return commenter;
    }

    public void setCommenter(ChatUser commenter) {
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

    public Set<CommentLike> getLikes() {
        return likes;
    }

    public void setLikes(Set<CommentLike> likes) {
        this.likes = likes;
    }
}

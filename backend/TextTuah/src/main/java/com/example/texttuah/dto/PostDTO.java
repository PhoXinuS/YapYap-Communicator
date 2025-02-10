package com.example.texttuah.dto;

import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Post;
import com.example.texttuah.entity.PostLike;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class PostDTO {
    private Long id;
    private UserFriendDTO poster;
    private String content;
    private LocalDateTime postedAt;
    private Integer likes;
    private Boolean isLiked;

    public static PostDTO convertTo(Post post, ChatUser user) {
        return new PostDTO(
                post.getId(),
                UserFriendDTO.convertTo(post.getPoster()),
                post.getContent(),
                post.getPostedAt(),
                post.getLikes().size(),
                user.getPostLikes().contains(new PostLike(post, user))
        );
    }

    public static Set<PostDTO> convertTo(Set<Post> posts, ChatUser user) {
        return posts.stream()
                .map(post -> PostDTO.convertTo(post, user))
                .collect(Collectors.toSet());
    }

    public PostDTO(Long id, UserFriendDTO poster, String content, LocalDateTime postedAt, Integer likes, Boolean isLiked) {
        this.id = id;
        this.poster = poster;
        this.content = content;
        this.postedAt = postedAt;
        this.likes = likes;
        this.isLiked = isLiked;
    }

    public PostDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDTO postDTO = (PostDTO) o;
        return Objects.equals(id, postDTO.id);
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

    public UserFriendDTO getPoster() {
        return poster;
    }

    public void setPoster(UserFriendDTO poster) {
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

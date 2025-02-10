package com.example.texttuah.entity;


import jakarta.persistence.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "CHAT_USERS")
public class ChatUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "JOINED_AT")
    private LocalDateTime joinedAt;

    // Where the user is in the USER1_ID and his friend in USER2_ID
    @OneToMany(mappedBy = "user1", fetch = FetchType.LAZY)
    private Set<Friendship> friendshipsInitiated = new HashSet<>();

    // Where the user is in the USER2_ID and his friend in USER1_ID
    @OneToMany(mappedBy = "user2", fetch = FetchType.LAZY)
    private Set<Friendship> friendshipsReceived = new HashSet<>();

    @OneToMany(mappedBy = "requester", fetch = FetchType.LAZY)
    private Set<FriendshipRequest> friendshipsRequestsSent = new HashSet<>();

    @OneToMany(mappedBy = "requested", fetch = FetchType.LAZY)
    private Set<FriendshipRequest> friendshipsRequestsReceived = new HashSet<>();

    @OneToMany(mappedBy = "conversationMember", fetch = FetchType.LAZY)
    private Set<ConversationMembers> conversationMembers = new HashSet<>();

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private Set<ChatMessage> messages = new HashSet<>();

    @OneToMany(mappedBy = "liker", fetch = FetchType.LAZY)
    private Set<MessageLike> messageLikes = new HashSet<>();

    @OneToMany(mappedBy = "poster", fetch = FetchType.LAZY)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "commenter", fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "liker", fetch = FetchType.LAZY)
    private Set<PostLike> postLikes = new HashSet<>();

    @OneToMany(mappedBy = "liker", fetch = FetchType.LAZY)
    private Set<CommentLike> commentLikes = new HashSet<>();

    @Transient
    public Set<Conversation> getAllConversations() {
        return conversationMembers
                .stream()
                .map(ConversationMembers::getConversation)
                .collect(Collectors.toSet());
    }

    @Transient
    public Set<ChatUser> getAllFriends() {
        return Stream.concat(
                friendshipsInitiated.stream().map(Friendship::getUser2),
                friendshipsReceived.stream().map(Friendship::getUser1)
            ).collect(Collectors.toSet());
    }

    @Transient
    public Set<ChatUser> getAllChatUsersRequested() {
        return friendshipsRequestsSent.stream()
                .map(FriendshipRequest::getRequested).collect(Collectors.toSet());
    }

    @Transient
    public Set<ChatUser> getAllChatUsersWhoRequested() {
        return friendshipsRequestsReceived.stream()
                .map(FriendshipRequest::getRequester).collect(Collectors.toSet());
    }

    public ChatUser() {}

    public ChatUser(Long id, String email, String name, String password, LocalDateTime joinedAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;

        this.joinedAt = joinedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatUser user = (ChatUser) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @PrePersist
    private void setDefaultValue() {
        this.joinedAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Set<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(Set<ChatMessage> messages) {
        this.messages = messages;
    }

    public Set<MessageLike> getMessageLikes() {
        return messageLikes;
    }

    public void setMessageLikes(
            Set<MessageLike> messageLikes
    ) {
        this.messageLikes = messageLikes;
    }

    public Set<ConversationMembers> getConversationMembers() {
        return conversationMembers;
    }

    public void setConversationMembers(
            Set<ConversationMembers> conversationMembers
    ) {
        this.conversationMembers = conversationMembers;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<PostLike> getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(Set<PostLike> postLikes) {
        this.postLikes = postLikes;
    }

    public Set<CommentLike> getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(Set<CommentLike> commentLikes) {
        this.commentLikes = commentLikes;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

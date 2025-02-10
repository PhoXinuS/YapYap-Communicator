package com.example.texttuah.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Table(name = "CHAT_MESSAGES")
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_ID")
    private ChatUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONVERSATION_ID")
    private Conversation conversation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "message")
    private Set<MessageLike> likes = new HashSet<>();

    @Column(name = "MESSAGE_CONTENT")
    private String content;

    @Column(name = "SENT_AT")
    private LocalDateTime sentAt;

    public ChatMessage() {
    }

    public ChatMessage(
            Long id,
            ChatUser sender,
            Conversation conversation,
            Set<MessageLike> likes,
            String content,
            LocalDateTime sentAt
    ) {
        this.id = id;
        this.sender = sender;
        this.conversation = conversation;
        this.likes = likes;
        this.content = content;
        this.sentAt = sentAt;
    }

    @PrePersist
    public void prePersist() {
        sentAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Set<MessageLike> getLikes() {
        return likes;
    }

    public void setLikes(Set<MessageLike> likes) {
        this.likes = likes;
    }

    public boolean addLike(MessageLike like) {
        return likes.add(like);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatUser getSender() {
        return sender;
    }

    public void setSender(ChatUser sender) {
        this.sender = sender;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}

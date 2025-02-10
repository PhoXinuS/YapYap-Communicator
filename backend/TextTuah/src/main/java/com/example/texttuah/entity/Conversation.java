package com.example.texttuah.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "CONVERSATIONS")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "conversation")
    private Set<ConversationMembers> conversationMembers = new HashSet<>();

    @OneToMany(mappedBy = "conversation")
    private Set<ChatMessage> messages = new HashSet<>();


    @Transient
    public Set<ChatUser> getAllMembers() {
        return conversationMembers
                .stream()
                .map(ConversationMembers::getConversationMember)
                .collect(Collectors.toSet());
    }

    public Conversation(String name) {
        this.name = name;
    }

    public Conversation(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Conversation() {
    }

    @PrePersist
    public void setPrePersist() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<ConversationMembers> getConversationMembers() {
        return conversationMembers;
    }

    public void setConversationMembers(Set<ConversationMembers> conversationMembers) {
        this.conversationMembers = conversationMembers;
    }

    public Set<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(Set<ChatMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
    }
}

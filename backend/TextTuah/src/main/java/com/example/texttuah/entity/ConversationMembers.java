package com.example.texttuah.entity;

import com.example.texttuah.embeddable.ConversationMembersId;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CONVERSATION_MEMBERS")
public class ConversationMembers {
    @EmbeddedId
    private ConversationMembersId id;

    @Column(name = "JOINED_AT")
    private LocalDateTime joinedAt;

    @MapsId("conversationId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONVERSATION_ID", nullable = false)
    Conversation conversation;

    @MapsId("chatUserId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    ChatUser conversationMember;

    public ConversationMembers() {
    }

    public ConversationMembers(ConversationMembersId id, Conversation conversation, ChatUser conversationMember) {
        this.id = id;
        this.conversation = conversation;
        this.conversationMember = conversationMember;
    }

    public ConversationMembers(Conversation conversation, ChatUser conversationMember) {
        this.conversation = conversation;
        this.conversationMember = conversationMember;
    }

    public ConversationMembers(ConversationMembersId id, LocalDateTime joinedAt, Conversation conversation, ChatUser conversationMember) {
        this.id = id;
        this.joinedAt = joinedAt;
        this.conversation = conversation;
        this.conversationMember = conversationMember;
    }

    @PreUpdate
    @PrePersist
    public void onPrePersist() {
        joinedAt = LocalDateTime.now();
    }

    public ConversationMembersId getId() {
        return id;
    }

    public void setId(ConversationMembersId id) {
        this.id = id;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public ChatUser getConversationMember() {
        return conversationMember;
    }

    public void setConversationMember(ChatUser conversationMember) {
        this.conversationMember = conversationMember;
    }
}

package com.example.texttuah.embeddable;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ConversationMembersId {
    private long conversationId;
    private long chatUserId;

    public ConversationMembersId() {
    }

    public ConversationMembersId(long conversationId, long chatUserId) {
        this.conversationId = conversationId;
        this.chatUserId = chatUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationMembersId that = (ConversationMembersId) o;
        return Objects.equals(conversationId, that.conversationId) && Objects.equals(chatUserId, that.chatUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversationId, chatUserId);
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public long getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(long chatUserId) {
        this.chatUserId = chatUserId;
    }
}

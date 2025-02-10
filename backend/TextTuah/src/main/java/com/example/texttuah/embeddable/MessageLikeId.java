package com.example.texttuah.embeddable;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class MessageLikeId {
    private Long messageId;
    private Long likerId;

    public MessageLikeId() {
    }

    public MessageLikeId(Long messageId, Long likerId) {
        this.messageId = messageId;
        this.likerId = likerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageLikeId that = (MessageLikeId) o;
        return Objects.equals(messageId, that.messageId) && Objects.equals(likerId, that.likerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, likerId);
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getLikerId() {
        return likerId;
    }

    public void setLikerId(Long likerId) {
        this.likerId = likerId;
    }
}

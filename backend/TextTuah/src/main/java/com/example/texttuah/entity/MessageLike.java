package com.example.texttuah.entity;

import com.example.texttuah.embeddable.MessageLikeId;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "MESSAGE_LIKES")
public class MessageLike {
    @EmbeddedId
    private MessageLikeId id;

    @MapsId("messageId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE_ID")
    private ChatMessage message;

    @MapsId("likerId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIKER_ID")
    private ChatUser liker;

    public MessageLike() {
    }

    public MessageLike(ChatMessage message, ChatUser liker) {
        this.message = message;
        this.liker = liker;
        this.id = new MessageLikeId(message.getId(), liker.getId());
    }

    public MessageLike(MessageLikeId id, ChatMessage message, ChatUser liker) {
        this.id = id;
        this.message = message;
        this.liker = liker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageLike that = (MessageLike) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public MessageLikeId getId() {
        return id;
    }

    public void setId(MessageLikeId id) {
        this.id = id;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public ChatUser getLiker() {
        return liker;
    }

    public void setLiker(ChatUser liker) {
        this.liker = liker;
    }
}

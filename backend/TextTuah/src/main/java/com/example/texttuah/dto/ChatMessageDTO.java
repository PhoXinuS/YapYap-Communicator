package com.example.texttuah.dto;

import com.example.texttuah.entity.ChatMessage;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.MessageLike;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatMessageDTO {
    private Long id;
    private String content;
    private String senderName;
    private LocalDateTime sentAt;
    private int numberOfLikes;
    private boolean isLiked;

    public ChatMessageDTO() {
    }

    public ChatMessageDTO(Long id, String content, String senderName, LocalDateTime sentAt, int numberOfLikes, boolean isLiked) {
        this.id = id;
        this.content = content;
        this.senderName = senderName;
        this.sentAt = sentAt;
        this.numberOfLikes = numberOfLikes;
        this.isLiked = isLiked;
    }

    public static ChatMessageDTO convertTo(ChatMessage message, ChatUser user) {
        return new ChatMessageDTO(
                message.getId(),
                message.getContent(),
                message.getSender().getName(),
                message.getSentAt(),
                message.getLikes().size(),
                message.getLikes().contains(new MessageLike(message, user))
        );
    }

    public static Set<ChatMessageDTO> convertTo(Set<ChatMessage> messages, ChatUser user) {
        return messages.stream()
                .map(message -> ChatMessageDTO.convertTo(message, user))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessageDTO that = (ChatMessageDTO) o;
        return Objects.equals(id, that.id);
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}

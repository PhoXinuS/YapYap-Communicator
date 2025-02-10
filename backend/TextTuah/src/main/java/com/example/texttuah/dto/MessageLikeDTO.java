package com.example.texttuah.dto;

public class MessageLikeDTO {
    private Long messageId;
    private int numberOfLikes;

    public MessageLikeDTO() {
    }

    public MessageLikeDTO(Long messageId, int numberOfLikes) {
        this.messageId = messageId;
        this.numberOfLikes = numberOfLikes;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }
}

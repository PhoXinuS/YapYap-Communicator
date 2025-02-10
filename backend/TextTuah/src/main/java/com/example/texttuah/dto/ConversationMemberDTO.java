package com.example.texttuah.dto;

import com.example.texttuah.entity.ChatUser;

import java.util.Set;
import java.util.stream.Collectors;

public class ConversationMemberDTO {
    private String name;

    public ConversationMemberDTO() {
    }

    public ConversationMemberDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ConversationMemberDTO convertTo(ChatUser user) {
        return new ConversationMemberDTO(user.getName());
    }

    public static Set<ConversationMemberDTO> convertTo(Set<ChatUser> users) {
        return users
                .stream()
                .map(ConversationMemberDTO::convertTo)
                .collect(Collectors.toSet());
    }
}

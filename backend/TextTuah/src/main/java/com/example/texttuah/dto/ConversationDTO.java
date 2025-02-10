package com.example.texttuah.dto;

import com.example.texttuah.entity.Conversation;

import java.util.Set;
import java.util.stream.Collectors;

public class ConversationDTO {
    private Long id;
    private String name;
    private Set<ConversationMemberDTO> members;

    public ConversationDTO() {
    }

    public ConversationDTO(Long id, String name, Set<ConversationMemberDTO> members) {
        this.id = id;
        this.name = name;
        this.members = members;
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

    public Set<ConversationMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<ConversationMemberDTO> members) {
        this.members = members;
    }

    public static ConversationDTO convertTo(Conversation conversation) {
        if (conversation == null) return null;
        return new ConversationDTO(
                conversation.getId(),
                conversation.getName(),
                conversation.getAllMembers()
                        .stream()
                        .map(ConversationMemberDTO::convertTo)
                        .collect(Collectors.toSet())
        );
    }
}

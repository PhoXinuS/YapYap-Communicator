package com.example.texttuah.dto;

import com.example.texttuah.entity.ChatUser;

public class UserFriendDTO {
    private String name;
    private String email;

    public UserFriendDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static UserFriendDTO convertTo(ChatUser user) {
        return new UserFriendDTO(user.getName(), user.getEmail());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

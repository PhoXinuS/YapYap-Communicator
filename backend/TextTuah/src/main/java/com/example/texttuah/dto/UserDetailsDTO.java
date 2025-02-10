package com.example.texttuah.dto;

import com.example.texttuah.entity.ChatUser;

public class UserDetailsDTO {
    private String name;
    private String email;
    private Long id;

    public UserDetailsDTO() {
    }

    public UserDetailsDTO(String name, String email, Long id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public static UserDetailsDTO convertTo(ChatUser user){
        return new UserDetailsDTO(user.getName(), user.getEmail(), user.getId());
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

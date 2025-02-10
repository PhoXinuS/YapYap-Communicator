package com.example.texttuah.dto;

import org.springframework.lang.Nullable;

import java.util.Objects;

public class UserIdentificationDTO {
    private Long id;

    private String email;

    public UserIdentificationDTO() {
    }

    public UserIdentificationDTO(Long id) {
        this.id = id;
    }

    public UserIdentificationDTO(String email) {
        this.email = email;
    }

    public UserIdentificationDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserIdentificationDTO that = (UserIdentificationDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    public boolean isIdentificationByID() {
        return id != null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

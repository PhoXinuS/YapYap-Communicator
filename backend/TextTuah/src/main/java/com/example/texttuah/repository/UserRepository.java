package com.example.texttuah.repository;

import com.example.texttuah.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ChatUser, Long> {

    Optional<ChatUser> findByEmail(String email);

    Optional<List<ChatUser>> findByName(String name);
}

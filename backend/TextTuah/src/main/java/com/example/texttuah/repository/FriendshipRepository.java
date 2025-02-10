package com.example.texttuah.repository;

import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByUser1AndUser2(ChatUser user1, ChatUser user2);
    boolean existsByUser1AndUser2(ChatUser user1, ChatUser user2);
}

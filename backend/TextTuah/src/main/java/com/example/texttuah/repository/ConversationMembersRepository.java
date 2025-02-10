package com.example.texttuah.repository;


import com.example.texttuah.embeddable.ConversationMembersId;
import com.example.texttuah.entity.ConversationMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationMembersRepository extends JpaRepository<ConversationMembers, ConversationMembersId> {
}

package com.example.texttuah.repository;

import com.example.texttuah.embeddable.FriendshipRequestId;
import com.example.texttuah.entity.FriendshipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, FriendshipRequestId> {
}

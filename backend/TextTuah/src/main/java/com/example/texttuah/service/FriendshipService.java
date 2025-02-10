package com.example.texttuah.service;

import com.example.texttuah.dto.UserFriendDTO;
import com.example.texttuah.dto.UserIdentificationDTO;
import com.example.texttuah.embeddable.FriendshipRequestId;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Friendship;
import com.example.texttuah.entity.FriendshipRequest;
import com.example.texttuah.repository.FriendshipRepository;
import com.example.texttuah.repository.FriendshipRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FriendshipService {
    public final FriendshipRequestRepository friendshipRequestRepository;
    public final UserService userService;
    public final JWTService jwtService;
    public final FriendshipRepository friendshipRepository;


    @Autowired
    public FriendshipService(
            FriendshipRequestRepository friendshipRequestRepository,
            @Lazy UserService userService,
            JWTService jwtService,
            FriendshipRepository friendshipRepository) {
        this.userService = userService;
        this.friendshipRequestRepository = friendshipRequestRepository;
        this.jwtService = jwtService;
        this.friendshipRepository = friendshipRepository;
    }

    public FriendshipRequest getFriendshipRequest(
            UserIdentificationDTO requester,
            UserIdentificationDTO requested
    ) {
        FriendshipRequestId id = new FriendshipRequestId();
        Long requesterId =  userService.getUserId(requester);
        Long requestedId =  userService.getUserId(requested);

        if (requestedId == null || requesterId == null) return null;

        id.setRequesterId(requesterId);
        id.setRequesteeId(requestedId);

        Optional<FriendshipRequest> friendshipRequest = friendshipRequestRepository.findById(id);
        return friendshipRequest.orElse(null);
    }

    public Friendship getFriendship(
            UserIdentificationDTO userId1,
            UserIdentificationDTO userId2
    ) {
        ChatUser user1 = userService.findByUserIdentification(userId1);
        ChatUser user2 = userService.findByUserIdentification(userId2);

        if (user1 == null || user2 == null) {
            return null;
        }

        Friendship friendship = friendshipRepository
                .findByUser1AndUser2(user1, user2)
                .orElse(null);

        if (friendship == null) {
            friendship = friendshipRepository
                    .findByUser1AndUser2(user2, user1)
                    .orElse(null);
        }

        if (friendship == null) {
            return null;
        }

        return friendship;
    }

    @Transactional
    public UserFriendDTO acceptFriendshipRequest(UserIdentificationDTO requestedId,
                                                 UserIdentificationDTO requesterId) {
        FriendshipRequest friendshipRequest = getFriendshipRequest(requesterId, requestedId);


        if (friendshipRequest == null) {
            System.out.println("Frendship request is null");
            return null;
        }
        ChatUser user1 = userService.findByUserIdentification(requesterId);
        ChatUser user2 = userService.findByUserIdentification(requestedId);

        if (user1 == null || user2 == null) {
            System.out.println("user1 or user2 is null");
            return null;
        }

        Friendship friendship = new Friendship();
        friendship.setUser1(user1);
        friendship.setUser2(user2);

        friendshipRepository.save(friendship);
        friendshipRequestRepository.delete(friendshipRequest);

        // returning user from whom you accepted request
        return UserFriendDTO.convertTo(user1);
    }

    public UserFriendDTO rejectFriendshipRequest(UserIdentificationDTO requestedId,
                                                 UserIdentificationDTO requesterId) {

        FriendshipRequest friendshipRequest = getFriendshipRequest(requesterId, requestedId);

        if (friendshipRequest == null) {
            return null;
        }

        ChatUser requester = userService.findByUserIdentification(requesterId);
        if (requester == null) {
            return null;
        }

        friendshipRequestRepository.delete(friendshipRequest);
        return UserFriendDTO.convertTo(requester);
    }

    public UserFriendDTO removeFriend(UserIdentificationDTO userId,
                                       UserIdentificationDTO friendToRemove) {
        Friendship friendship = getFriendship(
                userId,
                friendToRemove
        );

        if (friendship == null) {
            System.out.println("friendship is null");
            return null;
        }

        friendshipRepository.delete(friendship);

        return UserFriendDTO.convertTo(friendship.getUser2());
    }

    public boolean areFriends(ChatUser requester, ChatUser requested) {
        return friendshipRepository.existsByUser1AndUser2(requester, requested) ||
               friendshipRepository.existsByUser1AndUser2(requested, requester);
    }
}

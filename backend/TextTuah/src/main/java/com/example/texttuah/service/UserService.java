package com.example.texttuah.service;

import com.example.texttuah.dto.UserDetailsDTO;
import com.example.texttuah.dto.UserFriendDTO;
import com.example.texttuah.dto.UserIdentificationDTO;
import com.example.texttuah.embeddable.FriendshipRequestId;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.FriendshipRequest;
import com.example.texttuah.repository.FriendshipRequestRepository;
import com.example.texttuah.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRequestRepository friendshipRequestRepository;
    private final FriendshipService friendshipService;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Autowired
    public UserService(UserRepository userRepository,
                       @Lazy BCryptPasswordEncoder encoder,
                       @Lazy AuthenticationManager authenticationManager,
                       @Lazy FriendshipService friendshipService,
                       JWTService jwtService,
                       FriendshipRequestRepository friendshipRequestRepository
    ) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.friendshipRequestRepository = friendshipRequestRepository;
        this.friendshipService = friendshipService;
    }



    public ChatUser findByEmail(String email) {
        Optional<ChatUser> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    public ChatUser findById(Long id) {
        Optional<ChatUser> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    public ChatUser findByUserIdentification(UserIdentificationDTO userIdentificationDTO) {
        ChatUser user = userIdentificationDTO.isIdentificationByID() ?
                findById(userIdentificationDTO.getId()) :
                findByEmail(userIdentificationDTO.getEmail());

        if (user == null) {
            return null;
        }

        return user;
    }

    public Long getUserId(UserIdentificationDTO userIdentificationDTO) {
        if (userIdentificationDTO.isIdentificationByID()) {
            return userIdentificationDTO.getId();
        } else {
            ChatUser user = findByEmail(userIdentificationDTO.getEmail());
            if (user == null) return null;
            return user.getId();
        }
    }

    public List<ChatUser> findByName(String name) {
        Optional<List<ChatUser>> users = userRepository.findByName(name);
        if (users.isEmpty()) {
            return null;
        }
        return users.get();
    }

    public Set<UserFriendDTO> findAllFriends(String token) {
        String email = jwtService.extractEmail(token);
        ChatUser user = findByEmail(email);
        return user != null ? user.getAllFriends()
                .stream()
                .map(UserFriendDTO::convertTo)
                .collect(Collectors.toSet())
                : Collections.emptySet();
    }

    public Set<UserFriendDTO> findAllChatUsersRequested(String token) {
        String email = jwtService.extractEmail(token);
        ChatUser user = findByEmail(email);
        return user != null ? user.getAllChatUsersRequested()
                .stream()
                .map(UserFriendDTO::convertTo)
                .collect(Collectors.toSet())
                : Collections.emptySet();
    }

    public Set<UserFriendDTO> findAllChatUsersWhoRequested(String token) {
        String email = jwtService.extractEmail(token);
        ChatUser user = findByEmail(email);
        return user != null ? user.getAllChatUsersWhoRequested()
                .stream()
                .map(UserFriendDTO::convertTo)
                .collect(Collectors.toSet())
                : Collections.emptySet();
    }

    public String changeName(String newName, UserIdentificationDTO userIdentification) {
        ChatUser user = findByUserIdentification(userIdentification);
        if (user == null) {
            return null;
        }
        if (newName == null || newName.isBlank()) {
            return null;
        }
        user.setName(newName);
        userRepository.save(user);
        return newName;
    }

    @Transactional
    public UserFriendDTO sendFriendRequest(UserIdentificationDTO requestedUserIdentification, String token){
        String requesterEmail = jwtService.extractEmail(token);

        ChatUser requester;
        ChatUser requested;

        requested = requestedUserIdentification.isIdentificationByID() ?
                findById(requestedUserIdentification.getId()) :
                findByEmail(requestedUserIdentification.getEmail());

        requester = findByEmail(
                jwtService.extractEmail(token)
        );

        if (requester == null || requested == null) {
            return null;
        }

        if (friendshipRequestRepository.existsById(new FriendshipRequestId(requester.getId(), requested.getId()))) {
            return null;
        }

        // checking if users are already friends
        if (friendshipService.areFriends(requester, requested)) {
            // Users are already friends
            return null;
        }

        FriendshipRequest friendshipRequest = new FriendshipRequest();
        friendshipRequest.setRequester(requester);
        friendshipRequest.setRequested(requested);
        friendshipRequest.setId(
                new FriendshipRequestId(
                        requester.getId(),
                        requested.getId()
                )
        );

        friendshipRequestRepository.save(friendshipRequest);

        return UserFriendDTO.convertTo(requested);
    }


    @Transactional
    public ChatUser save(ChatUser chatUser) {
        chatUser.setPassword(encoder.encode(chatUser.getPassword()));
        return userRepository.save(chatUser);
    }

    public String verify(ChatUser chatUser) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(chatUser.getEmail(), chatUser.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(chatUser.getEmail());
        }
        return "Fail";
    }


    public UserDetailsDTO getUserDetails(String email) {
        ChatUser user = findByEmail(email);
        if (user == null) {
            return null;
        }

        return UserDetailsDTO.convertTo(user);
    }
}

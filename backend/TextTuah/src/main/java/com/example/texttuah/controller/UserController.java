package com.example.texttuah.controller;

import com.example.texttuah.dto.UserDetailsDTO;
import com.example.texttuah.dto.UserFriendDTO;
import com.example.texttuah.dto.UserIdentificationDTO;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.service.FriendshipService;
import com.example.texttuah.service.JWTService;
import com.example.texttuah.service.RequestService;
import com.example.texttuah.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    private final RequestService requestService;
    private final FriendshipService friendshipService;
    private final JWTService jwtService;

    @Autowired
    public UserController(
            UserService service,
            RequestService requestService,
            FriendshipService friendshipService,
            JWTService jwtService
    ) {
        this.service = service;
        this.requestService = requestService;
        this.friendshipService = friendshipService;
        this.jwtService = jwtService;
    }

    /* Following endpoints are for admins */

    @GetMapping("")
    public UserDetailsDTO getUser(
            HttpServletRequest request) {
        return service.getUserDetails(
                jwtService.extractEmail(
                        requestService.getJwtToken(request)
                )
        );
    }

    /* Following endpoint are for users  */

    @PatchMapping("/name/change/{newName}")
    public String changeUserName(@PathVariable(required = true) String newName,
                                 HttpServletRequest request) {
        return service.changeName (
                newName,
                jwtService.getUserIdentification(
                        requestService.getJwtToken(request)
                )
        );
    }

    @GetMapping("/friends")
    public Set<UserFriendDTO> getUserAllFriends(HttpServletRequest request) {
        return service.findAllFriends(
                requestService.getJwtToken(request)
        );
    }

    @PostMapping ("/friends/remove")
    public UserFriendDTO removeFriend(@RequestBody UserIdentificationDTO friendToRemove,
                                      HttpServletRequest request) {
        return friendshipService.removeFriend(
                // User who is removing friend
                jwtService.getUserIdentification(
                        requestService.getJwtToken(request)
                ),
                friendToRemove
        );
    }

    @GetMapping("/friends/requests/sent")
    public Set<UserFriendDTO> getSentFriendsRequests(HttpServletRequest request) {
        return service.findAllChatUsersRequested(
                requestService.getJwtToken(request)
        );
    }

    @GetMapping("/friends/requests/received")
    public Set<UserFriendDTO> getReceivedFriendsRequests(HttpServletRequest request) {
        return service.findAllChatUsersWhoRequested(
                requestService.getJwtToken(request)
        );
    }

    @PostMapping("/friends/requests/send")
    public UserFriendDTO sendFriendRequest(@RequestBody UserIdentificationDTO userIdentificationDTO,
                                           HttpServletRequest request) {
        return service.sendFriendRequest(
                userIdentificationDTO,
                requestService.getJwtToken(request)
        );
    }

    @PostMapping("/friends/requests/accept")
    public UserFriendDTO acceptFriendRequest(@RequestBody UserIdentificationDTO requesterId,
                                             HttpServletRequest request) {
        return friendshipService.acceptFriendshipRequest(
            // only user who was requested can accept request
            jwtService.getUserIdentification(
                    requestService.getJwtToken(request)
            ),
            // user who sent request
            requesterId
        );
    }

    @PostMapping("/friends/requests/reject")
    public UserFriendDTO rejectFriendRequest(@RequestBody UserIdentificationDTO requesterId,
                                             HttpServletRequest request) {
        return friendshipService.rejectFriendshipRequest(
                jwtService.getUserIdentification(
                        requestService.getJwtToken(request)
                ),
                requesterId
        );
    }

    /* Following endpoints are public. */

    @PostMapping("/register")
    public ChatUser register(@RequestBody ChatUser chatUser) {
        return service.save(chatUser);
    }

    @PostMapping("/login")
    public String login(@RequestBody ChatUser chatUser) {
        // Returning JWT
        return service.verify(chatUser);
    }
}

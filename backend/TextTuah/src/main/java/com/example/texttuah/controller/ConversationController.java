package com.example.texttuah.controller;

import com.example.texttuah.dto.ConversationDTO;
import com.example.texttuah.dto.UserIdentificationDTO;
import com.example.texttuah.service.ConversationService;
import com.example.texttuah.service.JWTService;
import com.example.texttuah.service.RequestService;
import com.example.texttuah.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/conversations")
public class ConversationController {
    RequestService requestService;
    JWTService jwtService;
    ConversationService conversationService;
    UserService userService;

    @Autowired
    public ConversationController(
            RequestService requestService,
            JWTService jwtService,
            ConversationService conversationService,
            UserService userService) {

        this.requestService = requestService;
        this.jwtService = jwtService;
        this.conversationService = conversationService;
        this.userService = userService;
    }

    @GetMapping("")
    public Set<ConversationDTO> getUserConversations(HttpServletRequest request) {
        return conversationService.getAllUserConversations(
                jwtService.extractEmail(
                        requestService.getJwtToken(request)
                )
        );
    }

    @PostMapping("/create/{conversationName}")
    public ConversationDTO createNewConversation(
            @RequestBody Set<UserIdentificationDTO> users,
            HttpServletRequest request,
            @PathVariable(required = true) String conversationName
    ) {
        users.add(new UserIdentificationDTO(
                jwtService.extractEmail(
                        requestService.getJwtToken(request)
                )
        ));
        return conversationService.createNewConversation(
            users, conversationName
        );
    }

    @DeleteMapping("/leave/{conversationId}")
    public boolean leaveConversation(
            HttpServletRequest request,
            @PathVariable(required = true) Long conversationId
    ) {
        return conversationService.deleteUserFromConversation(
                jwtService.extractEmail(
                        requestService.getJwtToken(request)
                ),
                conversationId
        );
    }

    @PostMapping("/add/{conversationId}")
    public void addConversationMember(
            @PathVariable long conversationId,
            HttpServletRequest request,
            @RequestBody Set<UserIdentificationDTO> usersIdentifications
    ) {
        conversationService.addConversationMembers(
                jwtService.extractEmail(
                        requestService.getJwtToken(request)
                ),
                conversationId,
                usersIdentifications
        );
    }

}

package com.example.texttuah.controller;

import com.example.texttuah.dto.ChatMessageDTO;
import com.example.texttuah.dto.MessageContentDTO;
import com.example.texttuah.entity.ChatMessage;
import com.example.texttuah.service.JWTService;
import com.example.texttuah.service.MessageService;
import com.example.texttuah.service.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/messages")
public class MessageController {
    private final JWTService jwtService;
    private final RequestService requestService;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageController(
            JWTService jwtService,
            RequestService requestService,
            MessageService messageService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.jwtService = jwtService;
        this.requestService = requestService;
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/{conversationId}")
    public Set<ChatMessageDTO> getAllConversationMessages(
            HttpServletRequest request,
            @PathVariable Long conversationId
    ) {
        return messageService.getAllConversationMessages(
                jwtService.extractEmail(
                        requestService.getJwtToken(request)
                ),
                conversationId
        );
    }

    @PostMapping("/add/{conversationId}")
    public ChatMessageDTO writeMessage(
            HttpServletRequest request,
            @PathVariable Long conversationId,
            @RequestBody MessageContentDTO messageContent
    ) {
        // Extract the sender's email
        String email = jwtService.extractEmail(requestService.getJwtToken(request));
        return messageService.addMessage(email, conversationId, messageContent);
    }

    @PostMapping("/toggle-like/{messageId}")
    public boolean likeMessage(
            HttpServletRequest request,
            @PathVariable Long messageId
    ) {
        return messageService.toggleLikeMessage(
            jwtService.extractEmail(
                    requestService.getJwtToken(request)
            ),
            messageId
        );
    }
}

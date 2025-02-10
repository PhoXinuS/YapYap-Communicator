package com.example.texttuah.service;

import com.example.texttuah.dto.ChatMessageDTO;
import com.example.texttuah.dto.MessageContentDTO;
import com.example.texttuah.dto.MessageLikeDTO;
import com.example.texttuah.entity.ChatMessage;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Conversation;
import com.example.texttuah.entity.MessageLike;
import com.example.texttuah.repository.ChatMessageRepository;
import com.example.texttuah.repository.ConversationRepository;
import com.example.texttuah.repository.MessageLikeRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final UserService userService;
    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ConversationService conversationService;
    private final MessageLikeRepositoy messageLikeRepositoy;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageService(
            UserService userService,
            ConversationRepository conversationRepository,
            ChatMessageRepository chatMessageRepository,
            ConversationService conversationService,
            MessageLikeRepositoy messageLikeRepositoy, SimpMessagingTemplate messagingTemplate
    ) {
        this.userService = userService;
        this.conversationRepository = conversationRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.conversationService = conversationService;
        this.messageLikeRepositoy = messageLikeRepositoy;
        this.messagingTemplate = messagingTemplate;
    }

    public Set<ChatMessageDTO> getAllConversationMessages(
            String email,
            Long conversationId
    ) {
        ChatUser user = userService.findByEmail(email);
        if (user == null) {
            return null;
        }
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        if (conversation == null) {
            return null;
        }

        // Checking if user is in conversation, if not he isn't allowed to get messages
        if (!conversation.getAllMembers().contains(user)) {
            return null;
        }

        // User is allowed to get messages
        return ChatMessageDTO.convertTo(conversation.getMessages(), user);
    }

    public ChatMessageDTO addMessage(
            String senderEmail,
            Long conversationId,
            MessageContentDTO messageContentDTO
    ) {
        ChatUser user = userService.findByEmail(senderEmail);
        if (user == null) {
            return null;
        }

        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        if (conversation == null) {
            return null;
        }

        if (!conversationService.isUserMemberOfConversation(user, conversation))
            return null;


        // User is a member of conversation
        ChatMessage message = new ChatMessage();
        message.setContent(messageContentDTO.getContent());
        message.setSender(user);
        message.setConversation(conversation);

        Set<String> participants = conversation.getAllMembers().stream().map(ChatUser::getEmail).collect(Collectors.toSet());
        ChatMessageDTO messageDTO = ChatMessageDTO.convertTo(chatMessageRepository.save(message), user);

        participants.forEach(username -> {
                        System.out.println(username);
                messagingTemplate.convertAndSendToUser(username, "/topic/conversation/" + conversationId, messageDTO);}
        );
        return messageDTO;
    }

    public boolean toggleLikeMessage(String likerEmail, Long messageId) {
        ChatUser user = userService.findByEmail(likerEmail);
        ChatMessage message = chatMessageRepository.findById(messageId).orElse(null);
        assert message != null;
        if (user == null) {
            throw new RuntimeException();
        }
        if (message == null) {
            throw new RuntimeException();
        }

        MessageLike messageLike = new MessageLike(message, user);
        int initialSize = message.getLikes().size();
        boolean isLiked;
        if (message.getLikes().contains(messageLike)) {
            // user already likes message
            messageLikeRepositoy.delete(messageLike);
            isLiked = false;
        } else {
            // user didn't like message
            messageLikeRepositoy.save(messageLike);
            isLiked = true;
        }
        broadcastToggleLike(message.getConversation(), isLiked, initialSize, messageId);
        return isLiked;
    }

    private void broadcastToggleLike(Conversation conversation, boolean isLiked, int initialSize, Long messageId) {
        initialSize += isLiked ? 1 : -1;
        Set<String> participants = conversation.getAllMembers().stream().map(ChatUser::getEmail).collect(Collectors.toSet());
        MessageLikeDTO messageLikeDTO = new MessageLikeDTO(messageId, initialSize);
        participants.forEach(username -> {
            System.out.println(username);
            messagingTemplate.convertAndSendToUser(username, "/topic/likes/" + conversation.getId(), messageLikeDTO);
        });
    }


    public boolean isUserInConversation(String email, Long conversationId) {
        ChatUser user = userService.findByEmail(email);
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        assert conversation != null;
        return conversationService.isUserMemberOfConversation(user, conversation);
    }
}

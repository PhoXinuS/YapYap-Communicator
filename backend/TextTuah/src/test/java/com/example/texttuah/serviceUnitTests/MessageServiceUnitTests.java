package com.example.texttuah.serviceUnitTests;

import com.example.texttuah.dto.ChatMessageDTO;
import com.example.texttuah.dto.MessageContentDTO;
import com.example.texttuah.entity.ChatMessage;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Conversation;
import com.example.texttuah.entity.ConversationMembers;
import com.example.texttuah.repository.ChatMessageRepository;
import com.example.texttuah.repository.ConversationRepository;
import com.example.texttuah.repository.MessageLikeRepositoy;
import com.example.texttuah.service.ConversationService;
import com.example.texttuah.service.MessageService;
import com.example.texttuah.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MessageServiceUnitTests {

    @Mock
    private UserService userService;

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ConversationService conversationService;

    @Mock
    private MessageLikeRepositoy messageLikeRepositoy;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllConversationMessages_Success() {
        ChatUser user = new ChatUser(1L, "test@example.com", "Test", "password", LocalDateTime.now());
        user.setEmail("test@example.com");
        Conversation conversation = new Conversation();
        conversation.setId(1L);
        ConversationMembers member = new ConversationMembers(conversation, user);
        conversation.setConversationMembers(Set.of(member));
        ChatMessage message = new ChatMessage();
        message.setContent("Test Message");
        message.setSender(user);
        conversation.setMessages(Set.of(message));

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(conversationRepository.findById(1L)).thenReturn(Optional.of(conversation));

        Set<ChatMessageDTO> messages = messageService.getAllConversationMessages("test@example.com", 1L);

        assertNotNull(messages);
        assertFalse(messages.isEmpty());
        assertEquals("Test Message", messages.iterator().next().getContent());
    }

    @Test
    void testGetAllConversationMessages_UserNotInConversation() {
        ChatUser user = new ChatUser(1L, "test@example.com", "Test", "password", LocalDateTime.now());
        user.setEmail("test@example.com");
        Conversation conversation = new Conversation();
        conversation.setId(1L);
        conversation.setConversationMembers(new HashSet<>());

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(conversationRepository.findById(1L)).thenReturn(Optional.of(conversation));

        Set<ChatMessageDTO> messages = messageService.getAllConversationMessages("test@example.com", 1L);

        assertNull(messages);
    }

    @Test
    void testAddMessage_Success() {
        ChatUser user = new ChatUser(1L, "test@example.com", "Test", "password", LocalDateTime.now());
        user.setEmail("test@example.com");
        Conversation conversation = new Conversation();
        conversation.setId(1L);
        ConversationMembers member = new ConversationMembers(conversation, user);
        conversation.setConversationMembers(Set.of(member));
        MessageContentDTO messageContentDTO = new MessageContentDTO();
        messageContentDTO.setContent("Test Message");

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(conversationRepository.findById(1L)).thenReturn(Optional.of(conversation));
        when(conversationService.isUserMemberOfConversation(user, conversation)).thenReturn(true);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatMessageDTO chatMessageDTO = messageService.addMessage("test@example.com", 1L, messageContentDTO);

        assertNotNull(chatMessageDTO);
        assertEquals("Test Message", chatMessageDTO.getContent());
    }

    @Test
    void testAddMessage_UserNotMemberOfConversation() {
        ChatUser user = new ChatUser(1L, "test@example.com", "Test", "password", LocalDateTime.now());
        user.setEmail("test@example.com");
        Conversation conversation = new Conversation();
        conversation.setId(1L);
        MessageContentDTO messageContentDTO = new MessageContentDTO();
        messageContentDTO.setContent("Test Message");

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(conversationRepository.findById(1L)).thenReturn(Optional.of(conversation));
        when(conversationService.isUserMemberOfConversation(user, conversation)).thenReturn(false);

        ChatMessageDTO chatMessageDTO = messageService.addMessage("test@example.com", 1L, messageContentDTO);

        assertNull(chatMessageDTO);
    }
}
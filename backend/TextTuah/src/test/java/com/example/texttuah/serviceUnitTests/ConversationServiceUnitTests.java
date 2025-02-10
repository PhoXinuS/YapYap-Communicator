package com.example.texttuah.serviceUnitTests;

import com.example.texttuah.dto.ConversationDTO;
import com.example.texttuah.dto.UserIdentificationDTO;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Conversation;
import com.example.texttuah.entity.ConversationMembers;
import com.example.texttuah.repository.ConversationMembersRepository;
import com.example.texttuah.repository.ConversationRepository;
import com.example.texttuah.service.ConversationService;
import com.example.texttuah.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConversationServiceUnitTests {

    @Mock
    private UserService userService;

    @Mock
    private ConversationMembersRepository conversationMembersRepository;

    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private ConversationService conversationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUserConversations_Success() {
        String email = "test@example.com";
        ChatUser user = new ChatUser();
        user.setEmail(email);
        Conversation conversation = new Conversation("Test Conversation");
        ConversationMembers member = new ConversationMembers(conversation, user);
        Set<ConversationMembers> members = Set.of(member);
        conversation.setConversationMembers(members);

        user.setConversationMembers(members);


        when(userService.findByEmail(email)).thenReturn(user);

        Set<ConversationDTO> result = conversationService.getAllUserConversations(email);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllUserConversations_UserNotFound() {
        String email = "test@example.com";

        when(userService.findByEmail(email)).thenReturn(null);

        Set<ConversationDTO> result = conversationService.getAllUserConversations(email);

        assertNull(result);
    }

    @Test
    void testCreateNewConversation_Success() {
        Set<UserIdentificationDTO> users = new HashSet<>();
        users.add(new UserIdentificationDTO(1L, "user1@example.com"));
        users.add(new UserIdentificationDTO(2L, "user2@example.com"));
        String name = "New Conversation";
        ChatUser user1 = new ChatUser(1L, "user1@example.com", "user1", "password1", null);
        ChatUser user2 = new ChatUser(2L, "user2@example.com", "user2", "password2", null);
        Conversation conversation = new Conversation(1L, name, LocalDateTime.now());

        when(userService.findByUserIdentification(any(UserIdentificationDTO.class)))
                .thenReturn(user1)
                .thenReturn(user2);
        when(conversationRepository.save(any(Conversation.class))).thenReturn(conversation);

        ConversationDTO result = conversationService.createNewConversation(users, name);

        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(conversationRepository, times(1)).save(any(Conversation.class));
        verify(conversationMembersRepository, times(2)).save(any(ConversationMembers.class));
    }

    @Test
    void testCreateNewConversation_InvalidName() {
        Set<UserIdentificationDTO> users = new HashSet<>();
        users.add(new UserIdentificationDTO(1L, "user1@example.com"));
        String name = "";

        ConversationDTO result = conversationService.createNewConversation(users, name);

        assertNull(result);
        verify(conversationRepository, never()).save(any(Conversation.class));
    }

    @Test
    void testDeleteUserFromConversation_Success() {
        String email = "test@example.com";
        Long conversationId = 1L;
        ChatUser user = new ChatUser(1L, "test@example.com", "Test", "password", LocalDateTime.now());
        Conversation conversation = new Conversation(1L, "Test Conversation", LocalDateTime.now());
        conversation.setId(conversationId);
        ConversationMembers member = new ConversationMembers(conversation, user);
        Set<ConversationMembers> members = Set.of(member);
        conversation.setConversationMembers(members);

        when(userService.findByEmail(email)).thenReturn(user);
        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));

        boolean result = conversationService.deleteUserFromConversation(email, conversationId);

        assertTrue(result);
        verify(conversationMembersRepository, times(1)).deleteById(any());
    }

    @Test
    void testDeleteUserFromConversation_ConversationNotFound() {
        String email = "test@example.com";
        Long conversationId = 1L;

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.empty());

        boolean result = conversationService.deleteUserFromConversation(email, conversationId);

        assertFalse(result);
        verify(conversationMembersRepository, never()).deleteById(any());
    }

    @Test
    void testAddConversationMembers_Success() {
        String email = "test@example.com";
        Long conversationId = 1L;
        Set<UserIdentificationDTO> usersIds = new HashSet<>();
        usersIds.add(new UserIdentificationDTO(1L, "user1@example.com"));
        ChatUser sender = new ChatUser();
        sender.setEmail(email);
        Conversation conversation = new Conversation("Test Conversation");
        conversation.setId(conversationId);
        ConversationMembers member = new ConversationMembers(conversation, sender);
        Set<ConversationMembers> members = Set.of(member);
        conversation.setConversationMembers(members);
        conversation.setConversationMembers(members);
        ChatUser user1 = new ChatUser(1L, "user1@example.com", "user1", "password1", null);

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(userService.findByEmail(email)).thenReturn(sender);
        when(userService.findByUserIdentification(any(UserIdentificationDTO.class))).thenReturn(user1);

        conversationService.addConversationMembers(email, conversationId, usersIds);

        verify(conversationMembersRepository, times(1)).save(any(ConversationMembers.class));
    }

    @Test
    void testAddConversationMembers_ConversationNotFound() {
        String email = "test@example.com";
        Long conversationId = 1L;
        Set<UserIdentificationDTO> usersIds = new HashSet<>();

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.empty());

        conversationService.addConversationMembers(email, conversationId, usersIds);

        verify(conversationMembersRepository, never()).save(any(ConversationMembers.class));
    }
}
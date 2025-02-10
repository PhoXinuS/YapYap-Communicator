package com.example.texttuah.service;

import com.example.texttuah.dto.ConversationDTO;
import com.example.texttuah.dto.ConversationMemberDTO;
import com.example.texttuah.dto.UserIdentificationDTO;
import com.example.texttuah.embeddable.ConversationMembersId;
import com.example.texttuah.entity.ChatUser;
import com.example.texttuah.entity.Conversation;
import com.example.texttuah.entity.ConversationMembers;
import com.example.texttuah.repository.ConversationMembersRepository;
import com.example.texttuah.repository.ConversationRepository;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConversationService {
    private final UserService userService;
    private final ConversationMembersRepository conversationMembersRepository;
    private final ConversationRepository conversationRepository;
    @Value("${minimal_conversation_members}")
    private int minimal_size;

    @Autowired
    public ConversationService(
            UserService userService,
            ConversationMembersRepository conversationMembersRepository,
            ConversationRepository conversationRepository
    ) {
        this.userService = userService;
        this.conversationMembersRepository = conversationMembersRepository;
        this.conversationRepository = conversationRepository;
    }

    public Set<ConversationDTO> getAllUserConversations(String email) {
        ChatUser user = userService.findByEmail(email);
        if (user == null) {
            return null;
        }

        return user.getAllConversations()
                .stream()
                .map(ConversationDTO::convertTo)
                .collect(Collectors.toSet());
    }

    @Transactional
    public ConversationDTO createNewConversation(
            Set<UserIdentificationDTO> users,
            String name) {

        if (name == null || name.isBlank() ) {
            return null;
        }

        if (users.size() < minimal_size) {
            return null;
        }

        Conversation conversation = new Conversation(name);
        conversation = conversationRepository.save(conversation);
        Set<ChatUser> members = new HashSet<>();

        if (conversation == null) {
            return null;
        }

        for(UserIdentificationDTO user : users) {
            ChatUser chatUser = userService.findByUserIdentification(user);
            if (chatUser == null || members.contains(chatUser)) {
                continue;
            }

            ConversationMembersId id = new ConversationMembersId(conversation.getId(), chatUser.getId());
            ConversationMembers conversationMembers = new ConversationMembers(id, conversation, chatUser);

            if (conversationMembers == null) {
                continue;
            }
            conversationMembersRepository.save(conversationMembers);
            members.add(chatUser);
        }

        // Checking if created conversation is correct

        return new ConversationDTO(
                conversation.getId(),
                conversation.getName(),
                ConversationMemberDTO.convertTo(members));
    }

    @Transactional
    public boolean deleteUserFromConversation(String userEmail, Long conversationId) {
        if (conversationId == null) {
            return false;
        }

        ChatUser user = userService.findByEmail(userEmail);
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);

        if (conversation == null) {
            return false;
        }
        if (user == null) {
            return false;
        }

        if (!isUserMemberOfConversation(user, conversation)) {
            return false;
        }

        ConversationMembersId conversationMembersId = new ConversationMembersId(
                conversation.getId(),
                user.getId()
        );

        int amountOfMembers = conversation.getAllMembers().size() - 1;

        conversationMembersRepository.deleteById(conversationMembersId);
        if (amountOfMembers < minimal_size) {
            deleteConversation(conversation);
        }

        return true;
    }

    public void deleteConversation(Conversation conversation) {
        conversationMembersRepository.deleteAll(conversation.getConversationMembers());
        conversationRepository.delete(conversation);
    }

    public void addConversationMembers(
            String email,
            Long conversationId,
            Set<UserIdentificationDTO> usersIds
    ) {
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        ChatUser sender = userService.findByEmail(email);

        // user who wants to add is not in that conversation
        if (conversation == null || !conversation.getAllMembers().contains(sender)) {
            return;
        }
        Set<ChatUser> members = new HashSet<>();

        for (UserIdentificationDTO userId : usersIds) {
            ChatUser user = userService.findByUserIdentification(userId);
            if (user == null) continue;
            if (members.contains(user)) continue; // In case if sent request contains the same members
            addConversationMember(conversation, user);
            members.add(user);
        }
    }

    public void addConversationMember(
            Conversation conversation,
            ChatUser user
    ) {
        ConversationMembersId conversationMembersId =
                new ConversationMembersId(conversation.getId(), user.getId());

        ConversationMembers conversationMembers = new ConversationMembers();
        conversationMembers.setId(conversationMembersId);
        conversationMembers.setConversation(conversation);
        conversationMembers.setConversationMember(user);

        conversationMembersRepository.save(conversationMembers);
    }

    public boolean isUserMemberOfConversation(ChatUser user, Conversation conversation) {
        return conversation.getAllMembers().contains(user);
    }

    public boolean isUserInConversation(String email, Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        if (conversation == null) {
            return false;
        }
        ChatUser user = userService.findByEmail(email);
        if (user == null) {
            return false;
        }
        return conversation.getAllMembers().contains(user);
    }
}

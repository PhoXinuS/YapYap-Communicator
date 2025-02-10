//package com.example.texttuah.integrationTests;
//
//import com.example.texttuah.entity.*;
//import com.example.texttuah.embeddable.*;
//import com.example.texttuah.repository.*;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.junit.jupiter.api.Nested;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@SpringJUnitConfig
//@Transactional
//public class RepositoriesIntegrationTests {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private DatabaseCleaner databaseCleaner;
//
//    @Autowired
//    private FriendshipRepository friendshipRepository;
//
//    @Autowired
//    private ChatMessageRepository chatMessageRepository;
//
//    @Autowired
//    private ConversationMembersRepository conversationMembersRepository;
//
//    @Autowired
//    private ConversationRepository conversationRepository;
//
//    @Autowired
//    private FriendshipRequestRepository friendshipRequestRepository;
//
//    @Autowired
//    private MessageLikeRepositoy messageLikeRepositoy;
//
//    @Nested
//    class UserRepositoryTests {
//
//        @BeforeEach
//        void setUp() {
//            databaseCleaner.clean();
//        }
//
//        @Test
//        void testSaveAndFindById() {
//            ChatUser user = new ChatUser();
//            user.setEmail("test@example.com");
//            user.setName("Test User");
//            user.setPassword("password");
//            user.setJoinedAt(LocalDateTime.now());
//
//            ChatUser savedUser = userRepository.save(user);
//            Optional<ChatUser> foundUser = userRepository.findById(savedUser.getId());
//
//            assertTrue(foundUser.isPresent());
//            assertEquals("test@example.com", foundUser.get().getEmail());
//            assertEquals("Test User", foundUser.get().getName());
//        }
//
//        @Test
//        void testFindByEmail() {
//            ChatUser user = new ChatUser();
//            user.setEmail("test@example.com");
//            user.setName("Test User");
//            user.setPassword("password");
//            user.setJoinedAt(LocalDateTime.now());
//
//            userRepository.save(user);
//            Optional<ChatUser> foundUser = userRepository.findByEmail("test@example.com");
//
//            assertTrue(foundUser.isPresent());
//            assertEquals("test@example.com", foundUser.get().getEmail());
//            assertEquals("Test User", foundUser.get().getName());
//        }
//
//        @Test
//        void testDeleteUser() {
//            ChatUser user = new ChatUser();
//            user.setEmail("test@example.com");
//            user.setName("Test User");
//            user.setPassword("password");
//            user.setJoinedAt(LocalDateTime.now());
//
//            ChatUser savedUser = userRepository.save(user);
//            userRepository.deleteById(savedUser.getId());
//            Optional<ChatUser> foundUser = userRepository.findById(savedUser.getId());
//
//            assertFalse(foundUser.isPresent());
//        }
//    }
//
//    @Nested
//    class FriendshipRepositoryTests {
//
//        @BeforeEach
//        void setUp() {
//            databaseCleaner.clean();
//        }
//
//        @Test
//        void testFindByUser1AndUser2() {
//            ChatUser user1 = new ChatUser();
//            user1.setEmail("test1@example.com");
//            user1.setName("Test User 1");
//            user1.setPassword("password");
//            user1.setJoinedAt(LocalDateTime.now());
//
//            ChatUser user2 = new ChatUser();
//            user2.setEmail("test2@example.com");
//            user2.setName("Test User 2");
//            user2.setPassword("password");
//            user2.setJoinedAt(LocalDateTime.now());
//
//            userRepository.save(user1);
//            userRepository.save(user2);
//
//            Friendship friendship = new Friendship();
//            friendship.setUser1(user1);
//            friendship.setUser2(user2);
//            friendship.setFriendsSince(LocalDateTime.now());
//
//            friendshipRepository.save(friendship);
//
//            Optional<Friendship> foundFriendship = friendshipRepository.findByUser1AndUser2(user1, user2);
//
//            assertTrue(foundFriendship.isPresent());
//            assertEquals(user1, foundFriendship.get().getUser1());
//            assertEquals(user2, foundFriendship.get().getUser2());
//        }
//
//        @Test
//        void testDeleteFriendship() {
//            ChatUser user1 = new ChatUser();
//            user1.setEmail("test1@example.com");
//            user1.setName("Test User 1");
//            user1.setPassword("password");
//            user1.setJoinedAt(LocalDateTime.now());
//
//            ChatUser user2 = new ChatUser();
//            user2.setEmail("test2@example.com");
//            user2.setName("Test User 2");
//            user2.setPassword("password");
//            user2.setJoinedAt(LocalDateTime.now());
//
//            userRepository.save(user1);
//            userRepository.save(user2);
//
//            Friendship friendship = new Friendship();
//            friendship.setUser1(user1);
//            friendship.setUser2(user2);
//            friendship.setFriendsSince(LocalDateTime.now());
//
//            Friendship savedFriendship = friendshipRepository.save(friendship);
//            friendshipRepository.deleteById(savedFriendship.getId());
//            Optional<Friendship> foundFriendship = friendshipRepository.findById(savedFriendship.getId());
//
//            assertFalse(foundFriendship.isPresent());
//        }
//
//    }
//
//    @Nested
//    class ChatMessageRepositoryTests {
//
//        @BeforeEach
//        void setUp() {
//            databaseCleaner.clean();
//        }
//
//        @Test
//        void testSaveAndFindByIdAndDelete() {
//            ChatMessage message = new ChatMessage();
//            message.setContent("Hello, world!");
//            message.setSentAt(LocalDateTime.now());
//
//            ChatMessage savedMessage = chatMessageRepository.save(message);
//            Optional<ChatMessage> foundMessage = chatMessageRepository.findById(savedMessage.getId());
//
//            assertTrue(foundMessage.isPresent());
//            assertEquals("Hello, world!", foundMessage.get().getContent());
//
//            chatMessageRepository.deleteById(savedMessage.getId());
//            Optional<ChatMessage> deletedMessage = chatMessageRepository.findById(savedMessage.getId());
//
//            assertFalse(deletedMessage.isPresent());
//        }
//    }
//
//    @Nested
//    class ConversationMembersRepositoryTests {
//
//        @BeforeEach
//        void setUp() {
//            databaseCleaner.clean();
//        }
//
//        @Test
//        void testSaveAndFindByIdAndDelete() {
//            Conversation conversation = new Conversation(3L, "Test Conversation", LocalDateTime.now());
//            ChatUser user = new ChatUser(3L, "test@example.com", "Test User", "password", LocalDateTime.now());
//            userRepository.save(user);
//            ConversationMembersId conversationMembersId = new ConversationMembersId(3L, 3L);
//            ConversationMembers conversationMembers = new ConversationMembers(conversationMembersId, LocalDateTime.now(), conversation, user);
//
//            conversationMembersRepository.save(conversationMembers);
//
//            Optional<ConversationMembers> foundConversationMembers = conversationMembersRepository.findById(conversationMembersId);
//
//            assertTrue(foundConversationMembers.isPresent());
//            assertEquals(conversationMembersId, foundConversationMembers.get().getId());
//
//            conversationMembersRepository.deleteById(conversationMembersId);
//
//            Optional<ConversationMembers> deletedConversationMembers = conversationMembersRepository.findById(conversationMembersId);
//
//            assertFalse(deletedConversationMembers.isPresent());
//        }
//    }
//
//    @Nested
//    class ConversationRepositoryTests {
//
//        @BeforeEach
//        void setUp() {
//            databaseCleaner.clean();
//        }
//
//        @Test
//        void testSaveAndFindByIdAndDelete() {
//            Conversation conversation = new Conversation(3L, "Test Conversation", LocalDateTime.now());
//
//            Conversation savedConversation = conversationRepository.save(conversation);
//            Optional<Conversation> foundConversation = conversationRepository.findById(savedConversation.getId());
//
//            assertTrue(foundConversation.isPresent());
//            assertEquals("Test Conversation", foundConversation.get().getName());
//
//            conversationRepository.deleteById(savedConversation.getId());
//            Optional<Conversation> deletedConversation = conversationRepository.findById(savedConversation.getId());
//
//            assertFalse(deletedConversation.isPresent());
//        }
//    }
//
//    @Nested
//    class FriendshipRequestRepositoryTests {
//
//        @BeforeEach
//        void setUp() {
//            databaseCleaner.clean();
//        }
//
//        @Test
//        void testSaveAndFindByIdAndDelete() {
//            ChatUser user1 = new ChatUser(3L, "test@example.com", "Test User", "password", LocalDateTime.now());
//            ChatUser user2 = new ChatUser(4L, "test2@example.com", "Test User2", "password", LocalDateTime.now());
//            userRepository.save(user1);
//            userRepository.save(user2);
//
//            FriendshipRequestId friendshipRequestId = new FriendshipRequestId(3L, 4L);
//
//            FriendshipRequest friendshipRequest = new FriendshipRequest(friendshipRequestId, user1, user2, LocalDateTime.now());
//
//            friendshipRequestRepository.save(friendshipRequest);
//            Optional<FriendshipRequest> foundFriendshipRequest = friendshipRequestRepository.findById(friendshipRequestId);
//
//            assertTrue(foundFriendshipRequest.isPresent());
//            assertEquals(friendshipRequestId, foundFriendshipRequest.get().getId());
//
//            friendshipRequestRepository.deleteById(friendshipRequestId);
//            Optional<FriendshipRequest> deletedFriendshipRequest = friendshipRequestRepository.findById(friendshipRequestId);
//
//            assertFalse(deletedFriendshipRequest.isPresent());
//
//        }
//    }
//
//   // MessageLikeRepositoryTests
//
//}
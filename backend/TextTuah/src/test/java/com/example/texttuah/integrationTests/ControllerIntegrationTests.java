//package com.example.texttuah.integrationTests;
//
//import com.example.texttuah.dto.*;
//import com.example.texttuah.entity.*;
//import com.example.texttuah.service.*;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import static org.hamcrest.Matchers.hasItems;
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@SpringJUnitConfig
//public class ControllerIntegrationTests {
//
//    @Autowired
//    private DatabaseCleaner databaseCleaner;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Nested
//    class UserControllerTests {
//
//        @Autowired
//        private UserService userService;
//
//        @Autowired
//        private JWTService jwtService;
//
//        @BeforeEach
//        void setUp() {
//            databaseCleaner.clean();
//        }
//
//        @Test
//        void testGetUser() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            userService.save(user);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            mockMvc.perform(get("/users")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.email").value("test@example.com"))
//                    .andExpect(jsonPath("$.name").value("test"));
//        }
//
//        @Test
//        void testChangeUserName() throws Exception {
//            String newName = "newName";
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            userService.save(user);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            mockMvc.perform(patch("/users/name/change/" + newName)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
//                    .andExpect(content().string(newName));
//
//            mockMvc.perform(get("/users")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.email").value("test@example.com"))
//                    .andExpect(jsonPath("$.name").value(newName));
//        }
//
//        @Test
//        void testFriendsManagement() throws Exception {
//            long userId;
//            long friendId;
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            ChatUser friend = new ChatUser(2L, "test2@example.com", "friend", "password", null);
//            userService.save(user);
//            userService.save(friend);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//            String jwtTokenFriend = jwtService.generateToken(friend.getEmail());
//
//            userId = userService.getUserId(new UserIdentificationDTO(null, user.getEmail()));
//            friendId = userService.getUserId(new UserIdentificationDTO(null, friend.getEmail()));
//
//            // send friend request
//            mockMvc.perform(post("/users/friends/requests/send")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(new UserIdentificationDTO(friendId, friend.getEmail()))))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print());
//
//            // get received friend requests
//            mockMvc.perform(get("/users/friends/requests/received")
//                            .header("Authorization", "Bearer " + jwtTokenFriend)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(jsonPath("$[0].email").value(user.getEmail()));
//
//            // accept friend request
//            mockMvc.perform(post("/users/friends/requests/accept")
//                            .header("Authorization", "Bearer " + jwtTokenFriend)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(new UserIdentificationDTO(userId, user.getEmail()))))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print());
//
//            // check if in friends list of both users
//            mockMvc.perform(get("/users/friends")
//                            .header("Authorization", "Bearer " + jwtTokenFriend)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(jsonPath("$[0].email").value(user.getEmail()));
//
//            mockMvc.perform(get("/users/friends")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(jsonPath("$[0].email").value(friend.getEmail()));
//
//            // remove friend
//            mockMvc.perform(post("/users/friends/remove")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(new UserIdentificationDTO(friendId, friend.getEmail()))))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print());
//
//            // check if removed from friends list of both users
//            mockMvc.perform(get("/users/friends")
//                            .header("Authorization", "Bearer " + jwtTokenFriend)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(jsonPath("$").isEmpty());
//
//            mockMvc.perform(get("/users/friends")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(jsonPath("$").isEmpty());
//
//            // send new friend request
//            mockMvc.perform(post("/users/friends/requests/send")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(new UserIdentificationDTO(friendId, friend.getEmail()))))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print());
//
//            // reject friend request
//            mockMvc.perform(post("/users/friends/requests/reject")
//                            .header("Authorization", "Bearer " + jwtTokenFriend)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(new UserIdentificationDTO(userId, user.getEmail()))))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print());
//
//            // check if rejected
//            mockMvc.perform(get("/users/friends")
//                            .header("Authorization", "Bearer " + jwtTokenFriend)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(jsonPath("$").isEmpty());
//
//
//        }
//
//        @Test
//        void testLoginAndRegister() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//
//            mockMvc.perform(post("/users/register")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(user)))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.email").value(user.getEmail()));
//
//            mockMvc.perform(post("/users/login")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(user)))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
//        }
//
//
//    }
//
//    @Nested
//    class PostControllerTests {
//
//        @Autowired
//        private UserService userService;
//
//        @Autowired
//        private JWTService jwtService;
//
//        @BeforeEach
//        void setUp() {
//            databaseCleaner.clean();
//        }
//
//        @Test
//        void testAddPost() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            userService.save(user);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//            PostContentDTO postContent = new PostContentDTO("test post");
//
//            mockMvc.perform(post("/posts/add")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(postContent)))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.content").value(postContent.getContent()))
//                    .andExpect(jsonPath("$.poster.email").value(user.getEmail()))
//                    .andDo(MockMvcResultHandlers.print());
//        }
//
//        @Test
//        void testGetPosts() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            userService.save(user);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//            PostContentDTO postContent = new PostContentDTO("test post");
//            UserIdentificationDTO userDto= new UserIdentificationDTO(null, user.getEmail());
//
//            mockMvc.perform(post("/posts/add")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(postContent)))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print());
//
//            mockMvc.perform(post("/posts")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(userDto)))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[0].content").value(postContent.getContent()))
//                    .andExpect(jsonPath("$[0].poster.email").value(user.getEmail()))
//                    .andDo(MockMvcResultHandlers.print());
//        }
//
//        @Test
//        void testDeletePost() throws Exception {
//            long postId;
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            userService.save(user);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//            PostContentDTO postContent = new PostContentDTO("test post");
//            UserIdentificationDTO userDto= new UserIdentificationDTO(null, user.getEmail());
//
//            MvcResult result = mockMvc.perform(post("/posts/add")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(postContent)))
//                    .andReturn();
//
//            postId = Long.parseLong(result.getResponse().getContentAsString().split(":")[1].split(",")[0]);
//
//            mockMvc.perform(delete("/posts/remove/" + postId)
//                            .header("Authorization", "Bearer " + jwtToken))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print());
//
//            mockMvc.perform(post("/posts")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(userDto)))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$").isEmpty())
//                    .andDo(MockMvcResultHandlers.print());
//        }
//
//        @Test
//        void testLikePost() throws Exception {
//            long postId;
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            userService.save(user);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//            PostContentDTO postContent = new PostContentDTO("test post");
//            UserIdentificationDTO userDto = new UserIdentificationDTO(null, user.getEmail());
//
//            MvcResult result = mockMvc.perform(post("/posts/add")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(postContent)))
//                    .andReturn();
//
//            postId = Long.parseLong(result.getResponse().getContentAsString().split(":")[1].split(",")[0]);
//
//            mockMvc.perform(post("/posts/toggle-like/" + postId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(userDto)))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(content().string("true"));
//        }
//
//        @Test
//        void testUnlikePost() throws Exception {
//            long postId;
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            userService.save(user);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//            PostContentDTO postContent = new PostContentDTO("test post");
//            UserIdentificationDTO userDto = new UserIdentificationDTO(null, user.getEmail());
//
//            MvcResult result = mockMvc.perform(post("/posts/add")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(postContent)))
//                    .andReturn();
//
//            postId = Long.parseLong(result.getResponse().getContentAsString().split(":")[1].split(",")[0]);
//
//            mockMvc.perform(post("/posts/toggle-like/" + postId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(userDto)))
//                    .andExpect(status().isOk());
//
//            mockMvc.perform(post("/posts/toggle-like/" + postId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(userDto)))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(content().string("false"));
//        }
//    }
//
//    @Nested
//    class ConversationControllerTests {
//
//        @Autowired
//        private ConversationService conversationService;
//
//        @Autowired
//        private UserService userService;
//
//        @Autowired
//        private JWTService jwtService;
//
//        @BeforeEach
//        void setUp() {
//            databaseCleaner.clean();
//        }
//
//        @Test
//        void testCreateConversation() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            ChatUser friend = new ChatUser(2L, "friend@example.com", "friend", "password", null);
//            userService.save(user);
//            userService.save(friend);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            mockMvc.perform(post("/conversations/create/testConversation")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(Set.of(new UserIdentificationDTO(null, friend.getEmail())))))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.name").value("testConversation"))
//                    .andExpect(jsonPath("$.members").isArray())
//                    .andExpect(jsonPath("$.members[*].name").value(hasItems(user.getName(), friend.getName())))
//                    .andDo(MockMvcResultHandlers.print());
//        }
//
//        @Test
//        void testGetConversations() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            ChatUser friend = new ChatUser(2L, "friend@example.com", "friend", "password", null);
//            userService.save(user);
//            userService.save(friend);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            HashSet<UserIdentificationDTO> members = new HashSet<>();
//            members.add(new UserIdentificationDTO(null, friend.getEmail()));
//            members.add(new UserIdentificationDTO(null, user.getEmail()));
//            conversationService.createNewConversation(members, "testConversation");
//
//            mockMvc.perform(get("/conversations")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[0].name").value("testConversation"))
//                    .andExpect(jsonPath("$[0].members").isArray())
//                    .andExpect(jsonPath("$[0].members[*].name").value(hasItems(user.getName(), friend.getName())))
//                    .andDo(MockMvcResultHandlers.print());
//        }
//
//        @Test
//        void testLeaveConversationTwoMembers() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            ChatUser friend = new ChatUser(2L, "friend@example.com", "friend", "password", null);
//            userService.save(user);
//            userService.save(friend);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            HashSet<UserIdentificationDTO> members = new HashSet<>();
//            members.add(new UserIdentificationDTO(null, friend.getEmail()));
//            members.add(new UserIdentificationDTO(null, user.getEmail()));
//            ConversationDTO conversation = conversationService.createNewConversation(members, "testConversation");
//            long conversationId = conversation.getId();
//
//            mockMvc.perform(delete("/conversations/leave/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(content().string("true"));
//
//            mockMvc.perform(get("/conversations")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$").isEmpty())
//                    .andDo(MockMvcResultHandlers.print());
//
//            jwtToken = jwtService.generateToken(friend.getEmail());
//            mockMvc.perform(get("/conversations")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$").isEmpty())
//                    .andDo(MockMvcResultHandlers.print());
//        }
//
//        @Test
//        void testLeaveConversationThreeMembers() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            ChatUser friend = new ChatUser(2L, "friend@example.com", "friend", "password", null);
//            ChatUser secondFriend = new ChatUser(3L, "friend2@example.com", "friend2", "password", null);
//            userService.save(user);
//            userService.save(friend);
//            userService.save(secondFriend);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            HashSet<UserIdentificationDTO> members = new HashSet<>();
//            members.add(new UserIdentificationDTO(null, friend.getEmail()));
//            members.add(new UserIdentificationDTO(null, user.getEmail()));
//            members.add(new UserIdentificationDTO(null, secondFriend.getEmail()));
//
//            ConversationDTO conversation = conversationService.createNewConversation(members, "testConversation");
//            long conversationId = conversation.getId();
//
//            mockMvc.perform(delete("/conversations/leave/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(content().string("true"));
//
//            mockMvc.perform(get("/conversations")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$").isEmpty())
//                    .andDo(MockMvcResultHandlers.print());
//
//            jwtToken = jwtService.generateToken(friend.getEmail());
//            mockMvc.perform(get("/conversations")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[0].name").value("testConversation"))
//                    .andExpect(jsonPath("$[0].members").isArray())
//                    .andExpect(jsonPath("$[0].members[*].name").value(hasItems(friend.getName(), secondFriend.getName())))
//                    .andDo(MockMvcResultHandlers.print());
//        }
//
//        @Test
//        void testLeaveConversationFourMembers() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            ChatUser friend = new ChatUser(2L, "friend@example.com", "friend", "password", null);
//            ChatUser secondFriend = new ChatUser(3L, "friend2@example.com", "friend2", "password", null);
//            ChatUser thirdFriend = new ChatUser(4L, "friend3@example.com", "friend3", "password", null);
//            userService.save(user);
//            userService.save(friend);
//            userService.save(secondFriend);
//            userService.save(thirdFriend);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            HashSet<UserIdentificationDTO> members = new HashSet<>();
//            members.add(new UserIdentificationDTO(null, friend.getEmail()));
//            members.add(new UserIdentificationDTO(null, user.getEmail()));
//            members.add(new UserIdentificationDTO(null, secondFriend.getEmail()));
//            members.add(new UserIdentificationDTO(null, thirdFriend.getEmail()));
//
//            ConversationDTO conversation = conversationService.createNewConversation(members, "testConversation");
//            long conversationId = conversation.getId();
//
//            mockMvc.perform(delete("/conversations/leave/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(content().string("true"));
//
//            jwtToken = jwtService.generateToken(friend.getEmail());
//            mockMvc.perform(get("/conversations")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[0].name").value("testConversation"))
//                    .andExpect(jsonPath("$[0].members").isArray())
//                    .andExpect(jsonPath("$[0].members[*].name").value(hasItems(friend.getName(), secondFriend.getName(), thirdFriend.getName())));
//        }
//
//        @Test
//        void testAddConversationMember() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            ChatUser friend = new ChatUser(2L, "friend@example.com", "friend", "password", null);
//            ChatUser secondFriend = new ChatUser(3L, "friend2@example.com", "friend2", "password", null);
//            userService.save(user);
//            userService.save(friend);
//            userService.save(secondFriend);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            HashSet<UserIdentificationDTO> members = new HashSet<>();
//            members.add(new UserIdentificationDTO(null, friend.getEmail()));
//            members.add(new UserIdentificationDTO(null, user.getEmail()));
//
//            ConversationDTO conversation = conversationService.createNewConversation(members, "testConversation");
//            long conversationId = conversation.getId();
//
//            mockMvc.perform(post("/conversations/add/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(Set.of(new UserIdentificationDTO(null, secondFriend.getEmail()))))
//                    )
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print());
//
//            mockMvc.perform(get("/conversations")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[0].name").value("testConversation"))
//                    .andExpect(jsonPath("$[0].members").isArray())
//                    .andExpect(jsonPath("$[0].members[*].name").value(hasItems(user.getName(), friend.getName(), secondFriend.getName())))
//                    .andDo(MockMvcResultHandlers.print());
//        }
//    }
//
//    @Nested
//    class MessageControllerTests {
//
//        @Autowired
//        private UserService userService;
//
//        @Autowired
//        private JWTService jwtService;
//
//        @Autowired
//        private ConversationService conversationService;
//
//        @BeforeEach
//        void setUp() {
//            databaseCleaner.clean();
//        }
//
//        @Test
//        void testAddMessage() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            ChatUser friend = new ChatUser(2L, "friend@example.com", "friend", "password", null);
//            userService.save(user);
//            userService.save(friend);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            HashSet<UserIdentificationDTO> members = new HashSet<>();
//            members.add(new UserIdentificationDTO(null, friend.getEmail()));
//            members.add(new UserIdentificationDTO(null, user.getEmail()));
//
//            ConversationDTO conversation = conversationService.createNewConversation(members, "testConversation");
//            long conversationId = conversation.getId();
//
//            MessageContentDTO messageContent = new MessageContentDTO("test message");
//
//            mockMvc.perform(post("/messages/add/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(messageContent)))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.content").value("test message"))
//                    .andExpect(jsonPath("$.senderName").value(user.getName()))
//                    .andDo(MockMvcResultHandlers.print());
//        }
//
//        @Test
//        void testGetMessages() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            ChatUser friend = new ChatUser(2L, "friend@example.com", "friend", "password", null);
//            userService.save(user);
//            userService.save(friend);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            HashSet<UserIdentificationDTO> members = new HashSet<>();
//            members.add(new UserIdentificationDTO(null, friend.getEmail()));
//            members.add(new UserIdentificationDTO(null, user.getEmail()));
//
//            ConversationDTO conversation = conversationService.createNewConversation(members, "testConversation");
//            long conversationId = conversation.getId();
//
//            MessageContentDTO messageContent = new MessageContentDTO("test message");
//            mockMvc.perform(post("/messages/add/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(messageContent)))
//                    .andExpect(status().isOk());
//
//            jwtToken = jwtService.generateToken(friend.getEmail());
//            messageContent = new MessageContentDTO("test message 2");
//            mockMvc.perform(post("/messages/add/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(messageContent)))
//                    .andExpect(status().isOk());
//
//            mockMvc.perform(get("/messages/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[*].content").value(hasItems("test message", "test message 2")))
//                    .andExpect(jsonPath("$[*].senderName").value(hasItems("test", "friend")))
//                    .andDo(MockMvcResultHandlers.print());
//        }
//
//        @Test
//        void testToggleLikeMessages() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            ChatUser friend = new ChatUser(2L, "friend@example.com", "friend", "password", null);
//            userService.save(user);
//            userService.save(friend);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            HashSet<UserIdentificationDTO> members = new HashSet<>();
//            members.add(new UserIdentificationDTO(null, friend.getEmail()));
//            members.add(new UserIdentificationDTO(null, user.getEmail()));
//
//            ConversationDTO conversation = conversationService.createNewConversation(members, "testConversation");
//            long conversationId = conversation.getId();
//            // add messages
//            MessageContentDTO messageContent = new MessageContentDTO("test message");
//            MvcResult result = mockMvc.perform(post("/messages/add/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(messageContent)))
//                    .andExpect(status().isOk())
//                    .andReturn();
//
//            long messageId1 = Long.parseLong(result.getResponse().getContentAsString().split(":")[1].split(",")[0]);
//
//            jwtToken = jwtService.generateToken(friend.getEmail());
//            messageContent = new MessageContentDTO("test message 2");
//            result = mockMvc.perform(post("/messages/add/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(messageContent)))
//                    .andExpect(status().isOk())
//                    .andReturn();
//
//            long messageId2 = Long.parseLong(result.getResponse().getContentAsString().split(":")[1].split(",")[0]);
//            // toggle like for both messages by friend
//            mockMvc.perform(post("/messages/toggle-like/" + messageId1)
//                            .header("Authorization", "Bearer " + jwtToken))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(content().string("true"));
//
//            mockMvc.perform(post("/messages/toggle-like/" + messageId2)
//                            .header("Authorization", "Bearer " + jwtToken))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(content().string("true"));
//
//            // check if liked by friend
//            mockMvc.perform(get("/messages/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[*].content").value(hasItems("test message", "test message 2")))
//                    .andExpect(jsonPath("$[*].senderName").value(hasItems("test", "friend")))
//                    .andExpect(jsonPath("$[1].liked").value(true))
//                    .andExpect(jsonPath("$[0].liked").value(true))
//                    .andExpect(jsonPath("$[1].numberOfLikes").value(1))
//                    .andExpect(jsonPath("$[0].numberOfLikes").value(1))
//                    .andDo(MockMvcResultHandlers.print());
//
//            // check if liked by user, should be false
//            jwtToken = jwtService.generateToken(user.getEmail());
//            mockMvc.perform(get("/messages/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[*].content").value(hasItems("test message", "test message 2")))
//                    .andExpect(jsonPath("$[*].senderName").value(hasItems("test", "friend")))
//                    .andExpect(jsonPath("$[1].liked").value(false))
//                    .andExpect(jsonPath("$[0].liked").value(false))
//                    .andExpect(jsonPath("$[1].numberOfLikes").value(1))
//                    .andExpect(jsonPath("$[0].numberOfLikes").value(1))
//                    .andDo(MockMvcResultHandlers.print());
//            // toggle like for both messages by user
//            mockMvc.perform(post("/messages/toggle-like/" + messageId1)
//                            .header("Authorization", "Bearer " + jwtToken))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(content().string("true"));
//
//            mockMvc.perform(post("/messages/toggle-like/" + messageId2)
//                            .header("Authorization", "Bearer " + jwtToken))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(content().string("true"));
//
//            // check if liked by user
//            mockMvc.perform(get("/messages/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[*].content").value(hasItems("test message", "test message 2")))
//                    .andExpect(jsonPath("$[*].senderName").value(hasItems("test", "friend")))
//                    .andExpect(jsonPath("$[1].liked").value(true))
//                    .andExpect(jsonPath("$[0].liked").value(true))
//                    .andExpect(jsonPath("$[1].numberOfLikes").value(2))
//                    .andExpect(jsonPath("$[0].numberOfLikes").value(2))
//                    .andDo(MockMvcResultHandlers.print());
//
//            // unlike for both messages by user
//            mockMvc.perform(post("/messages/toggle-like/" + messageId1)
//                            .header("Authorization", "Bearer " + jwtToken))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(content().string("false"));
//
//            mockMvc.perform(post("/messages/toggle-like/" + messageId2)
//                            .header("Authorization", "Bearer " + jwtToken))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(content().string("false"));
//
//
//            mockMvc.perform(get("/messages/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[*].content").value(hasItems("test message", "test message 2")))
//                    .andExpect(jsonPath("$[*].senderName").value(hasItems("test", "friend")))
//                    .andExpect(jsonPath("$[1].liked").value(false))
//                    .andExpect(jsonPath("$[0].liked").value(false))
//                    .andExpect(jsonPath("$[1].numberOfLikes").value(1))
//                    .andExpect(jsonPath("$[0].numberOfLikes").value(1));
//
//            // check if still liked by friend
//            jwtToken = jwtService.generateToken(friend.getEmail());
//            mockMvc.perform(get("/messages/" + conversationId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[*].content").value(hasItems("test message", "test message 2")))
//                    .andExpect(jsonPath("$[*].senderName").value(hasItems("test", "friend")))
//                    .andExpect(jsonPath("$[1].liked").value(true))
//                    .andExpect(jsonPath("$[0].liked").value(true))
//                    .andExpect(jsonPath("$[1].numberOfLikes").value(1))
//                    .andExpect(jsonPath("$[0].numberOfLikes").value(1));
//        }
//    }
//
//    @Nested
//    class CommentControllerTests {
//
//        @Autowired
//        private UserService userService;
//
//        @Autowired
//        private JWTService jwtService;
//
//        @BeforeEach
//        void setUp() {
//            databaseCleaner.clean();
//        }
//
//        @Test
//        void testSendComment() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            userService.save(user);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            PostContentDTO postContent = new PostContentDTO("test post");
//
//            MvcResult result = mockMvc.perform(post("/posts/add")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(postContent)))
//                    .andReturn();
//
//            long postId = Long.parseLong(result.getResponse().getContentAsString().split(":")[1].split(",")[0]);
//
//            CommentContentDTO commentContent = new CommentContentDTO("test comment");
//
//            mockMvc.perform(post("/comments/send/" + postId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(commentContent)))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.content").value("test comment"))
//                    .andExpect(jsonPath("$.commenter.email").value(user.getEmail()))
//                    .andDo(MockMvcResultHandlers.print());
//        }
//
//        @Test
//        void testGetComments() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            ChatUser friend = new ChatUser(2L, "friend@example.com", "friend", "password", null);
//            userService.save(user);
//            userService.save(friend);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            PostContentDTO postContent = new PostContentDTO("test post");
//
//            MvcResult result = mockMvc.perform(post("/posts/add")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(postContent)))
//                    .andReturn();
//
//            long postId = Long.parseLong(result.getResponse().getContentAsString().split(":")[1].split(",")[0]);
//
//            CommentContentDTO commentContent = new CommentContentDTO("test comment");
//
//            mockMvc.perform(post("/comments/send/" + postId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(commentContent)))
//                    .andExpect(status().isOk());
//
//            jwtToken = jwtService.generateToken(friend.getEmail());
//            commentContent = new CommentContentDTO("test comment 2");
//            mockMvc.perform(post("/comments/send/" + postId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(commentContent)))
//                    .andExpect(status().isOk());
//
//            mockMvc.perform(get("/comments/" + postId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[*].content").value(hasItems("test comment", "test comment 2")))
//                    .andExpect(jsonPath("$[*].commenter.email").value(hasItems(user.getEmail(), friend.getEmail())))
//                    .andDo(MockMvcResultHandlers.print());
//        }
//
//        @Test
//        void testRemoveComment() throws Exception {
//            ChatUser user = new ChatUser(1L, "test@example.com", "test", "password", null);
//            userService.save(user);
//            String jwtToken = jwtService.generateToken(user.getEmail());
//
//            PostContentDTO postContent = new PostContentDTO("test post");
//
//            MvcResult result = mockMvc.perform(post("/posts/add")
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(postContent)))
//                    .andReturn();
//
//            long postId = Long.parseLong(result.getResponse().getContentAsString().split(":")[1].split(",")[0]);
//
//            CommentContentDTO commentContent = new CommentContentDTO("test comment");
//
//            mockMvc.perform(post("/comments/send/" + postId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(new ObjectMapper().writeValueAsString(commentContent)))
//                    .andExpect(status().isOk());
//
//            result = mockMvc.perform(get("/comments/" + postId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[0].content").value("test comment"))
//                    .andReturn();
//
//            long commentId = Long.parseLong(result.getResponse().getContentAsString().split(":")[1].split(",")[0]);
//
//            mockMvc.perform(delete("/comments/remove/" + commentId)
//                            .header("Authorization", "Bearer " + jwtToken))
//                    .andExpect(status().isOk())
//                    .andDo(MockMvcResultHandlers.print());
//
//            mockMvc.perform(get("/comments/" + postId)
//                            .header("Authorization", "Bearer " + jwtToken)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$").isEmpty())
//                    .andDo(MockMvcResultHandlers.print());
//        }
//    }
//}
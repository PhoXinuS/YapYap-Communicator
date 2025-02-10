import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext'; // Import the useAuth hook
import { useNavigate } from 'react-router-dom'; // Import useNavigate hook
import '../ui/Conversations.css'; // Import the Conversations CSS file
import { Message } from '../types/Message'; // Import the Message type
import { Friend } from '../types/Friend'; // Import the Friend type
import api from '../api/ApiHandler'; // Import the API handler
import { useEffect } from 'react';
import MainPanel from '../components/MainPanel';
import { useRef } from 'react';
import SockJS from 'sockjs-client';
import { Client, IMessage } from '@stomp/stompjs';

let stompClient: Client | null = null;
let likeStompClient: Client | null = null;


const Conversations: React.FC = () => {
    const { user } = useAuth(); // Get user data from context
    const navigate = useNavigate(); // Hook for navigation
    const [conversations, setConversations] = useState<any[]>([]); // State to store conversations
    const [friends, setFriends] = useState<Friend[]>([]);
    
    const [selectedConversation, setSelectedConversation] = useState<number | null>(null);
    const [showCreateGroupForm, setShowCreateGroupForm] = useState<boolean>(false);
    const [showAddFriendForm, setShowAddFriendForm] = useState<boolean>(false);
    const [selectedFriends, setSelectedFriends] = useState<Friend[]>([]);
    const [filteredFriends, setFilteredFriends] = useState<Friend[]>(friends); // State to store filtered friends
    const [newMessage, setNewMessage] = useState<string>(''); // State for new message input
    const [messages, setMessages] = useState<Message[]>([]);
    const [groupName, setGroupName] = useState<string>(''); // State for group name

    const messagesEndRef = useRef<HTMLDivElement>(null); // Reference to the bottom of the messages container

    useEffect(() => {
        if (messagesEndRef.current) {
            messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    }, [messages]); // This will run when the messages array changes

    const connectWebSocket = async (conversationId: number) => {
        try {
            const token = await localStorage.getItem('authToken');
    
            if (!token) {
                console.error('No token available');
                return;
            }
    
            // Create the SockJS connection
            const url = process.env.REACT_APP_BACKEND_URL;
            const socket = new SockJS(url + '/ws');
    
            // Initialize the STOMP client
            stompClient = new Client({
                webSocketFactory: () => socket as WebSocket,
                connectHeaders: {
                    Authorization: `Bearer ${token}`, // Pass the token in headers
                },
                debug: (str) => console.log(str), // Debug logs
                onConnect: (frame) => {
                    console.log('Connected:', frame);
    
                    // Replace placeholder with actual conversation ID
                    const conversationPath = `/user/topic/conversation/${conversationId}`;
                    const likesPath = `/user/topic/likes/${conversationId}`;
    
                    // Subscribe to the conversation messages
                    stompClient?.subscribe(conversationPath, (message: IMessage) => {
                        const data = JSON.parse(message.body);
                        console.log('Message received:', data);
    
                        setMessages((prevMessages) => {
                            const updatedMessages = [...prevMessages, data];
                            updatedMessages.sort((a, b) => new Date(a.sentAt).getTime() - new Date(b.sentAt).getTime());
                            return updatedMessages;
                        });
                    });
    
                    // Subscribe to the like notifications
                    stompClient?.subscribe(likesPath, (message: IMessage) => {
                        const data = JSON.parse(message.body);
                        console.log('Like received:', data);
    
                        setMessages((prevMessages) => {
                            return prevMessages.map((msg) =>
                                msg.id === data.messageId
                                    ? {
                                        ...msg,
                                        numberOfLikes: data.numberOfLikes,
                                    }
                                    : msg
                            );
                        });
                    });
                },
                onStompError: (frame) => {
                    console.error('STOMP Error:', frame.headers['message'], frame.body);
                },
                onWebSocketError: (error) => {
                    console.error('WebSocket Error:', error);
                },
            });
    
            stompClient.activate(); // Start the WebSocket connection
        } catch (error) {
            console.error('Error connecting WebSocket:', error);
        }
    };
    

    useEffect(() => {
        if (selectedConversation !== null) {
            connectWebSocket(selectedConversation);
        }
    }, [selectedConversation]);

    const fetchConversations = async () => {
        try {
            const response = await api.Get('/conversations');
            const data = await response.json();
            console.log("conversations fetched : ", data);
            setConversations(data); // Update the conversations state
        } catch (error) {
            console.error('Error fetching conversations:', error);
        }
    };

    const fetchFriends = async () => {
        try {
            const response = await api.Get('/users/friends');
            const data: Friend[] = await response.json();
            console.log("friends fetched : ", data);
            setFriends(data);
        } catch (error) {
            console.error('Error fetching friends:', error);
        }
    };

    const handleNavigate = () => {
        navigate('/main-panel'); // Navigate to the MainPanel page
    };

    const handleSelectConversation = async (id: number) => {
        setSelectedConversation(id);
        
        try {
            const response = await api.Get(`/messages/${id}`);
            const data = await response.json();
            console.log("messages fetched : ", data);
            data.sort((a: Message, b: Message) => new Date(a.sentAt).getTime() - new Date(b.sentAt).getTime());
            setMessages(data);
        }
        catch (error) {
            console.error('Error fetching messages:', error);
        }
    };

    const handleAddFriendToConversation = async () => {
        if (selectedConversation === null) return;
        try {
            console.log(selectedFriends);
            const body = selectedFriends.map((friend) => ({ "email": friend.email }));
            console.log ("friends to add : ", body);
            const response = await api.Post(`/conversations/add/${selectedConversation}`, body);
            if (response.ok) {
                fetchConversations();
            }
        } catch (error) {
            console.error('Error adding friends to conversation:', error);
        }
        setSelectedFriends([]); // Clear the selected friends after adding
    };

    const handleCreateGroupClick = () => {
        fetchFriends(); // Fetch friends list
        setShowCreateGroupForm(true); // Show the form to select friends
    };

    const handleAddFriendClick = () => {
        fetchFriends(); // Fetch friends list
        setShowAddFriendForm(true); // Show the form to add friends to the conversation
    };

        
    const handleSelectFriend = (friend: Friend) => {
        setSelectedFriends((selectedFriends) => {
            // Check if the friend is already selected
            const isSelected = selectedFriends.some((f) => f.name === friend.name);
    
            // If selected, remove the friend from the list
            if (isSelected) {
                return selectedFriends.filter((f) => f.name !== friend.name);
            }
    
            // Otherwise, add the friend to the list
            console.log([...selectedFriends, friend]);
            return [...selectedFriends, friend];
        });
    };
    
    

    const handleCreateGroup = async () => {
        selectedFriends.forEach((friend) => console.log(friend.id));
        try{
            const body = selectedFriends.map((friend) => ({ "email": friend.email }));
            console.log("friends to add : ", body);
            const response = await api.Post(`/conversations/create/${groupName}`, body);
            if (response.ok)
            {
                const data = await response.json();
                console.log("group created : ", data);
                setConversations([...conversations, data]);
            }
        }
        catch(error){
            console.error('Error creating group:', error);
        }
        setShowCreateGroupForm(false); // Close the form after group creation
    };

    const handleCancelGroupCreation = () => {
        setShowCreateGroupForm(false); // Close the form without creating the
    };

    const handleSearchFriend = (searchTerm: string) => {
        const filtered = friends.filter((friend) =>
            friend.name.toLowerCase().includes(searchTerm.toLowerCase())
        );
        setFilteredFriends(filtered); // Update the filtered friends list
    };

    const handleSendMessage = async (e: React.FormEvent) => {
        e.preventDefault();
        if (newMessage.trim() === '') return;


        
        const response = await api.Post(`/messages/add/${selectedConversation}`, newMessage);
        const newMessageData = await response.json();
        console.log("new message : ", newMessageData);
        setNewMessage(''); // Clear the input
    };

    const handleLikeMessage = async (messageId: number) => {
        try {
            const response = await api.Post(`/messages/toggle-like/${messageId}`, { mode: 'no-cors' });
            console.log("message liked : ", response);
            const text = await response.text();
            if (text === "true") {
                setMessages((prevMessages) =>
                    prevMessages.map((msg) =>
                        msg.id === messageId ? { ...msg, liked: true } : msg
                    )
                );
            } else {
                setMessages((prevMessages) =>
                    prevMessages.map((msg) =>
                        msg.id === messageId ? { ...msg, liked: false } : msg
                    )
                );
            }
        }
        catch (error) {
            console.error('Error liking message:', error);
        }
    };

    const handleLeaveGroupClick = async () => {
        if (selectedConversation !== null) {
            const response = await api.Delete(`/conversations/leave/${selectedConversation}`);
            if (!response.ok) {
                console.error('Error leaving group:', response.statusText);
                return;
            }
            console.log("group left : ", response);
            const updatedConversations = conversations.filter((conv) => conv.id !== selectedConversation);
            setConversations(updatedConversations);
            setSelectedConversation(null);
        }
    };

    const handleCancelFriendAdding = () => {
        setShowAddFriendForm(false); // Close the form without adding friends
    };

    useEffect(() => {
        fetchConversations();
        fetchFriends();
        setFilteredFriends(friends);
    }, []);

    useEffect(() => {
        setFilteredFriends(friends); // Always sync filteredFriends with friends
    }, [friends]);

    
    return (
        <div className="conversations-container">
            <MainPanel />
            <div className="conversations-content">
                <div className="conversations-sidebar">
                    <h2 className="conversations-sidebar-title">Conversations</h2>

                    <button
                        onClick={handleCreateGroupClick}
                        className="create-group-button"
                        style={{ marginTop: '20px' }} // You can adjust the style here as needed
                    >
                        Create Group
                    </button>

                    <ul className="conversations-list">
                        {conversations.map((conversation) => (
                            <li
                                key={conversation.id}
                                className={`conversation-item ${
                                    selectedConversation === conversation.id ? 'active' : ''
                                }`}
                                onClick={() => handleSelectConversation(conversation.id)}
                            >
                                {conversation.name}
                            </li>
                        ))}
                    </ul>
                </div>

                <div className="messages-container">
                    {selectedConversation === null ? (
                        <p>Select a conversation to view messages</p>
                    ) : (
                        <>
                            <div className="message-history">
                                {messages.map((message) => (
                                    <div key={message.id} className="message-item">
                                        <strong>{message.senderName}:</strong> {message.content}
                                        <div className="message-meta">
                                            <span className="message-date">{new Date(message.sentAt).toLocaleString()}</span>
                                        </div>
                                        <div className="message-actions">
                                            <span
                                                className="like-counter"
                                                onClick={() => handleLikeMessage(message.id)}
                                            >
                                                {message.liked ? '❤️ ' : '🤍 '} {message.numberOfLikes} {message.numberOfLikes === 1 ? 'Like' : 'Likes'}
                                            </span>
                                        </div>
                                    </div>
                                ))}
                                <div ref={messagesEndRef} /> {/* This is the scroll reference */}
                            </div>
                            <form className="message-form" onSubmit={handleSendMessage} style={{ width: '100%' }}>
                                <input
                                    type="text"
                                    placeholder="Type a message"
                                    value={newMessage}
                                    onChange={(e) => setNewMessage(e.target.value)}
                                    style={{ width: '100%' }}
                                />
                                <button type="submit">Send</button>
                            </form>
                        </>
                    )}
                </div>

                <div className="group-members-container">
                    <h2 className="messages-title">Group Members</h2>
                    {selectedConversation !== null && (
                        <div className="group-members-list">
                            <button
                                onClick={handleLeaveGroupClick}
                                className="leave-group-button"
                            >
                                Leave Group
                            </button>
                            <button
                                onClick={handleAddFriendClick}
                                className="add-friend-to-conversation-button"
                            >
                                Add Friend
                            </button>
                            <div className="group-members-list-container">
                                {conversations
                                    .find((conv) => conv.id === selectedConversation)
                                    ?.members.map((member: Friend) => (
                                        <div key={member.id} className="group-member-item">
                                            {member.name}
                                        </div>
                                    ))}
                            </div>
                        </div>
                    )}
                </div>
            </div>

            {showCreateGroupForm && (
                <div className="add-friend-form-overlay">
                    <div className="add-friend-form">
                        <h3>Create Group</h3>
                        <input
                            type="text"
                            placeholder="Group name"
                            className="search-friend-input"
                            onChange={(e) => setGroupName(e.target.value)}
                        />
                        <h3>Select Friends to Add</h3>
                        <input
                            type="text"
                            placeholder="Search friends"
                            className="search-friend-input"
                            onChange={(e) => handleSearchFriend(e.target.value)}
                        />
                        <ul className="friend-list">
                            {filteredFriends.map((friend) => (
                                <li
                                    key={friend.id}
                                    className={`friend-item ${
                                        selectedFriends.some((f) => f.email === friend.email) ? 'selected' : ''
                                    }`}

                                    onClick={() => handleSelectFriend(friend)}
                                >
                                    {friend.name}
                                </li>
                            ))}
                        </ul>
                        <div className="form-buttons">
                            <button onClick={handleCreateGroup} className="create-group-button">
                                Confirm
                            </button>
                            <button onClick={handleCancelGroupCreation} className="cancel-create-group-button">
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {showAddFriendForm && (
                <div className="add-friend-form-overlay">
                    <div className="add-friend-form">
                        <h3>Select Friends to Add</h3>
                        <input
                            type="text"
                            placeholder="Search friends"
                            className="search-friend-input"
                            onChange={(e) => handleSearchFriend(e.target.value)}
                        />
                        <ul className="friend-list">
                            {filteredFriends.map((friend) => (
                                <li
                                    key={friend.id}
                                    className={`friend-item ${
                                        selectedFriends.some((f) => f.email === friend.email) ? 'selected' : ''
                                    }`}
                                    onClick={() => handleSelectFriend(friend)}
                                >
                                    {friend.name}
                                </li>
                            ))}
                        </ul>
                        <div className="form-buttons">
                            <button onClick={handleAddFriendToConversation} className="create-group-button">
                                Confirm
                            </button>
                            <button onClick={handleCancelFriendAdding} className="cancel-create-group-button">
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Conversations;

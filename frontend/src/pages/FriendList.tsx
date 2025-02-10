import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import '../ui/FriendList.css';
import { FriendRequest } from '../types/FriendRequest';
import { Friend } from '../types/Friend';
import api from '../api/ApiHandler';
import { wait } from '@testing-library/user-event/dist/utils';
import MainPanel from '../components/MainPanel';


const FriendList: React.FC = () => {
    const { user } = useAuth(); // Get the current user from context
    const [friends, setFriends] = useState<Friend[]>([]); // Friends data from the server
    const [friendRequests, setFriendRequests] = useState<FriendRequest[]>([]); // Friend requests
    const [newFriendEmail, setNewFriendEmail] = useState<string>(''); // New friend email
    const [newFriendId, setNewFriendId] = useState<string>(''); // New friend ID
    const [view, setView] = useState<'friends' | 'requests'>('friends'); // Toggle between views
    const [loading, setLoading] = useState<boolean>(true); // Loading state
    const [error, setError] = useState<string | null>(null); // Error message
    const navigate = useNavigate();

    // Function to fetch friends
    const fetchFriends = async () => {
        try {
            const response = await api.Get('/users/friends');
            const data: Friend[] = await response.json();
            console.log("Friends data: ", data);
            setFriends(data);
            setError(null); // Clear any previous errors
        } catch (err: any) {
            setError(err.message || 'An error occurred while fetching friends.');
        } finally {
            setLoading(false);
        }
    };

    // Function to fetch friend requests
    const fetchFriendRequests = async () => {
        const token = localStorage.getItem('authToken'); // Get token from local storage
        try {
            const response = await api.Get('/users/friends/requests/received');
            const data: FriendRequest[] = await response.json();
            console.log("Friend requests data: ", data);
            setFriendRequests(data);
            setError(null); // Clear any previous errors
        } catch (err: any) {
            setError(err.message || 'An error occurred while fetching friend requests.');
        }
    };

    // Accept a friend request
    const acceptRequest = async (email: string) => {
        try {
            const token = localStorage.getItem('authToken');
            const response = await api.Post('/users/friends/requests/accept', email);
            console.log("Friend request accepted: ", response);
            setFriendRequests((prevRequests) => prevRequests.filter((req) => req.email !== email));
        }
        catch (err: any) {
            setError(err.message || 'An error occurred while accepting friend request.');
        }
    };

    // Reject a friend request
    const rejectRequest = async (email: string) => {
        try{
            const response = await api.Post('/users/friends/requests/reject', email);
            console.log("Friend request rejected: ", response);
            setFriendRequests((prevRequests) => prevRequests.filter((req) => req.email !== email));
        }
        catch (err: any) {
            setError(err.message || 'An error occurred while rejecting friend request.');
        }
    };

    // Remove a friend
    const removeFriend = (email: String) => {
        try{
            const token = localStorage.getItem('authToken');
            const response = api.Post('/users/friends/remove', email);
            console.log("Friend removed: ", response);
            setFriends((prevFriends) => prevFriends.filter((friend) => friend.email !== email));
        }
        catch (err: any) {
            setError(err.message || 'An error occurred while removing friend.');
        }
    };


    const addFriend = async () => {
        if (newFriendEmail.trim() || newFriendId.trim()) {
            try {
                // Determine the request payload based on the input provided
                const payload = newFriendEmail.trim() ? { email: newFriendEmail.trim() } : { id: Number(newFriendId.trim()) }; // Convert to number if required

    
                console.log("Sending friend request payload:", payload);
    
                const response = await api.Post('/users/friends/requests/send', payload);
    
                console.log('Friend request sent successfully:');
                setNewFriendEmail('');
                setNewFriendId('');
                setError(null); // Clear any previous errors
            } catch (err: any) {
                console.error('Error sending friend request:', err);
                setError(err.message || 'An error occurred while sending the friend request.');
            }
        } else {
            alert('Please enter either an email or user ID');
        }
    };
    


    useEffect(() => {
        if (view === 'friends') {
            fetchFriends();
        } else if (view === 'requests') {
            fetchFriendRequests();
        }
    }, [view]);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div className="error-message">{error}</div>;
    }

    return (
        <div className="friend-list-container">
            <MainPanel />



            
            {/* Add Friend Form */}
            <div className="add-friend-form-container">
                <div className="add-friend-form">
                    <h2>Add a New Friend</h2>
                    <input
                        type="email"
                        placeholder="Enter friend email"
                        value={newFriendEmail}
                        onChange={(e) => setNewFriendEmail(e.target.value)}
                        className="add-friend-input"
                    />
                    <input
                        type="text"
                        placeholder="Or enter friend user ID"
                        value={newFriendId}
                        onChange={(e) => setNewFriendId(e.target.value)}
                        className="add-friend-input"
                    />
                    <button className="add-friend-btn" onClick={addFriend}>
                        Add Friend
                    </button>
                </div>
            </div>

            {/* Tab buttons for switching views */}
            <div className="tabs">
                <button
                    className={`tab-btn ${view === 'friends' ? 'active' : ''}`}
                    onClick={() => setView('friends')}
                >
                    Friends
                </button>
                <button
                    className={`tab-btn ${view === 'requests' ? 'active' : ''}`}
                    onClick={() => setView('requests')}
                >
                    Friend Requests
                </button>
            </div>
            
            {/* Friend List View */}
            {view === 'friends' && (
                <div className="friend-list-wrapper">
                    <div className="friend-grid">
                        {friends.length === 0 ? (
                            <p className="no-friends-message">You have no friends yet. Start adding some!</p>
                        ) : (
                            friends.map((friend) => (
                                <div key={friend.id} className="friend-card animated-card">
                                    <h3 className="friend-name">{friend.name}</h3>
                                    <p className="friend-email">{friend.email}</p>
                                    <button
                                        className="remove-friend-btn"
                                        onClick={() => removeFriend(friend.email)}
                                    >
                                        Remove Friend
                                    </button>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            )}


            {/* Friend Requests View */}
            {/* Friend Requests View */}
            {view === 'requests' && (
                <div className="friend-list-wrapper">
                    <div className="friend-grid">
                        {friendRequests.length === 0 ? (
                            <p className="no-friends-message">No pending friend requests.</p>
                        ) : (
                            friendRequests.map((request) => (
                                <div key={request.id} className="request-card">
                                    <h3>{request.name}</h3>
                                    <p>{request.email}</p>
                                    <button
                                        className="accept-request-btn"
                                        onClick={() => acceptRequest(request.email)}
                                    >
                                        Accept
                                    </button>
                                    <button
                                        className="reject-request-btn"
                                        onClick={() => rejectRequest(request.email)}
                                    >
                                        Reject
                                    </button>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

export default FriendList;

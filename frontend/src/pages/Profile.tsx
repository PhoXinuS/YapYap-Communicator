import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';  // Import the useAuth hook
import { useNavigate } from 'react-router-dom';  // Import useNavigate hook
import '../ui/Profile.css'; // Import the Profile CSS file
import api from '../api/ApiHandler'; // Import the API handler
import MainPanel from '../components/MainPanel'; // Import the MainPanel component

const Profile: React.FC = () => {
    const { user, logout } = useAuth();  // Get user data and logout from context
    const [newName, setNewName] = useState(user?.name || ''); // State for new name
    const [isEditing, setIsEditing] = useState(false); // To toggle name editing mode
    const navigate = useNavigate(); // Hook for navigation
    const [posts, setPosts] = useState([]); // State for posts
    const [newComment, setNewComment] = useState<{ [key: string]: string }>({}); // State for new comments
    

    const handleNavigate = () => {
        navigate('/main-panel'); // Navigate to the MainPanel page
    };

    const handleNameChange = async () => {
        if (newName && newName !== user?.name) {
            if (user) {
                const originalName = user.name;
                try {
                    console.log('Updating name to:', newName);
                    user.name = newName;
                    setIsEditing(false);
                    const response = await api.Patch(`/users/name/change/${newName}`, '');

                    if (!response.ok) {
                        user.name = originalName;
                        throw new Error(`Failed to update name: ${response.statusText}`);
                    }

                } catch (error: any) {
                    user.name = originalName;
                    alert('Failed to update name: ' + error.message);
                }
            }
        }
    };

    

    if (!user) {
        navigate('/signin');
    }

    return (
        <div className="profile-container">
            <MainPanel />
            <div className="profile-form-container">
                <h2 className="profile-title">Profile</h2>
                <div className="profile-info">
                    <p><strong>Name:</strong> {user?.name}</p>
                    <p><strong>Email:</strong> {user?.email}</p>
                    <p><strong>User ID:</strong> {user?.id}</p>
                </div>

                {/* Edit Name Section */}
                {isEditing ? (
                    <div className="name-edit-container">
                        <input
                            type="text"
                            value={newName}
                            onChange={(e) => setNewName(e.target.value)}
                            placeholder="Enter new name"
                            className="name-input"
                        />
                        <button className="save-name-button" onClick={handleNameChange}>Save Name</button>
                        <button className="cancel-button" onClick={() => setIsEditing(false)}>Cancel</button>
                    </div>
                ) : (
                    <button className="edit-name-button" onClick={() => setIsEditing(true)}>Edit Name</button>
                )}

            </div>
        </div>
    );
};

export default Profile;

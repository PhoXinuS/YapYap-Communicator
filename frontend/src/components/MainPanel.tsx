// src/components/MainPanel.tsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../ui/MainPanel.css';

const MainPanel: React.FC = () => {
    const { logout, user } = useAuth();
    const navigate = useNavigate();

    const handleNavigation = (route: string) => {
        navigate(route);
    };

    const handleNavigate = () => {
        navigate('/main-panel');
    };

    return (
        <header className="main-header">
            <div className="logo-title">
                <img
                    src="https://media1.tenor.com/m/7aEyY3k7WycAAAAC/yap-yapping.gif"
                    alt="React Logo"
                    className="home-logo"
                />
                <h1 className="home-title">YapYap</h1>
            </div>
            <nav className="main-panel-nav">
                <ul>
                    <li onClick={() => handleNavigation('/friend-list')}>Friend List</li>
                    <li onClick={() => handleNavigation('/conversations')}>Conversations</li>
                    <li onClick={() => handleNavigation('/posts')}>Posts</li>
                    <li onClick={() => handleNavigation('/profile')}>Profile</li>
                </ul>
            </nav>
            <div className="header-right">
                {user ? (
                    <div className="user-info">
                        <strong>{user.name}</strong>
                        <br />
                        <span>{user.email}</span>
                    </div>
                ) : (
                    <div className="user-info">Please log in</div>
                )}
                <button className="main-panel-logout" onClick={logout}>
                    Logout
                </button>
            </div>
        </header>
    );
};

export default MainPanel;

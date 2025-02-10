// src/pages/Home.tsx
import React from 'react';
import { Link } from 'react-router-dom';
import '../ui/Home.css';

const Home: React.FC = () => {
    return (
        <div className="home-container">
            {/* Header Section */}
            <header className="home-header">
                <img src="https://media1.tenor.com/m/7aEyY3k7WycAAAAC/yap-yapping.gif" alt="React Logo" className="home-logo" />
                <h1 className="home-title">YapYap</h1>
            </header>

            {/* Main Content Secti  on */}
            <main className="home-main">
                <div className="home-buttons">
                    <Link to="/signin" className="home-button">
                        Sign In
                    </Link>
                    <Link to="/signup" className="home-button">
                        Sign Up
                    </Link>
                </div>
            </main>
        </div>
    );
};

export default Home;

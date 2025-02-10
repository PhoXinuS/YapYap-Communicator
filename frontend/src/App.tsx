// App.tsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Home from './pages/Home';
import SignUp from './pages/SignUp';
import SignIn from './pages/SignIn';
import FriendList from './pages/FriendList';
import Conversations from './pages/Conversations';
import Posts from './pages/Posts';
import Profile from './pages/Profile';
import ProtectedRoute from './components/ProtectedRoute';

const App: React.FC = () => {
    return (
        <Router>
            <AuthProvider>  {/* Make sure AuthProvider is wrapping the app */}
                <Routes>
                    {/* Public Routes */}
                    <Route path="/" element={<Home />} />
                    <Route path="/signup" element={<SignUp />} />
                    <Route path="/signin" element={<SignIn />} />

                    {/* Protected Routes */}
                    <Route path="/friend-list" element={<ProtectedRoute><FriendList /></ProtectedRoute>} />
                    <Route path="/conversations" element={<ProtectedRoute><Conversations /></ProtectedRoute>} />
                    <Route path="/posts" element={<ProtectedRoute><Posts /></ProtectedRoute>} />
                    <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />

                    {/* Catch-all for 404 */}
                    <Route path="*" element={<h1>404 - Page Not Found</h1>} />
                </Routes>
            </AuthProvider>
        </Router>
    );
};

export default App;

// src/pages/SignIn.tsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../ui/SignIn.css';  // Import the CSS file

const SignIn: React.FC = () => {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError(null);

        try {
            await login(email, password);
            navigate('/posts');
        } catch (err: any) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    const goToSignUp = () => {
        navigate('/signup');  // Navigate to the sign-up page
    };

    return (
        <div className="signin-container">
            <header className="SignIn-header">
                <img src="https://media1.tenor.com/m/7aEyY3k7WycAAAAC/yap-yapping.gif" alt="React Logo" className="home-logo" />
                <h1 className="home-title">YapYap</h1>
                
            </header>
            <form onSubmit={handleSubmit} className="signin-form">
                <div className="form-group">
                    <label className="form-label">Email</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        className="form-input"
                    />
                </div>
                <div className="form-group">
                    <label className="form-label">Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        className="form-input"
                    />
                </div>
                {error && <p className="error-message">{error}</p>}
                <button type="submit" disabled={isLoading} className="signin-button">
                    {isLoading ? 'Signing In...' : 'Sign In'}
                </button>
            </form>
            <div className="signup-redirect">
                <p>Don't have an account? <span onClick={goToSignUp} className="signup-link">Sign Up</span></p>
            </div>
        </div>
    );
};

export default SignIn;

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../ui/SignUp.css';


const SignUp: React.FC = () => {
    const [name, setName] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(false);  // To handle loading state
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        // Check for matching passwords
        if (password !== confirmPassword) {
            setError('Passwords do not match!');
            return;
        }

        try {
            const userData = {
                id: 0, 
                email: email,
                name: name,
                password: password,
                joinedAt: ""  // Sending empty string to indicate it should be handled by the backend
            };

            // Send POST request
            const response = await axios.post(`${process.env.REACT_APP_BACKEND_URL}/users/register`, userData, {
                headers: { 'Content-Type': 'application/json' },
            });

            console.log('User registered:', response.data);
            navigate('/signin');  // Redirect upon success

        } catch (err: any) {
            if (err.response) {
                setError(err.response.data || 'An error occurred during registration');
            } else {
                setError('Unable to connect to the server.');
            }
        }
    };

    return (
        <div className="signup-container">
            <header className="SignIn-header">
                <img src="https://media1.tenor.com/m/7aEyY3k7WycAAAAC/yap-yapping.gif" alt="React Logo" className="home-logo" />
                <h1 className="home-title">YapYap</h1>
            </header>
            <form className="signup-form" onSubmit={handleSubmit}>
                {error && <p className="signup-error">{error}</p>}
                <div>
                    <label htmlFor="name">Name</label>
                    <input
                        id="name"
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="email">Email</label>
                    <input
                        id="email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        id="password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="confirmPassword">Confirm Password</label>
                    <input
                        id="confirmPassword"
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <button type="submit" disabled={loading}>
                        {loading ? 'Signing Up...' : 'Sign Up'}
                    </button>
                </div>
            </form>
            <div className="signin-redirect">
                <p>Already have an account? <span className="signin-link" onClick={() => navigate('/signin')}>Sign in here</span></p>
            </div>
        </div>
    );
};

export default SignUp;

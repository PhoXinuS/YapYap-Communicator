import React, { createContext, useState, useContext, ReactNode, useEffect } from 'react';
import { User } from '../types/User';

interface AuthContextType {
    user: User | null;
    login: (email: string, password: string) => Promise<void>;
    logout: () => void;
    isAuthenticated: boolean;
}

interface AuthProviderProps {
    children: ReactNode;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
    const [user, setUser] = useState<User | null>(() => {
        const userData = localStorage.getItem('user');
        return userData ? JSON.parse(userData) : null;
    });

    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(() => {
        return !!localStorage.getItem('authToken');
    });

    useEffect(() => {
        const initializeAuth = async () => {
            const token = localStorage.getItem('authToken');
            const userData = localStorage.getItem('user');
            if (token && userData) {
                try {
                    const parsedUser = JSON.parse(userData);
                    setUser(parsedUser);
                    setIsAuthenticated(true);
                } catch (error) {
                    console.error('Failed to parse user data:', error);
                    logout();
                }
            }
        };

        initializeAuth();
    }, []);

    const login = async (email: string, password: string): Promise<void> => {
        try {
            const response = await fetch(`${process.env.REACT_APP_BACKEND_URL}/users/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password }),
            });

            if (!response.ok) {
                throw new Error('Login failed');
            }

            const token = await response.text();
            if (!token) {
                throw new Error('No token received');
            }

            localStorage.setItem('authToken', token);
            await fetchUserData();
        } catch (error: any) {
            console.error(error);
            throw new Error(error.message || 'An error occurred during login');
        }
    };

    const fetchUserData = async (): Promise<void> => {
        try {
            const token = localStorage.getItem('authToken');
            const response = await fetch(`${process.env.REACT_APP_BACKEND_URL}/users`, {
                headers: { 'Authorization': `Bearer ${token}` },
            });

            if (!response.ok) {
                throw new Error('Failed to fetch user data');
            }

            const userData = await response.json();
            setUser(userData);
            setIsAuthenticated(true);
            localStorage.setItem('user', JSON.stringify(userData));
        } catch (error) {
            console.error(error);
            logout();
        }
    };

    const logout = () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
        setUser(null);
        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, isAuthenticated }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

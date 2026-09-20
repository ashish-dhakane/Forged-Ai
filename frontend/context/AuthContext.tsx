'use client';

// Authentication context providing session state, login/logout operations, and route protection.
import React, { createContext, useContext, useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { User, AuthResponse } from '../types';
import { loginUser, registerUser, loginAsDemo, logoutUser, getCurrentUser } from '../services/authService';

interface AuthContextType {
  user: User | null;
  token: string | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  login: (email: string, pass: string) => Promise<void>;
  register: (name: string, email: string, pass: string, github?: string) => Promise<void>;
  loginDemo: () => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Context provider wrapping the application to manage persistent user authentication sessions.
export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const router = useRouter();

  // Restores stored user authentication state from localStorage on application mount.
  useEffect(() => {
    async function loadStoredAuth() {
      try {
        const storedToken = localStorage.getItem('forgeai_token');
        const storedUser = localStorage.getItem('forgeai_user');

        if (storedToken && storedUser) {
          setToken(storedToken);
          setUser(JSON.parse(storedUser));
          // Refresh user profile asynchronously
          getCurrentUser().then(setUser).catch(() => {});
        }
      } catch (err) {
        console.warn('Session restoration failed:', err);
      } finally {
        setIsLoading(false);
      }
    }
    loadStoredAuth();
  }, []);

  // Performs user login, sets session tokens, and navigates to the primary dashboard.
  const handleLogin = async (email: string, pass: string) => {
    setIsLoading(true);
    try {
      const authRes: AuthResponse = await loginUser(email, pass);
      setToken(authRes.token);
      setUser(authRes);
      router.push('/dashboard');
    } finally {
      setIsLoading(false);
    }
  };

  // Registers a new student account and redirects directly to dashboard with initial score baseline.
  const handleRegister = async (name: string, email: string, pass: string, github?: string) => {
    setIsLoading(true);
    try {
      const authRes: AuthResponse = await registerUser(name, email, pass, github);
      setToken(authRes.token);
      setUser(authRes);
      router.push('/dashboard');
    } finally {
      setIsLoading(false);
    }
  };

  // One-click instant login for viva presentation, loading pre-seeded engineering profile.
  const handleLoginDemo = async () => {
    setIsLoading(true);
    try {
      const authRes: AuthResponse = await loginAsDemo();
      setToken(authRes.token);
      setUser(authRes);
      router.push('/dashboard');
    } finally {
      setIsLoading(false);
    }
  };

  // Clears active session and redirects user to landing page.
  const handleLogout = () => {
    logoutUser();
    setUser(null);
    setToken(null);
    router.push('/login');
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isLoading,
        isAuthenticated: !!token,
        login: handleLogin,
        register: handleRegister,
        loginDemo: handleLoginDemo,
        logout: handleLogout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

// Hook providing easy access to authentication state and methods across components.
export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}

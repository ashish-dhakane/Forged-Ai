// API service managing user authentication, JWT storage, and demo logins.
import { apiRequest } from './api';
import { AuthResponse, User } from '../types';

// Logs in with email and password, saving the JWT token in localStorage.
export async function loginUser(email: string, password: string): Promise<AuthResponse> {
  const data = await apiRequest<AuthResponse>('/api/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password }),
  });
  if (data.token) {
    localStorage.setItem('forgeai_token', data.token);
    localStorage.setItem('forgeai_user', JSON.stringify(data));
  }
  return data;
}

// Registers a new user account and stores the returned JWT session.
export async function registerUser(name: string, email: string, password: string, githubUsername?: string): Promise<AuthResponse> {
  const data = await apiRequest<AuthResponse>('/api/auth/register', {
    method: 'POST',
    body: JSON.stringify({ name, email, password, githubUsername }),
  });
  if (data.token) {
    localStorage.setItem('forgeai_token', data.token);
    localStorage.setItem('forgeai_user', JSON.stringify(data));
  }
  return data;
}

// Provides instant one-click authentication for university viva and project demonstrations.
export async function loginAsDemo(): Promise<AuthResponse> {
  const data = await apiRequest<AuthResponse>('/api/auth/demo-login', {
    method: 'POST',
  });
  if (data.token) {
    localStorage.setItem('forgeai_token', data.token);
    localStorage.setItem('forgeai_user', JSON.stringify(data));
  }
  return data;
}

// Fetches the current authenticated user profile from backend /api/auth/me.
export async function getCurrentUser(): Promise<User> {
  return await apiRequest<User>('/api/auth/me');
}

// Clears the stored authentication token and user session data upon logout.
export function logoutUser(): void {
  if (typeof window !== 'undefined') {
    localStorage.removeItem('forgeai_token');
    localStorage.removeItem('forgeai_user');
  }
}

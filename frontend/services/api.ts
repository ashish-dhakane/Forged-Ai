// Centralized HTTP client handling JWT injection, backend requests, and structured error propagation.

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

// Retrieves the stored JWT authentication token from browser storage.
function getAuthToken(): string | null {
  if (typeof window !== 'undefined') {
    return localStorage.getItem('forgeai_token');
  }
  return null;
}

// Executes an HTTP request with JSON headers and automatic JWT Bearer token attachment.
export async function apiRequest<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
  const token = getAuthToken();

  const headers: HeadersInit = {
    'Content-Type': 'application/json',
    ...(options.headers || {}),
  };

  if (token) {
    (headers as Record<string, string>)['Authorization'] = `Bearer ${token}`;
  }

  const url = `${API_BASE_URL}${endpoint}`;

  try {
    const response = await fetch(url, {
      ...options,
      headers,
    });

    if (!response.ok) {
      const errorBody = await response.json().catch(() => ({}));
      const message = errorBody.message || errorBody.error || `HTTP ${response.status}: ${response.statusText}`;
      throw new Error(message);
    }

    return (await response.json()) as T;
  } catch (error: any) {
    console.error(`API Request failed for ${endpoint}:`, error.message);
    throw error;
  }
}

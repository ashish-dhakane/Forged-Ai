// API service querying synced GitHub repositories, language distributions, and commit insights.
import { apiRequest } from './api';
import { Repository } from '../types';

export interface GithubSnapshot {
  totalRepositories: number;
  totalStars: number;
  totalForks: number;
  languages: { name: string; count: number; percentage: number }[];
  recentActivity: { day: string; commits: number }[];
  topProjects: Repository[];
}

// Retrieves all synced repositories for the authenticated user.
export async function getGithubRepositories(): Promise<Repository[]> {
  return await apiRequest<Repository[]>('/api/github/repos');
}

// Synchronizes repositories directly from GitHub for a given username or token.
export async function syncGithubRepositories(githubUsername?: string, token?: string): Promise<Repository[]> {
  return await apiRequest<Repository[]>('/api/github/sync', {
    method: 'POST',
    body: JSON.stringify({ githubUsername, token }),
  });
}

// Retrieves aggregate language distribution and repository commit metrics.
export async function getGithubSnapshot(): Promise<GithubSnapshot> {
  return await apiRequest<GithubSnapshot>('/api/github/snapshot');
}

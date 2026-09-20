// API service fetching engineering missions and submitting completed assignments for XP rewards.
import { apiRequest } from './api';
import { Mission } from '../types';

// Retrieves all practical engineering missions.
export async function getAllMissions(): Promise<Mission[]> {
  return await apiRequest<Mission[]>('/api/missions');
}

// Retrieves all user mission submissions.
export async function getUserMissionSubmissions(): Promise<any[]> {
  return await apiRequest<any[]>('/api/missions/submissions');
}

// Submits verification details for a mission, marks it complete, and awards XP.
export async function submitMission(
  missionId: number,
  repositoryUrl: string,
  submissionNotes: string
): Promise<any> {
  return await apiRequest<any>(`/api/missions/${missionId}/submit`, {
    method: 'POST',
    body: JSON.stringify({ repositoryUrl, submissionNotes }),
  });
}

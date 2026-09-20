// API service retrieving comprehensive profile details and Engineer DNA dimensions.
import { apiRequest } from './api';
import { EngineerDnaDimension } from '../types';

export interface ProfileData {
  id: number;
  name: string;
  email: string;
  githubUsername: string;
  bio: string;
  xp: number;
  level: number;
  streak: number;
  skills: string[];
  technologies: string[];
  languages: { name: string; percentage: number }[];
  experience: string;
  education: string;
  projects: any[];
}

export interface DnaResponse {
  dimensions: EngineerDnaDimension[];
  aiInsight: string;
  isAiGenerated: boolean;
}

// Retrieves the student's complete engineering profile and project records.
export async function getProfileData(): Promise<ProfileData> {
  return await apiRequest<ProfileData>('/api/profile');
}

// Retrieves the 6-dimension Engineer DNA scores and AI commentary.
export async function getEngineerDna(): Promise<DnaResponse> {
  return await apiRequest<DnaResponse>('/api/profile/dna');
}

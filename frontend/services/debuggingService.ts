// API service managing interactive debugging challenges and evaluating student diagnostic fixes.
import { apiRequest } from './api';
import { DebuggingChallenge } from '../types';

export interface DebuggingResponse {
  isSuccessful: boolean;
  attemptNumber: number;
  feedback: string;
  xpAwarded: number;
  currentXp: number;
  currentLevel: number;
  idealSolutionCode?: string;
}

// Retrieves all debugging challenges across Easy, Medium, and Hard tiers.
export async function getAllDebuggingChallenges(): Promise<DebuggingChallenge[]> {
  return await apiRequest<DebuggingChallenge[]>('/api/debugging/challenges');
}

// Retrieves a single debugging challenge by ID.
export async function getDebuggingChallengeById(id: number): Promise<DebuggingChallenge> {
  return await apiRequest<DebuggingChallenge>(`/api/debugging/challenges/${id}`);
}

// Submits the student's root-cause explanation and corrected code for automated evaluation.
export async function submitDebuggingFix(
  challengeId: number,
  userExplanation: string,
  userFixedCode: string
): Promise<DebuggingResponse> {
  return await apiRequest<DebuggingResponse>(`/api/debugging/challenges/${challengeId}/submit`, {
    method: 'POST',
    body: JSON.stringify({ userExplanation, userFixedCode }),
  });
}

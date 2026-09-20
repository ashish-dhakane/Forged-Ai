// API service delivering technical assessments, question retrieval, and answer submissions.
import { apiRequest } from './api';
import { Assessment, AssessmentResult } from '../types';

// Retrieves all available technical skill assessments across categories.
export async function getAllAssessments(): Promise<Assessment[]> {
  return await apiRequest<Assessment[]>('/api/assessments');
}

// Retrieves a specific assessment along with its technical questions.
export async function getAssessmentById(id: number): Promise<Assessment> {
  return await apiRequest<Assessment>(`/api/assessments/${id}`);
}

// Submits the candidate's answers to an assessment and returns score percentage and tier.
export async function submitAssessmentAnswers(assessmentId: number, answers: Record<number, number>): Promise<AssessmentResult> {
  return await apiRequest<AssessmentResult>('/api/assessments/submit', {
    method: 'POST',
    body: JSON.stringify({ assessmentId, answers }),
  });
}

// Retrieves previous completed assessment results for the user.
export async function getUserAssessmentResults(): Promise<AssessmentResult[]> {
  return await apiRequest<AssessmentResult[]>('/api/assessments/results');
}

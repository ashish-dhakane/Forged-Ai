// API service conducting AI technical interview simulations and fetching hiring readiness evaluations.
import { apiRequest } from './api';
import { Interview, InterviewEvaluation } from '../types';

// Starts a new technical interview session tailored to the requested role and difficulty.
export async function startInterview(role: string, difficulty: string, category: string): Promise<Interview> {
  return await apiRequest<Interview>('/api/interview/start', {
    method: 'POST',
    body: JSON.stringify({ role, difficulty, category }),
  });
}

// Submits the candidate's response for a specific interview question.
export async function submitInterviewAnswer(questionId: number, userAnswer: string): Promise<any> {
  return await apiRequest<any>('/api/interview/answer', {
    method: 'POST',
    body: JSON.stringify({ questionId, userAnswer }),
  });
}

// Concludes the interview and returns a comprehensive hiring scorecard and readiness report.
export async function evaluateInterview(interviewId: number): Promise<InterviewEvaluation> {
  return await apiRequest<InterviewEvaluation>(`/api/interview/${interviewId}/evaluate`, {
    method: 'POST',
  });
}

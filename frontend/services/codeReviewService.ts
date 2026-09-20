// API service submitting code snippets to the AI review backend for structured analysis.
import { apiRequest } from './api';
import { CodeReviewFeedback } from '../types';

// Sends user code and programming language to the AI review endpoint and receives structured feedback.
export async function analyzeCode(codeSnippet: string, language: string): Promise<CodeReviewFeedback> {
  return await apiRequest<CodeReviewFeedback>('/api/code-review/analyze', {
    method: 'POST',
    body: JSON.stringify({ codeSnippet, language }),
  });
}

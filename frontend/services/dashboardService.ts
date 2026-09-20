// API service retrieving dashboard engineering scores, readiness metrics, and focus guidance.
import { apiRequest } from './api';
import { EngineeringScore } from '../types';

// Fetches the user's composite Engineering Score and Industry Readiness metrics from the backend.
export async function getDashboardOverview(): Promise<EngineeringScore> {
  return await apiRequest<EngineeringScore>('/api/dashboard/overview');
}

// Retrieves current recommended learning focus area and pedagogical rationale.
export async function getDashboardFocus(): Promise<{ currentFocus: string; focusReason: string }> {
  return await apiRequest<{ currentFocus: string; focusReason: string }>('/api/dashboard/focus');
}

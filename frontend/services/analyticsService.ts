// API service retrieving longitudinal analytics trends and skill evolution metrics.
import { apiRequest } from './api';

export interface AnalyticsData {
  period: string;
  scoreHistory: { date: string; score: number; readiness: number }[];
  xpGrowth: { day: string; xp: number }[];
  skillEvolution: { skill: string; baseline: number; current: number }[];
  isDemoData: boolean;
  learningConsistency: number;
  activeDaysInPeriod: number;
}

// Retrieves engineering score progression, XP velocity, and skill evolution over 7d, 30d, or 90d periods.
export async function getAnalyticsTrends(period: '7d' | '30d' | '90d' = '30d'): Promise<AnalyticsData> {
  return await apiRequest<AnalyticsData>(`/api/analytics/trends?period=${period}`);
}

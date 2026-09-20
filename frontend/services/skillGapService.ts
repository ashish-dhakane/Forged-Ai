// API service fetching detected skill gaps and triggering gap recalculations.
import { apiRequest } from './api';
import { SkillGap } from '../types';

// Retrieves all current skill gaps identified for the student.
export async function getSkillGaps(): Promise<SkillGap[]> {
  return await apiRequest<SkillGap[]>('/api/skill-gaps');
}

// Recomputes skill gaps against industry benchmarks using latest assessment and repository data.
export async function recalculateSkillGaps(): Promise<SkillGap[]> {
  return await apiRequest<SkillGap[]>('/api/skill-gaps/recalculate', {
    method: 'POST',
  });
}

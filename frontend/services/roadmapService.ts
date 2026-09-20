// API service managing personalized roadmap progress, phases, and milestone status.
import { apiRequest } from './api';
import { Roadmap, RoadmapItem } from '../types';

// Retrieves the student's active 5-phase personalized engineering roadmap.
export async function getRoadmap(): Promise<Roadmap> {
  return await apiRequest<Roadmap>('/api/roadmap');
}

// Updates milestone status and progress percentage for a specific roadmap item.
export async function updateRoadmapItemProgress(
  itemId: number,
  status: 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED',
  progressPercentage: number
): Promise<RoadmapItem> {
  return await apiRequest<RoadmapItem>(`/api/roadmap/items/${itemId}/progress`, {
    method: 'PATCH',
    body: JSON.stringify({ status, progressPercentage }),
  });
}

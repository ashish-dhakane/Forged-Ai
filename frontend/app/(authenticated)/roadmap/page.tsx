'use client';

// Personalized Engineering Roadmap page showing phased learning paths and interactive progress tracking.
import React, { useEffect, useState } from 'react';
import {
  Milestone,
  CheckCircle2,
  Clock,
  BookOpen,
  ArrowRight,
  Play,
  Check,
  Sparkles,
} from 'lucide-react';
import { getRoadmap, updateRoadmapItemProgress } from '../../../services/roadmapService';
import { Roadmap, RoadmapItem } from '../../../types';
import ProgressBar from '../../../components/ui/ProgressBar';
import DemoBadge from '../../../components/layout/DemoBadge';
import { useAuth } from '../../../context/AuthContext';

export default function RoadmapPage() {
  const { user } = useAuth();
  const [roadmap, setRoadmap] = useState<Roadmap | null>(null);
  const [loading, setLoading] = useState(true);
  const [updatingId, setUpdatingId] = useState<number | null>(null);

  // Fetches the user's active personalized engineering roadmap on mount.
  useEffect(() => {
    async function loadData() {
      try {
        const data = await getRoadmap();
        setRoadmap(data);
      } catch (err) {
        console.error('Failed to load roadmap:', err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, []);

  // Updates status and progress percentage for a specific milestone item.
  const handleStatusChange = async (itemId: number, newStatus: 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED', percentage: number) => {
    setUpdatingId(itemId);
    try {
      const updatedItem = await updateRoadmapItemProgress(itemId, newStatus, percentage);
      // Re-fetch roadmap to update overall progress percentage
      const refreshed = await getRoadmap();
      setRoadmap(refreshed);
    } catch (err) {
      console.error('Failed to update milestone:', err);
    } finally {
      setUpdatingId(null);
    }
  };

  if (loading || !roadmap) {
    return (
      <div className="flex items-center justify-center py-20">
        <div className="w-6 h-6 border-2 border-blue-500 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  // Group roadmap items by phase number (1 to 5)
  const phases = [1, 2, 3, 4, 5].map((phaseNum) => {
    const items = roadmap.items ? roadmap.items.filter((item) => item.phaseNumber === phaseNum) : [];
    const title = items.length > 0 ? items[0].phaseTitle : `Phase ${phaseNum}`;
    return { phaseNum, title, items };
  });

  return (
    <div className="space-y-8 max-w-5xl">
      {/* Roadmap Overview Banner */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 sm:p-8 shadow-sm">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <span className="text-xs font-mono uppercase tracking-wider text-blue-400">
                Target: {roadmap.targetRole}
              </span>
              {user?.isDemo && <DemoBadge />}
            </div>
            <h1 className="text-xl font-bold text-white mt-1">{roadmap.title}</h1>
            <p className="text-xs text-slate-400 mt-1">
              Dynamically adapted curriculum prioritized to close your high-severity Testing and Debugging skill gaps.
            </p>
          </div>

          <div className="sm:w-64 p-4 rounded-xl bg-slate-950 border border-slate-800">
            <ProgressBar
              percentage={roadmap.overallProgress}
              label="Overall Completion"
              color="emerald"
            />
          </div>
        </div>
      </div>

      {/* Phased Roadmap Timeline */}
      <div className="space-y-6">
        {phases.map(({ phaseNum, title, items }) => (
          <div key={phaseNum} className="bg-slate-900 border border-slate-800 rounded-xl p-6">
            <div className="flex items-center justify-between pb-3 border-b border-slate-800 mb-4">
              <div className="flex items-center gap-2.5">
                <div className="w-7 h-7 rounded-lg bg-blue-500/10 text-blue-400 border border-blue-500/20 flex items-center justify-center font-bold text-xs font-mono">
                  0{phaseNum}
                </div>
                <h3 className="text-sm font-bold text-white">{title}</h3>
              </div>
              <span className="text-xs text-slate-400 font-mono">{items.length} Milestones</span>
            </div>

            <div className="space-y-3.5">
              {items.map((item) => {
                const isCompleted = item.status === 'COMPLETED';
                const isInProgress = item.status === 'IN_PROGRESS';
                const isUpdating = updatingId === item.id;

                return (
                  <div
                    key={item.id}
                    className={`p-4 rounded-xl border transition-colors ${
                      isCompleted
                        ? 'bg-slate-950/60 border-slate-800/60'
                        : isInProgress
                        ? 'bg-slate-950 border-blue-900/40 shadow-sm'
                        : 'bg-slate-950/40 border-slate-800'
                    }`}
                  >
                    <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
                      <div>
                        <div className="flex items-center gap-2">
                          <span className="text-xs font-semibold text-white">{item.skillName}</span>
                          <span
                            className={`text-[10px] px-2 py-0.2 rounded font-mono ${
                              isCompleted
                                ? 'bg-emerald-500/10 text-emerald-400'
                                : isInProgress
                                ? 'bg-blue-500/10 text-blue-400'
                                : 'bg-slate-800 text-slate-400'
                            }`}
                          >
                            {item.status.replace('_', ' ')}
                          </span>
                        </div>
                        <p className="text-[11px] text-slate-400 mt-1 flex items-center gap-3">
                          <span>{item.currentLevel} → {item.targetLevel}</span>
                          <span>•</span>
                          <span className="flex items-center gap-1">
                            <Clock className="w-3 h-3" />
                            ~{item.estimatedHours} hrs effort
                          </span>
                        </p>
                      </div>

                      {/* Action buttons (Start, In Progress, Complete) */}
                      <div className="flex items-center gap-2 shrink-0">
                        {!isCompleted && !isInProgress && (
                          <button
                            onClick={() => handleStatusChange(item.id, 'IN_PROGRESS', 50)}
                            disabled={isUpdating}
                            className="px-3 py-1 rounded bg-blue-600 hover:bg-blue-500 text-white text-xs font-medium transition-colors flex items-center gap-1"
                          >
                            <Play className="w-3 h-3" />
                            <span>Start</span>
                          </button>
                        )}

                        {isInProgress && (
                          <button
                            onClick={() => handleStatusChange(item.id, 'COMPLETED', 100)}
                            disabled={isUpdating}
                            className="px-3 py-1 rounded bg-emerald-600 hover:bg-emerald-500 text-white text-xs font-medium transition-colors flex items-center gap-1"
                          >
                            <Check className="w-3 h-3" />
                            <span>Mark Done</span>
                          </button>
                        )}

                        {isCompleted && (
                          <span className="flex items-center gap-1 text-xs text-emerald-400 font-medium">
                            <CheckCircle2 className="w-3.5 h-3.5" />
                            <span>Mastered</span>
                          </span>
                        )}
                      </div>
                    </div>

                    {/* Recommended Resources link */}
                    {item.recommendedResources && (
                      <div className="mt-3 pt-2.5 border-t border-slate-900 flex items-center justify-between text-xs">
                        <span className="text-[11px] text-slate-400 flex items-center gap-1.5 truncate">
                          <BookOpen className="w-3 h-3 text-slate-400 shrink-0" />
                          <span>Curated: {item.recommendedResources}</span>
                        </span>
                        <span className="text-xs text-slate-400 font-mono shrink-0 ml-2">
                          {item.progressPercentage}%
                        </span>
                      </div>
                    )}
                  </div>
                );
              })}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

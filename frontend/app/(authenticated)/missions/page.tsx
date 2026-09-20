'use client';

// Engineering Missions dashboard providing production assignments and XP-based skill progression.
import React, { useEffect, useState } from 'react';
import {
  CheckCircle2,
  Zap,
  Clock,
  ArrowRight,
  ExternalLink,
  Award,
  Sparkles,
  FileCode,
} from 'lucide-react';
import {
  getAllMissions,
  getUserMissionSubmissions,
  submitMission,
} from '../../../services/missionService';
import { Mission } from '../../../types';
import { useAuth } from '../../../context/AuthContext';

export default function MissionsPage() {
  const { user } = useAuth();
  const [missions, setMissions] = useState<Mission[]>([]);
  const [submissions, setSubmissions] = useState<Record<number, any>>({});
  const [selectedMission, setSelectedMission] = useState<Mission | null>(null);
  const [repoUrl, setRepoUrl] = useState('');
  const [notes, setNotes] = useState('');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  // Loads practical engineering assignments and user submissions on mount.
  useEffect(() => {
    async function loadData() {
      try {
        const [allMissions, userSubs] = await Promise.all([
          getAllMissions(),
          getUserMissionSubmissions(),
        ]);
        setMissions(allMissions);

        const subMap: Record<number, any> = {};
        userSubs.forEach((sub: any) => {
          if (sub.mission && sub.mission.id) {
            subMap[sub.mission.id] = sub;
          }
        });
        setSubmissions(subMap);
      } catch (err) {
        console.error('Failed to load missions:', err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, []);

  // Submits verification details for a mission to claim XP.
  const handleSubmitMission = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedMission) return;
    setSubmitting(true);
    try {
      const result = await submitMission(selectedMission.id, repoUrl, notes);
      setSubmissions((prev) => ({
        ...prev,
        [selectedMission.id]: result,
      }));
      setSuccessMessage(`Mission verified! +${selectedMission.xpReward} XP awarded.`);
      setTimeout(() => {
        setSelectedMission(null);
        setSuccessMessage(null);
        setRepoUrl('');
        setNotes('');
      }, 1500);
    } catch (err) {
      console.error('Mission submission error:', err);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-20">
        <div className="w-6 h-6 border-2 border-blue-500 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  return (
    <div className="space-y-8 max-w-5xl">
      {/* Header Overview */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h2 className="text-base font-semibold text-white">Hands-on Engineering Missions</h2>
            <p className="text-xs text-slate-400 mt-1">
              Production-grade assignments to bridge detected gaps in testing, concurrency, and application hardening.
            </p>
          </div>
          <div className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-slate-950 border border-slate-800 text-xs">
            <Zap className="w-4 h-4 text-amber-400 fill-amber-400" />
            <span className="text-slate-300 font-semibold">{user?.xp || 2450} Total XP</span>
          </div>
        </div>
      </div>

      {/* Submission Modal / View */}
      {selectedMission && (
        <div className="bg-slate-900 border border-blue-500/30 rounded-xl p-6 animate-in fade-in">
          <div className="flex items-center justify-between pb-3 border-b border-slate-800 mb-4">
            <div>
              <span className="text-[10px] font-mono uppercase tracking-wider text-blue-400">
                Submit Mission Verification
              </span>
              <h3 className="text-sm font-bold text-white mt-0.5">{selectedMission.title}</h3>
            </div>
            <button
              onClick={() => setSelectedMission(null)}
              className="text-xs text-slate-400 hover:text-white px-2 py-1 rounded bg-slate-800"
            >
              Cancel
            </button>
          </div>

          {successMessage ? (
            <div className="p-4 rounded-lg bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-xs font-semibold text-center flex items-center justify-center gap-2">
              <CheckCircle2 className="w-4 h-4" />
              <span>{successMessage}</span>
            </div>
          ) : (
            <form onSubmit={handleSubmitMission} className="space-y-3.5">
              <div>
                <label className="block text-xs font-medium text-slate-300 mb-1">
                  GitHub Repository / PR URL (Optional)
                </label>
                <input
                  type="url"
                  value={repoUrl}
                  onChange={(e) => setRepoUrl(e.target.value)}
                  placeholder="https://github.com/your-username/project/pull/1"
                  className="w-full px-3 py-2 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 placeholder-slate-400 focus:outline-none focus:border-blue-500"
                />
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-300 mb-1">
                  Implementation Summary & Technical Notes
                </label>
                <textarea
                  required
                  rows={3}
                  value={notes}
                  onChange={(e) => setNotes(e.target.value)}
                  placeholder="Briefly describe how you satisfied the requirements (e.g. Added Mockito tests covering edge case nulls and verified 85% coverage with JaCoCo)."
                  className="w-full px-3 py-2 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 placeholder-slate-400 focus:outline-none focus:border-blue-500"
                />
              </div>

              <div className="flex justify-end gap-2 pt-2">
                <button
                  type="submit"
                  disabled={submitting}
                  className="px-4 py-2 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs font-medium transition-colors shadow-sm disabled:opacity-50"
                >
                  {submitting ? 'Verifying...' : `Complete Mission (+${selectedMission.xpReward} XP)`}
                </button>
              </div>
            </form>
          )}
        </div>
      )}

      {/* Missions Grid */}
      <div className="space-y-4">
        {missions.map((mission) => {
          const submission = submissions[mission.id];
          const isCompleted = submission && submission.status === 'COMPLETED';

          const requirements = mission.requirements ? mission.requirements.split('||') : [];

          return (
            <div
              key={mission.id}
              className={`bg-slate-900 border rounded-xl p-6 transition-colors ${
                isCompleted ? 'border-emerald-900/40 bg-slate-900/70' : 'border-slate-800 hover:border-slate-700'
              }`}
            >
              <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 pb-3 border-b border-slate-800">
                <div>
                  <div className="flex items-center gap-2">
                    <span className="text-[10px] font-semibold px-2 py-0.5 rounded bg-blue-500/10 text-blue-400 border border-blue-500/20 font-mono uppercase">
                      {mission.skillCategory}
                    </span>
                    <span className="text-[11px] text-slate-400 font-mono">{mission.difficulty}</span>
                  </div>
                  <h3 className="text-sm font-bold text-white mt-1.5">{mission.title}</h3>
                </div>

                <div className="flex items-center gap-3">
                  <div className="flex items-center gap-1 text-xs font-bold text-amber-400 font-mono">
                    <Zap className="w-3.5 h-3.5 fill-amber-400" />
                    <span>+{mission.xpReward} XP</span>
                  </div>

                  {isCompleted ? (
                    <span className="px-2.5 py-0.5 rounded text-xs font-semibold bg-emerald-500/15 text-emerald-400 border border-emerald-500/30 flex items-center gap-1">
                      <CheckCircle2 className="w-3.5 h-3.5" />
                      <span>Completed</span>
                    </span>
                  ) : (
                    <button
                      onClick={() => setSelectedMission(mission)}
                      className="px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs font-medium transition-colors"
                    >
                      Submit Verification
                    </button>
                  )}
                </div>
              </div>

              <p className="text-xs text-slate-300 mt-3 leading-relaxed">{mission.description}</p>

              {/* Requirements checklist */}
              {requirements.length > 0 && (
                <div className="mt-3.5 p-3 rounded-lg bg-slate-950 border border-slate-800/80">
                  <span className="text-[11px] font-semibold text-slate-400 block mb-1.5">
                    Verification Requirements:
                  </span>
                  <ul className="space-y-1 text-xs text-slate-300">
                    {requirements.map((req, idx) => (
                      <li key={idx} className="flex items-center gap-2">
                        <CheckCircle2 className="w-3 h-3 text-slate-400" />
                        <span>{req}</span>
                      </li>
                    ))}
                  </ul>
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}

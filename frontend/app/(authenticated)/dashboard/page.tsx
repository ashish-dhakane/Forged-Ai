'use client';

// Main ForgeAI dashboard displaying calculated Engineering Score, Industry Readiness, and category breakdowns.
import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import {
  Zap,
  Flame,
  GitBranch,
  Target,
  ArrowRight,
  TrendingUp,
  AlertTriangle,
  Award,
  CheckCircle2,
} from 'lucide-react';
import {
  ResponsiveContainer,
  RadarChart,
  PolarGrid,
  PolarAngleAxis,
  PolarRadiusAxis,
  Radar,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
} from 'recharts';
import StatCard from '../../../components/ui/StatCard';
import DemoBadge from '../../../components/layout/DemoBadge';
import ProgressBar from '../../../components/ui/ProgressBar';
import { getDashboardOverview } from '../../../services/dashboardService';
import { EngineeringScore } from '../../../types';
import { useAuth } from '../../../context/AuthContext';

export default function DashboardPage() {
  const { user } = useAuth();
  const [score, setScore] = useState<EngineeringScore | null>(null);
  const [loading, setLoading] = useState(true);
  const [mounted, setMounted] = useState(false);

  // Fetches calculated score breakdown from Spring Boot backend on mount.
  useEffect(() => {
    setMounted(true);
    async function loadData() {
      try {
        const data = await getDashboardOverview();
        setScore(data);
      } catch (err) {
        console.error('Failed to load dashboard metrics:', err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, []);

  if (loading || !score) {
    return (
      <div className="flex items-center justify-center py-20">
        <div className="w-6 h-6 border-2 border-blue-500 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  // Prepares radar data across the 10 weighted dimensions
  const radarData = [
    { category: 'Programming (20%)', score: score.programming, fullMark: 100 },
    { category: 'Problem Solving (15%)', score: score.problemSolving, fullMark: 100 },
    { category: 'Projects (15%)', score: score.projects, fullMark: 100 },
    { category: 'GitHub (10%)', score: score.github, fullMark: 100 },
    { category: 'Debugging (10%)', score: score.debugging, fullMark: 100 },
    { category: 'Testing (8%)', score: score.testing, fullMark: 100 },
    { category: 'System Design (8%)', score: score.systemDesign, fullMark: 100 },
    { category: 'Security (5%)', score: score.security, fullMark: 100 },
    { category: 'Communication (5%)', score: score.communication, fullMark: 100 },
    { category: 'Consistency (4%)', score: score.consistency, fullMark: 100 },
  ];

  // Industry readiness pillars bar data
  const readinessPillars = [
    { name: 'Technical', value: score.technicalReadiness },
    { name: 'Project Portfolio', value: score.projectReadiness },
    { name: 'Interview Readiness', value: score.interviewReadiness },
    { name: 'Engineering Practice', value: score.engineeringPracticeReadiness },
  ];

  return (
    <div className="space-y-8">
      {/* Top Welcome & KPI row */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-xl font-bold text-white tracking-tight">
              Welcome back, {user?.name || 'Engineer'}
            </h1>
            {score.isDemoData && <DemoBadge />}
          </div>
          <p className="text-xs text-slate-400 mt-0.5">
            Your transparent Engineering Score is calculated deterministically from verified competencies.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <Link
            href="/assessment"
            className="px-3.5 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs font-medium transition-colors flex items-center gap-1.5 shadow-sm"
          >
            <span>Take Skill Assessment</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </Link>
        </div>
      </div>

      {/* KPI Cards Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard
          title="Engineering Score"
          value={`${score.overallScore}/100`}
          subtitle="Weighted 10-dimension composite"
          icon={Award}
          badge="Formula-Calculated"
          badgeColor="blue"
        />
        <StatCard
          title="Industry Readiness"
          value={`${score.industryReadinessScore}/100`}
          subtitle="Hiring threshold benchmark: 75/100"
          icon={TrendingUp}
          badge="Near Industry Ready"
          badgeColor="emerald"
        />
        <StatCard
          title="XP & Growth Level"
          value={`Level ${user?.level || 4}`}
          subtitle={`${user?.xp || 2450} Total Experience Points`}
          icon={Zap}
          badge="+150 XP Available"
          badgeColor="amber"
        />
        <StatCard
          title="Consistency Streak"
          value={`${user?.streak || 7} Days`}
          subtitle="Active daily learning momentum"
          icon={Flame}
          badge="Top 10% Consistency"
          badgeColor="purple"
        />
      </div>

      {/* Current Pedagogical Focus Alert Card */}
      <div className="bg-slate-900 border border-amber-500/30 rounded-xl p-5 bg-gradient-to-r from-amber-500/5 via-slate-900 to-slate-900">
        <div className="flex items-start gap-3.5">
          <div className="p-2 rounded-lg bg-amber-500/10 text-amber-400 border border-amber-500/20 shrink-0">
            <Target className="w-5 h-5" />
          </div>
          <div className="flex-1">
            <div className="flex items-center gap-2">
              <span className="text-xs font-mono uppercase tracking-wider text-amber-400 font-semibold">
                Current Priority Focus
              </span>
              <span className="text-xs font-bold text-white">• {score.currentFocus}</span>
            </div>
            <p className="text-xs text-slate-300 mt-1 leading-relaxed">
              {score.focusReason}
            </p>
            <div className="mt-3 flex items-center gap-3">
              <Link
                href="/debugging"
                className="text-xs text-amber-400 hover:text-amber-300 font-medium flex items-center gap-1"
              >
                <span>Launch Debugging Challenge</span>
                <ArrowRight className="w-3.5 h-3.5" />
              </Link>
              <span className="text-slate-600">•</span>
              <Link
                href="/missions"
                className="text-xs text-slate-400 hover:text-slate-200 font-medium"
              >
                View Testing Missions
              </Link>
            </div>
          </div>
        </div>
      </div>

      {/* Charts Section: Radar Breakdown + Readiness Pillars */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Radar Chart: 10 Weighted Dimensions */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h3 className="text-sm font-semibold text-white">Engineering Snapshot</h3>
              <p className="text-xs text-slate-400">10-category deterministic weighted distribution</p>
            </div>
            <span className="text-xs font-mono text-slate-400">100 Max</span>
          </div>

          <div className="h-64 w-full">
            {mounted && (
              <ResponsiveContainer width="100%" height="100%">
                <RadarChart data={radarData}>
                  <PolarGrid stroke="#334155" />
                  <PolarAngleAxis dataKey="category" tick={{ fill: '#94a3b8', fontSize: 10 }} />
                  <PolarRadiusAxis angle={30} domain={[0, 100]} tick={{ fill: '#64748b', fontSize: 9 }} />
                  <Radar name="Candidate Score" dataKey="score" stroke="#3b82f6" fill="#3b82f6" fillOpacity={0.4} />
                </RadarChart>
              </ResponsiveContainer>
            )}
          </div>
        </div>

        {/* Bar Chart: Industry Readiness Pillars */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 flex flex-col justify-between">
          <div>
            <div className="flex items-center justify-between mb-4">
              <div>
                <h3 className="text-sm font-semibold text-white">Industry Readiness Pillars</h3>
                <p className="text-xs text-slate-400">Alignment with hiring rubric requirements</p>
              </div>
              <span className="text-xs font-semibold text-emerald-400 font-mono">
                {score.industryReadinessScore}% Average
              </span>
            </div>

            <div className="h-56 w-full">
              {mounted && (
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={readinessPillars} layout="vertical" margin={{ left: 20, right: 20 }}>
                    <XAxis type="number" domain={[0, 100]} tick={{ fill: '#64748b', fontSize: 10 }} />
                    <YAxis dataKey="name" type="category" tick={{ fill: '#94a3b8', fontSize: 11 }} width={120} />
                    <Tooltip
                      contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '8px', fontSize: '12px' }}
                    />
                    <Bar dataKey="value" fill="#10b981" radius={[0, 4, 4, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              )}
            </div>
          </div>

          <div className="border-t border-slate-800/80 pt-4 mt-2 flex items-center justify-between text-xs text-slate-400">
            <span>Deterministic formula: Weights sum to 100%</span>
            <Link href="/analytics" className="text-blue-400 hover:underline">
              View historical trends →
            </Link>
          </div>
        </div>
      </div>

      {/* Recommended Next Actions Checklist */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <h3 className="text-sm font-semibold text-white mb-4">Recommended Next Growth Actions</h3>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="p-4 rounded-lg bg-slate-950 border border-slate-800 flex items-start gap-3">
            <CheckCircle2 className="w-4 h-4 text-blue-400 shrink-0 mt-0.5" />
            <div>
              <div className="text-xs font-semibold text-white">Complete Unit Testing Mission</div>
              <p className="text-[11px] text-slate-400 mt-0.5">Write JUnit 5 tests to raise Testing competency from 58 to 68.</p>
              <Link href="/missions" className="text-[11px] text-blue-400 hover:underline mt-2 inline-block font-medium">
                Start Mission (+150 XP) →
              </Link>
            </div>
          </div>

          <div className="p-4 rounded-lg bg-slate-950 border border-slate-800 flex items-start gap-3">
            <CheckCircle2 className="w-4 h-4 text-indigo-400 shrink-0 mt-0.5" />
            <div>
              <div className="text-xs font-semibold text-white">Solve Python/Java Bug Challenge</div>
              <p className="text-[11px] text-slate-400 mt-0.5">Identify defect mechanism in concurrent worker pool.</p>
              <Link href="/debugging" className="text-[11px] text-indigo-400 hover:underline mt-2 inline-block font-medium">
                Debug Puzzle (+100 XP) →
              </Link>
            </div>
          </div>

          <div className="p-4 rounded-lg bg-slate-950 border border-slate-800 flex items-start gap-3">
            <CheckCircle2 className="w-4 h-4 text-emerald-400 shrink-0 mt-0.5" />
            <div>
              <div className="text-xs font-semibold text-white">Simulate Backend Tech Interview</div>
              <p className="text-[11px] text-slate-400 mt-0.5">Practice answering distributed concurrency & database locking rounds.</p>
              <Link href="/interview" className="text-[11px] text-emerald-400 hover:underline mt-2 inline-block font-medium">
                Launch Interview →
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

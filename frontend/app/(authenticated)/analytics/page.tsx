'use client';

// Analytics dashboard showing longitudinal score trajectory, XP velocity, and skill evolution with timeframe filtering.
import React, { useEffect, useState } from 'react';
import {
  LineChart as LineChartIcon,
  TrendingUp,
  Zap,
  Calendar,
  Award,
  Layers,
} from 'lucide-react';
import {
  ResponsiveContainer,
  LineChart,
  Line,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from 'recharts';
import { getAnalyticsTrends, AnalyticsData } from '../../../services/analyticsService';
import DemoBadge from '../../../components/layout/DemoBadge';
import StatCard from '../../../components/ui/StatCard';
import { useAuth } from '../../../context/AuthContext';

export default function AnalyticsPage() {
  const { user } = useAuth();
  const [period, setPeriod] = useState<'7d' | '30d' | '90d'>('30d');
  const [analytics, setAnalytics] = useState<AnalyticsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [mounted, setMounted] = useState(false);

  // Fetches longitudinal metrics whenever the timeframe filter changes.
  useEffect(() => {
    setMounted(true);
    async function loadData() {
      setLoading(true);
      try {
        const data = await getAnalyticsTrends(period);
        setAnalytics(data);
      } catch (err) {
        console.error('Failed to load analytics:', err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, [period]);

  return (
    <div className="space-y-8 max-w-6xl">
      {/* Top Header & Timeframe Switcher */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h2 className="text-base font-semibold text-white">Engineering Growth Analytics</h2>
              {user?.isDemo && <DemoBadge />}
            </div>
            <p className="text-xs text-slate-400 mt-1">
              Historical progression trends tracking verified competency gains, XP rate, and consistency.
            </p>
          </div>

          <div className="flex items-center gap-1.5 p-1 bg-slate-950 border border-slate-800 rounded-lg">
            {(['7d', '30d', '90d'] as const).map((p) => (
              <button
                key={p}
                onClick={() => setPeriod(p)}
                className={`px-3 py-1 rounded text-xs font-medium transition-colors ${
                  period === p
                    ? 'bg-blue-600 text-white shadow-sm'
                    : 'text-slate-400 hover:text-slate-200'
                }`}
              >
                {p === '7d' ? '7 Days' : p === '30d' ? '30 Days' : '90 Days'}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* KPI Row */}
      {analytics && (
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <StatCard
            title="Learning Consistency"
            value={`${analytics.learningConsistency}%`}
            subtitle="Streak maintenance and daily commits"
            icon={Calendar}
            badge="Strong Consistency"
            badgeColor="emerald"
          />
          <StatCard
            title="Active Practice Days"
            value={`${analytics.activeDaysInPeriod} Days`}
            subtitle={`In the selected ${period} timeframe`}
            icon={TrendingUp}
            badge="73% Active Rate"
            badgeColor="blue"
          />
          <StatCard
            title="XP Growth Velocity"
            value="+650 XP"
            subtitle="Accumulated in current timeframe"
            icon={Zap}
            badge="Top Velocity Tier"
            badgeColor="amber"
          />
        </div>
      )}

      {/* Score Progression Trajectory (Line Chart) */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex items-center justify-between mb-4">
          <div>
            <h3 className="text-sm font-semibold text-white">Engineering Score & Readiness Trajectory</h3>
            <p className="text-xs text-slate-400">Progression from initial baseline to current benchmark</p>
          </div>
        </div>

        <div className="h-72 w-full">
          {mounted && analytics && (
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={analytics.scoreHistory}>
                <CartesianGrid stroke="#1e293b" strokeDasharray="3 3" />
                <XAxis dataKey="date" tick={{ fill: '#94a3b8', fontSize: 11 }} />
                <YAxis domain={[40, 100]} tick={{ fill: '#64748b', fontSize: 11 }} />
                <Tooltip
                  contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '8px', fontSize: '12px' }}
                />
                <Legend />
                <Line
                  type="monotone"
                  dataKey="score"
                  name="Engineering Score"
                  stroke="#3b82f6"
                  strokeWidth={2.5}
                  dot={{ r: 4 }}
                />
                <Line
                  type="monotone"
                  dataKey="readiness"
                  name="Industry Readiness"
                  stroke="#10b981"
                  strokeWidth={2.5}
                  dot={{ r: 4 }}
                />
              </LineChart>
            </ResponsiveContainer>
          )}
        </div>
      </div>

      {/* Skill Evolution & XP Charts Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Skill Evolution Bar Chart */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h3 className="text-sm font-semibold text-white">Skill Evolution (Baseline vs Current)</h3>
              <p className="text-xs text-slate-400">Observed improvements across core technical pillars</p>
            </div>
          </div>

          <div className="h-64 w-full">
            {mounted && analytics && (
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={analytics.skillEvolution} margin={{ left: 10, right: 10 }}>
                  <CartesianGrid stroke="#1e293b" strokeDasharray="3 3" />
                  <XAxis dataKey="skill" tick={{ fill: '#94a3b8', fontSize: 10 }} />
                  <YAxis domain={[0, 100]} tick={{ fill: '#64748b', fontSize: 10 }} />
                  <Tooltip
                    contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '8px', fontSize: '12px' }}
                  />
                  <Legend />
                  <Bar dataKey="baseline" name="Initial Baseline" fill="#475569" radius={[4, 4, 0, 0]} />
                  <Bar dataKey="current" name="Current Score" fill="#6366f1" radius={[4, 4, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            )}
          </div>
        </div>

        {/* XP Velocity Chart */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h3 className="text-sm font-semibold text-white">XP Acquisition Cadence</h3>
              <p className="text-xs text-slate-400">Experience points awarded for missions, tests, and bug fixes</p>
            </div>
          </div>

          <div className="h-64 w-full">
            {mounted && analytics && (
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={analytics.xpGrowth}>
                  <CartesianGrid stroke="#1e293b" strokeDasharray="3 3" />
                  <XAxis dataKey="day" tick={{ fill: '#94a3b8', fontSize: 10 }} />
                  <YAxis tick={{ fill: '#64748b', fontSize: 10 }} />
                  <Tooltip
                    contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '8px', fontSize: '12px' }}
                  />
                  <Bar dataKey="xp" name="XP Gained" fill="#f59e0b" radius={[4, 4, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

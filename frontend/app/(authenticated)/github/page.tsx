'use client';

// GitHub Intelligence page allowing username sync, code health scoring, and AI repository audits.
import React, { useEffect, useState } from 'react';
import {
  GitBranch,
  Star,
  GitFork,
  RefreshCw,
  ExternalLink,
  ShieldAlert,
  CheckCircle2,
  Code2,
  Sparkles,
  Info,
} from 'lucide-react';
import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
} from 'recharts';
import {
  getGithubRepositories,
  syncGithubRepositories,
  getGithubSnapshot,
  GithubSnapshot,
} from '../../../services/githubService';
import { Repository } from '../../../types';
import DemoBadge from '../../../components/layout/DemoBadge';

export default function GithubPage() {
  const [repos, setRepos] = useState<Repository[]>([]);
  const [snapshot, setSnapshot] = useState<GithubSnapshot | null>(null);
  const [username, setUsername] = useState('demo-engineer');
  const [token, setToken] = useState('');
  const [loading, setLoading] = useState(true);
  const [syncing, setSyncing] = useState(false);
  const [mounted, setMounted] = useState(false);

  // Loads GitHub repositories and activity snapshot on initial render.
  useEffect(() => {
    setMounted(true);
    async function loadData() {
      try {
        const [reposRes, snapRes] = await Promise.all([
          getGithubRepositories(),
          getGithubSnapshot(),
        ]);
        setRepos(reposRes);
        setSnapshot(snapRes);
      } catch (err) {
        console.error('Failed to load GitHub data:', err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, []);

  // Triggers GitHub API synchronization with live endpoint or fallback sample data.
  const handleSync = async (e: React.FormEvent) => {
    e.preventDefault();
    setSyncing(true);
    try {
      const updatedRepos = await syncGithubRepositories(username, token);
      const updatedSnap = await getGithubSnapshot();
      setRepos(updatedRepos);
      setSnapshot(updatedSnap);
    } catch (err) {
      console.error('Sync failed:', err);
    } finally {
      setSyncing(false);
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
    <div className="space-y-8 max-w-6xl">
      {/* Synchronization Control Header */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h2 className="text-base font-semibold text-white">GitHub Integration & Intelligence</h2>
              <DemoBadge />
            </div>
            <p className="text-xs text-slate-400 mt-1">
              Synchronize repositories to analyze commit velocity, language distributions, and code architecture health.
            </p>
          </div>

          <form onSubmit={handleSync} className="flex flex-wrap items-center gap-2.5">
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="GitHub username"
              className="px-3 py-1.5 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 placeholder-slate-400 focus:outline-none focus:border-blue-500 w-44"
            />
            <input
              type="password"
              value={token}
              onChange={(e) => setToken(e.target.value)}
              placeholder="Token (optional)"
              className="px-3 py-1.5 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 placeholder-slate-400 focus:outline-none focus:border-blue-500 w-36"
            />
            <button
              type="submit"
              disabled={syncing}
              className="px-4 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs font-medium transition-colors flex items-center gap-1.5 shadow-sm disabled:opacity-50"
            >
              <RefreshCw className={`w-3.5 h-3.5 ${syncing ? 'animate-spin' : ''}`} />
              <span>{syncing ? 'Syncing...' : 'Sync GitHub'}</span>
            </button>
          </form>
        </div>
      </div>

      {/* GitHub Snapshot Stats Grid */}
      {snapshot && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="bg-slate-900 border border-slate-800 rounded-xl p-5">
            <span className="text-xs text-slate-400 font-medium uppercase tracking-wider">Repositories Tracked</span>
            <div className="text-2xl font-bold text-white mt-1">{snapshot.totalRepositories} Repos</div>
            <div className="text-xs text-slate-400 mt-1">
              {snapshot.totalStars} Stars • {snapshot.totalForks} Forks
            </div>
          </div>

          <div className="bg-slate-900 border border-slate-800 rounded-xl p-5">
            <span className="text-xs text-slate-400 font-medium uppercase tracking-wider">Top Detected Languages</span>
            <div className="mt-2 flex flex-wrap gap-1.5">
              {snapshot.languages.map((l) => (
                <span
                  key={l.name}
                  className="px-2.5 py-0.5 rounded text-[11px] font-medium bg-slate-950 border border-slate-800 text-slate-300"
                >
                  {l.name} ({l.percentage}%)
                </span>
              ))}
            </div>
          </div>

          <div className="bg-slate-900 border border-slate-800 rounded-xl p-5">
            <span className="text-xs text-slate-400 font-medium uppercase tracking-wider">Weekly Commit Cadence</span>
            <div className="h-14 w-full mt-2">
              {mounted && (
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={snapshot.recentActivity}>
                    <XAxis dataKey="day" tick={{ fill: '#64748b', fontSize: 10 }} axisLine={false} tickLine={false} />
                    <Tooltip
                      contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '6px', fontSize: '11px' }}
                    />
                    <Bar dataKey="commits" fill="#3b82f6" radius={[3, 3, 0, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              )}
            </div>
          </div>
        </div>
      )}

      {/* Repository Analysis Grid */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <div>
            <h3 className="text-sm font-semibold text-white">Repository Health & Architectural Audits</h3>
            <p className="text-xs text-slate-400">Automated static checks detecting testing gaps and maintainability</p>
          </div>
        </div>

        <div className="space-y-4">
          {repos.map((repo) => {
            const strengths = repo.strengthsJson ? repo.strengthsJson.split('||') : [];
            const improvements = repo.improvementsJson ? repo.improvementsJson.split('||') : [];

            return (
              <div
                key={repo.id}
                className="bg-slate-900 border border-slate-800 rounded-xl p-6 hover:border-slate-700 transition-colors"
              >
                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2 pb-4 border-b border-slate-800">
                  <div>
                    <div className="flex items-center gap-2.5">
                      <GitBranch className="w-4 h-4 text-blue-400" />
                      <h4 className="text-sm font-bold text-white tracking-tight">{repo.name}</h4>
                      <span className="text-[11px] px-2 py-0.5 rounded bg-blue-500/10 text-blue-400 border border-blue-500/20 font-mono">
                        {repo.language || 'Multi-Language'}
                      </span>
                    </div>
                    {repo.description && (
                      <p className="text-xs text-slate-400 mt-1 max-w-2xl">{repo.description}</p>
                    )}
                  </div>

                  <div className="flex items-center gap-4 text-xs font-medium">
                    <span className="flex items-center gap-1 text-slate-300">
                      <Star className="w-3.5 h-3.5 text-amber-400 fill-amber-400" />
                      {repo.starsCount}
                    </span>
                    <span className="flex items-center gap-1 text-slate-300">
                      <GitFork className="w-3.5 h-3.5 text-slate-400" />
                      {repo.forksCount}
                    </span>
                    <div className="flex items-center gap-1.5 px-2.5 py-1 rounded bg-slate-950 border border-slate-800">
                      <span className="text-[11px] text-slate-400">Health:</span>
                      <span className="text-xs font-bold text-emerald-400 font-mono">{repo.healthScore}/100</span>
                    </div>
                    {repo.htmlUrl && (
                      <a
                        href={repo.htmlUrl}
                        target="_blank"
                        rel="noreferrer"
                        className="text-slate-400 hover:text-white p-1 rounded transition-colors"
                      >
                        <ExternalLink className="w-4 h-4" />
                      </a>
                    )}
                  </div>
                </div>

                {/* AI-Assisted Architectural Insights */}
                <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
                  {/* Strengths */}
                  <div className="p-3.5 rounded-lg bg-slate-950/60 border border-slate-800/80">
                    <div className="flex items-center gap-1.5 text-xs font-semibold text-emerald-400 mb-2">
                      <CheckCircle2 className="w-3.5 h-3.5" />
                      <span>Observed Strengths</span>
                    </div>
                    <ul className="space-y-1 text-xs text-slate-300">
                      {strengths.map((s, idx) => (
                        <li key={idx} className="flex items-start gap-1.5">
                          <span className="text-emerald-500">•</span>
                          <span>{s}</span>
                        </li>
                      ))}
                    </ul>
                  </div>

                  {/* Potential Improvements */}
                  <div className="p-3.5 rounded-lg bg-slate-950/60 border border-slate-800/80">
                    <div className="flex items-center gap-1.5 text-xs font-semibold text-amber-400 mb-2">
                      <Sparkles className="w-3.5 h-3.5" />
                      <span>Potential Improvements</span>
                    </div>
                    <ul className="space-y-1 text-xs text-slate-300">
                      {improvements.map((imp, idx) => (
                        <li key={idx} className="flex items-start gap-1.5">
                          <span className="text-amber-500">•</span>
                          <span>{imp}</span>
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>

                {/* Detected Technologies */}
                {repo.detectedTechnologies && (
                  <div className="mt-3 text-[11px] text-slate-400 font-mono flex items-center gap-1.5">
                    <span className="text-slate-400">Stack:</span>
                    <span>{repo.detectedTechnologies}</span>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}

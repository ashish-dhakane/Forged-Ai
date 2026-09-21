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
  AlertCircle,
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
import { useAuth } from '../../../context/AuthContext';

export default function GithubPage() {
  const { user } = useAuth();
  const [repos, setRepos] = useState<Repository[]>([]);
  const [snapshot, setSnapshot] = useState<GithubSnapshot | null>(null);
  const [username, setUsername] = useState('');
  const [token, setToken] = useState('');
  const [loading, setLoading] = useState(true);
  const [syncing, setSyncing] = useState(false);
  const [syncError, setSyncError] = useState<string | null>(null);
  const [syncSuccess, setSyncSuccess] = useState<string | null>(null);
  const [mounted, setMounted] = useState(false);

  // Synchronize username default with authenticated user state
  useEffect(() => {
    if (user?.githubUsername) {
      setUsername(user.githubUsername);
    } else if (user?.isDemo) {
      setUsername('demo-engineer');
    }
  }, [user]);

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
      } catch (err: any) {
        console.error('Failed to load GitHub data:', err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, []);

  // Triggers GitHub API synchronization with live endpoint.
  const handleSync = async (e: React.FormEvent) => {
    e.preventDefault();
    setSyncing(true);
    setSyncError(null);
    setSyncSuccess(null);

    try {
      const updatedRepos = await syncGithubRepositories(username, token);
      const updatedSnap = await getGithubSnapshot();
      setRepos(updatedRepos);
      setSnapshot(updatedSnap);
      setSyncSuccess(`Successfully synchronized ${updatedRepos.length} repositories from GitHub.`);
      setTimeout(() => setSyncSuccess(null), 4000);
    } catch (err: any) {
      console.error('Sync failed:', err);
      setSyncError(err.message || 'Failed to synchronize with GitHub API.');
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

  const isDemoActive = user?.isDemo || (repos.length > 0 && repos[0]?.fullName?.startsWith('ashish-dhakane/'));

  return (
    <div className="space-y-8 max-w-6xl">
      {/* Synchronization Control Header */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h2 className="text-base font-semibold text-white">GitHub Integration & Intelligence</h2>
              {isDemoActive ? (
                <DemoBadge />
              ) : repos.length > 0 ? (
                <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-medium bg-emerald-500/10 text-emerald-400 border border-emerald-500/25">
                  <CheckCircle2 className="w-3 h-3" />
                  <span>LIVE GITHUB DATA</span>
                </span>
              ) : null}
            </div>
            <p className="text-xs text-slate-400 mt-1">
              Synchronize public repositories to analyze commit velocity, language distributions, and code architecture health.
            </p>
          </div>

          <form onSubmit={handleSync} className="flex flex-wrap items-center gap-2.5">
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="GitHub username"
              className="px-3 py-1.5 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 placeholder-slate-400 focus:outline-none focus:border-blue-500 w-44"
              required
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

        {/* Sync feedback alerts */}
        {syncError && (
          <div className="mt-4 p-3 rounded-lg bg-rose-500/10 border border-rose-500/25 flex items-center gap-2 text-xs text-rose-400">
            <AlertCircle className="w-4 h-4 shrink-0" />
            <span>{syncError}</span>
          </div>
        )}

        {syncSuccess && (
          <div className="mt-4 p-3 rounded-lg bg-emerald-500/10 border border-emerald-500/25 flex items-center gap-2 text-xs text-emerald-400">
            <CheckCircle2 className="w-4 h-4 shrink-0" />
            <span>{syncSuccess}</span>
          </div>
        )}
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
            <span className="text-xs text-slate-400 font-medium uppercase tracking-wider">Language Distribution</span>
            <div className="mt-2 flex flex-wrap gap-2">
              {snapshot.languages && snapshot.languages.length > 0 ? (
                snapshot.languages.map((lang, idx) => (
                  <span
                    key={idx}
                    className="inline-flex items-center gap-1 text-xs px-2 py-0.5 rounded bg-slate-950 border border-slate-800 text-slate-300"
                  >
                    <span className="w-1.5 h-1.5 rounded-full bg-blue-400" />
                    <span>{lang.name}</span>
                    <span className="text-slate-400 font-mono">({lang.percentage}%)</span>
                  </span>
                ))
              ) : (
                <span className="text-xs text-slate-400">No language data yet</span>
              )}
            </div>
          </div>

          <div className="bg-slate-900 border border-slate-800 rounded-xl p-5">
            <span className="text-xs text-slate-400 font-medium uppercase tracking-wider">Average Repository Health</span>
            <div className="text-2xl font-bold text-emerald-400 mt-1">
              {repos.length > 0
                ? Math.round(repos.reduce((acc, r) => acc + (r.healthScore || 0), 0) / repos.length) + '/100'
                : 'N/A'}
            </div>
            <div className="text-xs text-slate-400 mt-1">Heuristic architectural code metrics</div>
          </div>
        </div>
      )}

      {/* Synced Repositories List */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h3 className="text-sm font-semibold text-white">Analyzed Repositories & Architecture Health</h3>
            <p className="text-xs text-slate-400 mt-0.5">Automated code inspection across repositories</p>
          </div>
          <span className="text-xs text-slate-400 font-mono">{repos.length} Repositories</span>
        </div>

        {repos.length === 0 ? (
          <div className="text-center py-12 border border-dashed border-slate-800 rounded-lg">
            <Code2 className="w-8 h-8 text-slate-400 mx-auto mb-2" />
            <div className="text-xs font-semibold text-slate-300">No synchronized repositories yet</div>
            <p className="text-xs text-slate-400 mt-1 max-w-sm mx-auto">
              Enter your public GitHub username in the input above and click &quot;Sync GitHub&quot; to fetch and evaluate your engineering evidence.
            </p>
          </div>
        ) : (
          <div className="space-y-4">
            {repos.map((repo) => {
              const strengths = repo.strengthsJson ? repo.strengthsJson.split('||') : [];
              const improvements = repo.improvementsJson ? repo.improvementsJson.split('||') : [];

              return (
                <div
                  key={repo.id}
                  className="p-5 rounded-lg bg-slate-950 border border-slate-800 hover:border-slate-700 transition-colors"
                >
                  <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2">
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="text-sm font-bold text-white font-mono">{repo.name}</span>
                        {repo.language && (
                          <span className="text-[11px] px-2 py-0.5 rounded bg-blue-500/10 text-blue-400 border border-blue-500/20 font-medium">
                            {repo.language}
                          </span>
                        )}
                      </div>
                      <p className="text-xs text-slate-400 mt-1">
                        {repo.description || 'No description provided.'}
                      </p>
                    </div>

                    <div className="flex items-center gap-4 shrink-0">
                      <div className="flex items-center gap-3 text-xs text-slate-400">
                        <span className="flex items-center gap-1">
                          <Star className="w-3.5 h-3.5 text-amber-400" />
                          <span>{repo.starsCount}</span>
                        </span>
                        <span className="flex items-center gap-1">
                          <GitFork className="w-3.5 h-3.5 text-slate-400" />
                          <span>{repo.forksCount}</span>
                        </span>
                        <span className="px-2 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 font-mono font-semibold">
                          {Math.round(repo.healthScore)}% Health
                        </span>
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
        )}
      </div>
    </div>
  );
}

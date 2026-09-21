'use client';

// Interactive Debugging Engine presenting buggy code puzzles and evaluating student diagnostic reasoning.
import React, { useEffect, useState } from 'react';
import {
  Bug,
  HelpCircle,
  CheckCircle2,
  AlertCircle,
  Zap,
  ArrowRight,
  Code2,
  Terminal,
  RotateCcw,
} from 'lucide-react';
import {
  getAllDebuggingChallenges,
  submitDebuggingFix,
  DebuggingResponse,
} from '../../../services/debuggingService';
import { DebuggingChallenge } from '../../../types';
import DemoBadge from '../../../components/layout/DemoBadge';
import { useAuth } from '../../../context/AuthContext';

export default function DebuggingPage() {
  const { user } = useAuth();
  const [challenges, setChallenges] = useState<DebuggingChallenge[]>([]);
  const [activeChallenge, setActiveChallenge] = useState<DebuggingChallenge | null>(null);
  const [userExplanation, setUserExplanation] = useState('');
  const [userCode, setUserCode] = useState('');
  const [revealedHints, setRevealedHints] = useState<number[]>([]);
  const [response, setResponse] = useState<DebuggingResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  // Loads catalog of multi-language debugging puzzles on mount.
  useEffect(() => {
    async function loadData() {
      try {
        const data = await getAllDebuggingChallenges();
        setChallenges(data);
        if (data.length > 0) {
          selectChallenge(data[0]);
        }
      } catch (err) {
        console.error('Failed to load debugging challenges:', err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, []);

  // Switches active puzzle, initializing editor with the buggy code.
  const selectChallenge = (ch: DebuggingChallenge) => {
    setActiveChallenge(ch);
    setUserCode(ch.buggyCode);
    setUserExplanation('');
    setRevealedHints([]);
    setResponse(null);
  };

  // Submits the proposed code correction and diagnostic reasoning.
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!activeChallenge) return;
    setSubmitting(true);
    try {
      const res = await submitDebuggingFix(activeChallenge.id, userExplanation, userCode);
      setResponse(res);
    } catch (err) {
      console.error('Submission error:', err);
    } finally {
      setSubmitting(false);
    }
  };

  // Reveals hint sequentially
  const handleRevealHint = (hintIndex: number) => {
    if (!revealedHints.includes(hintIndex)) {
      setRevealedHints([...revealedHints, hintIndex]);
    }
  };

  if (loading || !activeChallenge) {
    return (
      <div className="flex items-center justify-center py-20">
        <div className="w-6 h-6 border-2 border-blue-500 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  let hints: string[] = [];
  try {
    hints = JSON.parse(activeChallenge.hintsJson);
  } catch {
    hints = ['Inspect boundary conditions', 'Check concurrency race hazards'];
  }

  return (
    <div className="space-y-6 max-w-6xl">
      {/* Top Header */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h2 className="text-base font-semibold text-white">Interactive Debugging Engine</h2>
              {user?.isDemo && <DemoBadge />}
            </div>
            <p className="text-xs text-slate-400 mt-1">
              Isolate defects across Python, TypeScript, Java, and Go. Explain the mechanism and submit clean fixes.
            </p>
          </div>

          <div className="flex items-center gap-2">
            <span className="text-xs text-slate-400">Select Puzzle:</span>
            <select
              value={activeChallenge.id}
              onChange={(e) => {
                const found = challenges.find((c) => c.id === Number(e.target.value));
                if (found) selectChallenge(found);
              }}
              className="bg-slate-950 border border-slate-800 rounded-lg px-3 py-1.5 text-xs text-slate-200 font-mono focus:outline-none focus:border-blue-500"
            >
              {challenges.map((c) => (
                <option key={c.id} value={c.id}>
                  [{c.language}] {c.title} ({c.difficulty})
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Main Debugging Workbench */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Left Column: Problem Brief & Buggy Code */}
        <div className="space-y-4">
          <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
            <div className="flex items-center justify-between pb-3 border-b border-slate-800 mb-3">
              <div>
                <span className="text-[10px] font-mono uppercase tracking-wider text-amber-400">
                  {activeChallenge.language} • {activeChallenge.difficulty} Tier
                </span>
                <h3 className="text-sm font-bold text-white mt-0.5">{activeChallenge.title}</h3>
              </div>
              <div className="flex items-center gap-1 text-xs font-bold text-amber-400 font-mono">
                <Zap className="w-3.5 h-3.5 fill-amber-400" />
                <span>+{activeChallenge.xpReward} XP</span>
              </div>
            </div>

            <p className="text-xs text-slate-300 leading-relaxed">{activeChallenge.description}</p>

            <div className="mt-4 p-3 rounded-lg bg-slate-950 border border-slate-800 text-[11px] font-mono text-slate-400">
              <span className="text-slate-400 block mb-1">Test Case Context:</span>
              <span className="text-slate-200">{activeChallenge.testCaseDescription}</span>
            </div>

            {/* Progressive Hints Section */}
            <div className="mt-4 pt-3 border-t border-slate-800">
              <span className="text-xs font-semibold text-slate-400 block mb-2">Pedagogical Hints:</span>
              <div className="space-y-2">
                {hints.map((hint, idx) => {
                  const isRevealed = revealedHints.includes(idx);
                  return (
                    <div key={idx} className="p-2.5 rounded-lg bg-slate-950 border border-slate-800/80 text-xs">
                      {isRevealed ? (
                        <span className="text-slate-300">💡 Hint {idx + 1}: {hint}</span>
                      ) : (
                        <button
                          type="button"
                          onClick={() => handleRevealHint(idx)}
                          className="text-blue-400 hover:text-blue-300 font-medium flex items-center gap-1.5"
                        >
                          <HelpCircle className="w-3.5 h-3.5" />
                          <span>Reveal Hint #{idx + 1}</span>
                        </button>
                      )}
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        </div>

        {/* Right Column: Code Editor & Fix Submission */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 flex flex-col justify-between shadow-sm">
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">
                1. Identify & Explain the Root Cause:
              </label>
              <textarea
                required
                rows={3}
                value={userExplanation}
                onChange={(e) => setUserExplanation(e.target.value)}
                placeholder="Explain why this bug occurs in execution (e.g. In Python, mutable default arguments are evaluated once at definition time, creating a shared singleton across calls)."
                className="w-full p-2.5 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 placeholder-slate-400 focus:outline-none focus:border-blue-500"
              />
            </div>

            <div>
              <div className="flex items-center justify-between mb-1">
                <label className="block text-xs font-semibold text-slate-300">
                  2. Submit Corrected Code Fix ({activeChallenge.language}):
                </label>
                <button
                  type="button"
                  onClick={() => setUserCode(activeChallenge.buggyCode)}
                  className="text-[10px] text-slate-400 hover:text-slate-200"
                >
                  Reset Code
                </button>
              </div>
              <textarea
                required
                rows={9}
                value={userCode}
                onChange={(e) => setUserCode(e.target.value)}
                className="w-full p-3 bg-slate-950 border border-slate-800 rounded-lg font-mono text-xs text-slate-200 leading-relaxed focus:outline-none focus:border-blue-500 resize-none"
              />
            </div>

            <button
              type="submit"
              disabled={submitting || !userExplanation.trim()}
              className="w-full py-2.5 px-4 rounded-lg bg-blue-600 hover:bg-blue-500 text-white font-medium text-xs transition-colors flex items-center justify-center gap-2 shadow-sm disabled:opacity-50"
            >
              <span>{submitting ? 'Evaluating Diagnosis...' : 'Submit Diagnostic Fix'}</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </button>
          </form>

          {/* Feedback & Result Alert */}
          {response && (
            <div className={`mt-4 p-4 rounded-xl border text-xs ${
              response.isSuccessful
                ? 'bg-emerald-500/10 border-emerald-500/30 text-emerald-300'
                : 'bg-rose-500/10 border-rose-500/30 text-rose-300'
            }`}>
              <div className="flex items-center gap-2 font-bold mb-1">
                {response.isSuccessful ? (
                  <>
                    <CheckCircle2 className="w-4 h-4 text-emerald-400" />
                    <span>Defect Successfully Resolved (+{response.xpAwarded} XP)</span>
                  </>
                ) : (
                  <>
                    <AlertCircle className="w-4 h-4 text-rose-400" />
                    <span>Fix Incomplete (Attempt #{response.attemptNumber})</span>
                  </>
                )}
              </div>
              <p className="leading-relaxed mt-1 text-slate-300">{response.feedback}</p>

              {/* Revealed Solution Reference on Success */}
              {response.idealSolutionCode && (
                <div className="mt-3 pt-2 border-t border-emerald-500/20">
                  <span className="text-[11px] font-semibold text-emerald-400 block mb-1">
                    Canonical Clean Solution:
                  </span>
                  <pre className="p-2.5 rounded bg-slate-950 text-[11px] font-mono text-slate-300 overflow-x-auto">
                    <code>{response.idealSolutionCode}</code>
                  </pre>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

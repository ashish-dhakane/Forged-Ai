'use client';

// Skill Gap Analysis dashboard displaying identified deficits, gap severity, and concrete remediation steps.
import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import {
  GitCompare,
  AlertTriangle,
  ArrowRight,
  RefreshCw,
  CheckCircle2,
  Sparkles,
  ShieldAlert,
} from 'lucide-react';
import { getSkillGaps, recalculateSkillGaps } from '../../../services/skillGapService';
import { SkillGap } from '../../../types';
import DemoBadge from '../../../components/layout/DemoBadge';

export default function SkillGapsPage() {
  const [gaps, setGaps] = useState<SkillGap[]>([]);
  const [loading, setLoading] = useState(true);
  const [recalculating, setRecalculating] = useState(false);

  // Fetches identified skill gaps on page load.
  useEffect(() => {
    async function loadData() {
      try {
        const data = await getSkillGaps();
        setGaps(data);
      } catch (err) {
        console.error('Failed to load skill gaps:', err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, []);

  // Re-evaluates gaps using latest backend algorithms.
  const handleRecalculate = async () => {
    setRecalculating(true);
    try {
      const updated = await recalculateSkillGaps();
      setGaps(updated);
    } catch (err) {
      console.error('Recalculation error:', err);
    } finally {
      setRecalculating(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-20">
        <div className="w-6 h-6 border-2 border-blue-500 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  const highGaps = gaps.filter((g) => g.gapSeverity === 'HIGH');
  const mediumGaps = gaps.filter((g) => g.gapSeverity === 'MEDIUM');
  const lowGaps = gaps.filter((g) => g.gapSeverity === 'LOW');

  return (
    <div className="space-y-8 max-w-5xl">
      {/* Skill Gap Header Card */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h2 className="text-base font-semibold text-white">Skill Gap Detection Engine</h2>
              <DemoBadge />
            </div>
            <p className="text-xs text-slate-400 mt-1">
              Cross-analyzes GitHub commits, assessment scores, and completed missions against hiring benchmarks.
            </p>
          </div>

          <button
            onClick={handleRecalculate}
            disabled={recalculating}
            className="px-3.5 py-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-200 text-xs font-medium transition-colors flex items-center gap-1.5 border border-slate-700 disabled:opacity-50"
          >
            <RefreshCw className={`w-3.5 h-3.5 ${recalculating ? 'animate-spin' : ''}`} />
            <span>{recalculating ? 'Recalculating...' : 'Recalculate Gaps'}</span>
          </button>
        </div>

        {/* Severity Summary Pills */}
        <div className="mt-5 grid grid-cols-3 gap-3 border-t border-slate-800 pt-4">
          <div className="p-3 rounded-lg bg-rose-500/10 border border-rose-500/20 text-center">
            <div className="text-lg font-bold text-rose-400">{highGaps.length}</div>
            <div className="text-[11px] text-slate-400 uppercase font-mono">High Severity Gaps</div>
          </div>
          <div className="p-3 rounded-lg bg-amber-500/10 border border-amber-500/20 text-center">
            <div className="text-lg font-bold text-amber-400">{mediumGaps.length}</div>
            <div className="text-[11px] text-slate-400 uppercase font-mono">Moderate Gaps</div>
          </div>
          <div className="p-3 rounded-lg bg-emerald-500/10 border border-emerald-500/20 text-center">
            <div className="text-lg font-bold text-emerald-400">{lowGaps.length}</div>
            <div className="text-[11px] text-slate-400 uppercase font-mono">Strong / Minor Gaps</div>
          </div>
        </div>
      </div>

      {/* Detailed Skill Gap Cards */}
      <div className="space-y-4">
        {gaps.map((gap) => {
          const isHigh = gap.gapSeverity === 'HIGH';
          const isMedium = gap.gapSeverity === 'MEDIUM';

          const badgeClasses = isHigh
            ? 'bg-rose-500/10 text-rose-400 border-rose-500/30'
            : isMedium
            ? 'bg-amber-500/10 text-amber-400 border-amber-500/30'
            : 'bg-emerald-500/10 text-emerald-400 border-emerald-500/30';

          return (
            <div
              key={gap.id}
              className="bg-slate-900 border border-slate-800 rounded-xl p-6 hover:border-slate-700 transition-colors"
            >
              <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 pb-3 border-b border-slate-800">
                <div>
                  <span className="text-[10px] font-mono uppercase tracking-wider text-slate-400">
                    Category: {gap.category}
                  </span>
                  <h3 className="text-sm font-bold text-white mt-0.5">{gap.skillName}</h3>
                </div>

                <div className="flex items-center gap-3">
                  <span className={`px-2.5 py-0.5 rounded text-xs font-semibold border ${badgeClasses}`}>
                    {gap.gapSeverity} GAP
                  </span>
                </div>
              </div>

              {/* Current vs Target Comparison */}
              <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div className="p-3 rounded-lg bg-slate-950 border border-slate-800 flex items-center justify-between">
                  <span className="text-xs text-slate-400">Current Level:</span>
                  <span className="text-xs font-semibold text-slate-200">{gap.currentLevel}</span>
                </div>
                <div className="p-3 rounded-lg bg-slate-950 border border-slate-800 flex items-center justify-between">
                  <span className="text-xs text-slate-400">Target Industry Level:</span>
                  <span className="text-xs font-semibold text-emerald-400">{gap.targetLevel}</span>
                </div>
              </div>

              {/* Recommended Action Card */}
              <div className="mt-4 p-3.5 rounded-lg bg-slate-950/70 border border-slate-800/80">
                <div className="flex items-center gap-1.5 text-xs font-semibold text-blue-400 mb-1">
                  <Sparkles className="w-3.5 h-3.5" />
                  <span>Recommended Pedagogical Action</span>
                </div>
                <p className="text-xs text-slate-300 leading-relaxed">{gap.recommendedAction}</p>
              </div>

              <div className="mt-4 flex items-center justify-end gap-3 text-xs">
                <Link
                  href="/roadmap"
                  className="text-blue-400 hover:text-blue-300 font-medium flex items-center gap-1"
                >
                  <span>View in Roadmap</span>
                  <ArrowRight className="w-3 h-3" />
                </Link>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

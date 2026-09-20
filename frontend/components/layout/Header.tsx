'use client';

// Top application header presenting user streak, level badges, and viva demo mode indicator.
import React from 'react';
import { Flame, ShieldCheck, Github } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import DemoBadge from './DemoBadge';

interface HeaderProps {
  title?: string;
  subtitle?: string;
}

export default function Header({ title, subtitle }: HeaderProps) {
  const { user } = useAuth();

  return (
    <header className="h-16 border-b border-slate-800 bg-slate-900/60 backdrop-blur px-8 flex items-center justify-between sticky top-0 z-10">
      <div>
        <h1 className="text-base font-semibold text-white tracking-tight">{title || 'ForgeAI Platform'}</h1>
        {subtitle && <p className="text-xs text-slate-400">{subtitle}</p>}
      </div>

      <div className="flex items-center gap-4">
        <DemoBadge />

        {/* Current streak pill */}
        <div className="flex items-center gap-1.5 px-3 py-1 rounded-full bg-slate-800/80 border border-slate-700 text-xs font-medium text-amber-300">
          <Flame className="w-3.5 h-3.5 text-amber-400 fill-amber-400" />
          <span>{user?.streak || 7} Day Streak</span>
        </div>

        {/* GitHub connection pill */}
        <div className="flex items-center gap-1.5 px-3 py-1 rounded-full bg-slate-800/80 border border-slate-700 text-xs font-medium text-slate-300">
          <Github className="w-3.5 h-3.5 text-slate-400" />
          <span>@{user?.githubUsername || 'demo-engineer'}</span>
        </div>

        {/* Verified student profile badge */}
        <div className="flex items-center gap-1 text-emerald-400 text-xs font-medium">
          <ShieldCheck className="w-4 h-4 text-emerald-400" />
          <span className="hidden sm:inline">Industry-Ready Candidate</span>
        </div>
      </div>
    </header>
  );
}

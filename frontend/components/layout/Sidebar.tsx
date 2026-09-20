'use client';

// Sidebar navigation component providing developer-focused links to all 12 platform modules.
import React from 'react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import {
  LayoutDashboard,
  User,
  GitBranch,
  FileCheck2,
  GitCompare,
  Milestone,
  CheckCircle2,
  Code2,
  Bug,
  Mic,
  LineChart,
  Settings,
  LogOut,
  Zap,
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

interface NavItem {
  name: string;
  href: string;
  icon: React.ElementType;
}

const navItems: NavItem[] = [
  { name: 'Dashboard', href: '/dashboard', icon: LayoutDashboard },
  { name: 'Engineering Profile', href: '/profile', icon: User },
  { name: 'GitHub Intelligence', href: '/github', icon: GitBranch },
  { name: 'Skill Assessment', href: '/assessment', icon: FileCheck2 },
  { name: 'Skill Gaps', href: '/skill-gaps', icon: GitCompare },
  { name: 'Roadmap', href: '/roadmap', icon: Milestone },
  { name: 'Missions', href: '/missions', icon: CheckCircle2 },
  { name: 'Code Review', href: '/code-review', icon: Code2 },
  { name: 'Debugging', href: '/debugging', icon: Bug },
  { name: 'AI Interview', href: '/interview', icon: Mic },
  { name: 'Analytics', href: '/analytics', icon: LineChart },
  { name: 'Settings', href: '/settings', icon: Settings },
];

export default function Sidebar() {
  const pathname = usePathname();
  const { user, logout } = useAuth();

  return (
    <aside className="w-64 bg-slate-900 text-slate-300 border-r border-slate-800 flex flex-col h-screen sticky top-0">
      {/* Brand logo & platform header */}
      <div className="p-5 border-b border-slate-800 flex items-center justify-between">
        <Link href="/dashboard" className="flex items-center gap-2.5">
          <div className="w-8 h-8 rounded bg-gradient-to-tr from-blue-600 to-indigo-500 flex items-center justify-center text-white font-bold text-base shadow-sm">
            F
          </div>
          <div>
            <span className="text-lg font-bold text-white tracking-tight">ForgeAI</span>
            <span className="text-[10px] block text-slate-400 -mt-1 font-mono uppercase">Growth Platform</span>
          </div>
        </Link>
      </div>

      {/* Main navigation list */}
      <nav className="flex-1 overflow-y-auto px-3 py-4 space-y-1 scrollbar-thin scrollbar-thumb-slate-800">
        <div className="text-[11px] font-semibold text-slate-400 uppercase tracking-wider px-3 mb-2">
          Engineering Platform
        </div>
        {navItems.map((item) => {
          const isActive = pathname === item.href;
          const Icon = item.icon;
          return (
            <Link
              key={item.name}
              href={item.href}
              className={`flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
                isActive
                  ? 'bg-blue-600/15 text-blue-400 border border-blue-500/30'
                  : 'text-slate-400 hover:text-slate-100 hover:bg-slate-800/60'
              }`}
            >
              <Icon className={`w-4 h-4 ${isActive ? 'text-blue-400' : 'text-slate-400'}`} />
              <span>{item.name}</span>
            </Link>
          );
        })}
      </nav>

      {/* User info card & logout button */}
      <div className="p-4 border-t border-slate-800 bg-slate-950/50">
        <div className="flex items-center justify-between mb-3">
          <div className="flex items-center gap-2.5 overflow-hidden">
            <div className="w-8 h-8 rounded-full bg-slate-800 border border-slate-700 flex items-center justify-center text-slate-300 font-semibold text-xs shrink-0">
              {user?.name ? user.name.charAt(0).toUpperCase() : 'E'}
            </div>
            <div className="truncate">
              <div className="text-xs font-semibold text-white truncate">{user?.name || 'Demo Engineer'}</div>
              <div className="text-[11px] text-slate-400 flex items-center gap-1">
                <Zap className="w-3 h-3 text-amber-400 fill-amber-400" />
                <span>Level {user?.level || 1} • {user?.xp || 0} XP</span>
              </div>
            </div>
          </div>
          <button
            onClick={logout}
            title="Log Out"
            className="p-1.5 text-slate-400 hover:text-rose-400 hover:bg-slate-800 rounded transition-colors"
          >
            <LogOut className="w-4 h-4" />
          </button>
        </div>
        <div className="text-[10px] text-slate-400 text-center font-mono">
          ForgeAI Core v1.0.0 • Java + Next.js
        </div>
      </div>
    </aside>
  );
}

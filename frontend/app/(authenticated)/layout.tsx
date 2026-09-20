'use client';

// Authenticated layout wrapping all engineering workspace modules with the sidebar and top header.
import React, { useEffect } from 'react';
import { useRouter, usePathname } from 'next/navigation';
import Sidebar from '../../components/layout/Sidebar';
import Header from '../../components/layout/Header';
import { useAuth } from '../../context/AuthContext';

const routeTitles: Record<string, { title: string; subtitle: string }> = {
  '/dashboard': { title: 'Engineering Dashboard', subtitle: 'Real-time engineering score, readiness index, and focus area' },
  '/profile': { title: 'Engineering Profile', subtitle: 'Technical portfolio, skills matrix, and 6-dimension Engineer DNA' },
  '/github': { title: 'GitHub Intelligence', subtitle: 'Repository health audits, language distribution, and commit insights' },
  '/assessment': { title: 'Skill Assessments', subtitle: 'Interactive technical evaluations benchmarking industry readiness' },
  '/skill-gaps': { title: 'Skill Gap Analysis', subtitle: 'Identified competency deficits and prioritized growth recommendations' },
  '/roadmap': { title: 'Personalized Roadmap', subtitle: 'Adaptive 5-phase engineering curriculum tailored to your gaps' },
  '/missions': { title: 'Engineering Missions', subtitle: 'Hands-on practical challenges designed to build production competence' },
  '/code-review': { title: 'AI Code Review', subtitle: 'Automated static analysis, security checks, and refactoring advice' },
  '/debugging': { title: 'Debugging Engine', subtitle: 'Diagnose runtime defects, explain root causes, and submit code fixes' },
  '/interview': { title: 'AI Technical Interview', subtitle: 'Simulate role-specific technical rounds and receive scoring reports' },
  '/analytics': { title: 'Progress Analytics', subtitle: 'Longitudinal score trajectory, XP velocity, and consistency trends' },
  '/settings': { title: 'Platform Settings', subtitle: 'Developer configurations, API credentials, and demonstration controls' },
};

export default function AuthenticatedLayout({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, isLoading } = useAuth();
  const router = useRouter();
  const pathname = usePathname();

  // Protects routes from unauthenticated direct browser access.
  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      router.push('/login');
    }
  }, [isLoading, isAuthenticated, router]);

  if (isLoading) {
    return (
      <div className="min-h-screen bg-slate-950 flex items-center justify-center">
        <div className="flex flex-col items-center gap-3">
          <div className="w-8 h-8 rounded-full border-2 border-blue-500 border-t-transparent animate-spin" />
          <span className="text-xs text-slate-400 font-mono">Loading ForgeAI Workspace...</span>
        </div>
      </div>
    );
  }

  const currentInfo = routeTitles[pathname] || { title: 'ForgeAI Workspace', subtitle: 'Developer Growth Platform' };

  return (
    <div className="flex min-h-screen bg-slate-950 text-slate-100">
      <Sidebar />
      <div className="flex-1 flex flex-col min-w-0 overflow-x-hidden">
        <Header title={currentInfo.title} subtitle={currentInfo.subtitle} />
        <main className="flex-1 p-8 overflow-y-auto">
          {children}
        </main>
      </div>
    </div>
  );
}

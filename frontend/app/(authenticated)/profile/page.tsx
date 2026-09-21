'use client';

// Engineering Profile page displaying developer identity, portfolio projects, and the 6-dimension Engineer DNA.
import React, { useEffect, useState } from 'react';
import {
  User,
  Github,
  Mail,
  GraduationCap,
  Briefcase,
  Sparkles,
  Layers,
  Code,
  ExternalLink,
} from 'lucide-react';
import {
  ResponsiveContainer,
  RadarChart,
  PolarGrid,
  PolarAngleAxis,
  PolarRadiusAxis,
  Radar,
} from 'recharts';
import { getProfileData, getEngineerDna, ProfileData, DnaResponse } from '../../../services/profileService';
import DemoBadge from '../../../components/layout/DemoBadge';

export default function ProfilePage() {
  const [profile, setProfile] = useState<ProfileData | null>(null);
  const [dna, setDna] = useState<DnaResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [mounted, setMounted] = useState(false);

  // Fetches student profile and 6-dimension Engineer DNA from backend APIs.
  useEffect(() => {
    setMounted(true);
    async function loadData() {
      try {
        const [profileRes, dnaRes] = await Promise.all([getProfileData(), getEngineerDna()]);
        setProfile(profileRes);
        setDna(dnaRes);
      } catch (err) {
        console.error('Failed to load profile details:', err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, []);

  if (loading || !profile) {
    return (
      <div className="flex items-center justify-center py-20">
        <div className="w-6 h-6 border-2 border-blue-500 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  return (
    <div className="space-y-8 max-w-6xl">
      {/* Profile Header Card */}
      <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 sm:p-8 relative overflow-hidden shadow-sm">
        <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-6">
          <div className="flex items-center gap-5">
            <div className="w-16 h-16 rounded-2xl bg-gradient-to-tr from-blue-600 to-indigo-500 flex items-center justify-center text-white font-bold text-2xl shadow-md shrink-0">
              {profile.name.charAt(0)}
            </div>
            <div>
              <div className="flex items-center gap-2.5">
                <h1 className="text-xl font-bold text-white tracking-tight">{profile.name}</h1>
                {profile.email === 'demo@forgeai.dev' && <DemoBadge />}
              </div>
              <p className="text-xs text-slate-400 mt-1 max-w-xl">{profile.bio}</p>
              <div className="mt-3 flex flex-wrap items-center gap-4 text-xs text-slate-400">
                <span className="flex items-center gap-1.5">
                  <Mail className="w-3.5 h-3.5 text-slate-400" />
                  {profile.email}
                </span>
                <span className="flex items-center gap-1.5">
                  <Github className="w-3.5 h-3.5 text-slate-400" />
                  @{profile.githubUsername || 'demo-engineer'}
                </span>
                <span className="flex items-center gap-1.5 text-blue-400 font-medium">
                  <GraduationCap className="w-3.5 h-3.5" />
                  {profile.education}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Engineer DNA Section (Radar + AI Insight) */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h2 className="text-base font-semibold text-white">Engineer DNA</h2>
            <p className="text-xs text-slate-400">
              Holistic characterization across Builder, Debugger, Problem Solver, Architect, Security, and Communicator.
            </p>
          </div>
          <span className="px-2.5 py-1 rounded text-[11px] font-medium bg-blue-500/10 text-blue-400 border border-blue-500/25">
            6 Dimensions
          </span>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 items-center">
          {/* Radar Chart */}
          <div className="h-72 w-full">
            {mounted && dna && (
              <ResponsiveContainer width="100%" height="100%">
                <RadarChart data={dna.dimensions}>
                  <PolarGrid stroke="#334155" />
                  <PolarAngleAxis dataKey="dimension" tick={{ fill: '#94a3b8', fontSize: 11 }} />
                  <PolarRadiusAxis angle={30} domain={[0, 100]} tick={{ fill: '#64748b', fontSize: 10 }} />
                  <Radar name="Engineer DNA" dataKey="score" stroke="#818cf8" fill="#6366f1" fillOpacity={0.45} />
                </RadarChart>
              </ResponsiveContainer>
            )}
          </div>

          {/* AI Qualitative Synthesis Card */}
          <div className="space-y-4">
            <div className="p-5 rounded-xl bg-slate-950 border border-slate-800">
              <div className="flex items-center gap-2 text-indigo-400 text-xs font-semibold uppercase tracking-wider mb-2">
                <Sparkles className="w-4 h-4" />
                <span>AI Engineering DNA Analysis</span>
                <span className="text-[10px] px-2 py-0.5 rounded bg-indigo-500/10 border border-indigo-500/20 text-indigo-300">
                  AI-Generated
                </span>
              </div>
              <p className="text-xs text-slate-300 leading-relaxed">
                {dna?.aiInsight ||
                  'Your profile shows strong Builder and Problem Solver tendencies, while Testing and System Design are priority areas for improvement to reach senior industry benchmarks.'}
              </p>
            </div>

            <div className="grid grid-cols-2 sm:grid-cols-3 gap-2.5 text-center">
              {dna?.dimensions.map((dim) => (
                <div key={dim.dimension} className="p-3 rounded-lg bg-slate-950/70 border border-slate-800">
                  <div className="text-[11px] font-medium text-slate-400">{dim.dimension}</div>
                  <div className="text-base font-bold text-white mt-0.5">{dim.score}/100</div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* Skills Matrix & Languages Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Core Competencies & Skills */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
          <h3 className="text-sm font-semibold text-white mb-3">Core Engineering Skills</h3>
          <div className="flex flex-wrap gap-2">
            {profile.skills.map((skill) => (
              <span
                key={skill}
                className="px-3 py-1 rounded-lg text-xs font-medium bg-slate-950 border border-slate-800 text-slate-300"
              >
                {skill}
              </span>
            ))}
          </div>

          <h3 className="text-sm font-semibold text-white mt-6 mb-3">Frameworks & Technologies</h3>
          <div className="flex flex-wrap gap-2">
            {profile.technologies.map((tech) => (
              <span
                key={tech}
                className="px-3 py-1 rounded-lg text-xs font-medium bg-slate-950 border border-blue-900/30 text-blue-300"
              >
                {tech}
              </span>
            ))}
          </div>
        </div>

        {/* Primary Languages Breakdown */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
          <h3 className="text-sm font-semibold text-white mb-4">Programming Languages</h3>
          <div className="space-y-4">
            {profile.languages.map((lang) => (
              <div key={lang.name}>
                <div className="flex justify-between text-xs mb-1">
                  <span className="font-medium text-slate-300">{lang.name}</span>
                  <span className="font-mono text-slate-400">{lang.percentage}%</span>
                </div>
                <div className="h-2 w-full bg-slate-800 rounded-full overflow-hidden">
                  <div
                    className="h-full bg-blue-500 rounded-full"
                    style={{ width: `${lang.percentage}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Portfolio Projects Section */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex items-center justify-between mb-4">
          <div>
            <h3 className="text-sm font-semibold text-white">Portfolio Engineering Projects</h3>
            <p className="text-xs text-slate-400">Verified codebases analyzed by ForgeAI</p>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {profile.projects.map((project) => (
            <div key={project.id} className="p-4 rounded-xl bg-slate-950 border border-slate-800 flex flex-col justify-between">
              <div>
                <div className="flex items-center justify-between">
                  <h4 className="text-sm font-semibold text-white">{project.title}</h4>
                  <span className="text-[10px] px-2 py-0.5 rounded bg-slate-800 text-slate-400 font-mono">
                    {project.complexityLevel}
                  </span>
                </div>
                <p className="text-xs text-slate-400 mt-2 leading-relaxed">{project.description}</p>
              </div>

              <div className="mt-4 pt-3 border-t border-slate-900 flex items-center justify-between text-xs">
                <span className="text-[11px] text-slate-400 font-mono">{project.techStack}</span>
                {project.githubUrl && (
                  <a
                    href={project.githubUrl}
                    target="_blank"
                    rel="noreferrer"
                    className="text-blue-400 hover:text-blue-300 flex items-center gap-1 font-medium"
                  >
                    <span>Code</span>
                    <ExternalLink className="w-3 h-3" />
                  </a>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

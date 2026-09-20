'use client';

// Minimal and professional public landing page showcasing ForgeAI's engineering growth loop.
import React from 'react';
import Link from 'next/link';
import {
  ArrowRight,
  GitBranch,
  Milestone,
  Code2,
  Bug,
  Mic,
  LineChart,
  ShieldCheck,
  CheckCircle2,
  Cpu,
  Layers,
  Sparkles,
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function LandingPage() {
  const { loginDemo, isLoading } = useAuth();

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col">
      {/* Top public navbar */}
      <header className="border-b border-slate-800/80 bg-slate-950/80 backdrop-blur sticky top-0 z-50">
        <div className="max-w-6xl mx-auto px-6 h-16 flex items-center justify-between">
          <Link href="/" className="flex items-center gap-2.5">
            <div className="w-8 h-8 rounded bg-gradient-to-tr from-blue-600 to-indigo-500 flex items-center justify-center text-white font-bold text-base shadow-sm">
              F
            </div>
            <div>
              <span className="text-lg font-bold text-white tracking-tight">ForgeAI</span>
              <span className="text-[10px] block text-slate-400 -mt-1 font-mono uppercase">Engineering Platform</span>
            </div>
          </Link>

          <div className="flex items-center gap-3">
            <button
              onClick={() => loginDemo()}
              disabled={isLoading}
              className="text-xs px-3 py-1.5 rounded-lg border border-amber-500/30 bg-amber-500/10 text-amber-300 hover:bg-amber-500/20 transition-colors font-medium flex items-center gap-1.5"
            >
              <Sparkles className="w-3.5 h-3.5" />
              <span>Viva 1-Click Demo</span>
            </button>
            <Link
              href="/login"
              className="text-xs font-medium text-slate-300 hover:text-white px-3 py-1.5 rounded-lg hover:bg-slate-800 transition-colors"
            >
              Sign In
            </Link>
            <Link
              href="/register"
              className="text-xs font-medium bg-blue-600 hover:bg-blue-500 text-white px-3.5 py-1.5 rounded-lg transition-colors shadow-sm"
            >
              Get Started
            </Link>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <main className="flex-1">
        <section className="py-20 px-6 max-w-5xl mx-auto text-center">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-500/10 border border-blue-500/20 text-xs font-medium text-blue-400 mb-6">
            <ShieldCheck className="w-3.5 h-3.5" />
            <span>Final-Year Computer Science Major Project • Spring Boot & Next.js</span>
          </div>

          <h1 className="text-4xl sm:text-5xl md:text-6xl font-extrabold text-white tracking-tight leading-tight">
            Build engineering skills. <br />
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-400 via-indigo-300 to-slate-200">
              Not just code.
            </span>
          </h1>

          <p className="mt-6 text-base sm:text-lg text-slate-400 max-w-2xl mx-auto leading-relaxed">
            An AI-powered engineering growth platform that analyzes your real development work,
            evaluates software architecture skills, and systematically closes gaps to make you industry-ready.
          </p>

          <div className="mt-8 flex flex-wrap justify-center items-center gap-3.5">
            <Link
              href="/register"
              className="px-5 py-2.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white font-medium text-sm transition-all shadow-sm flex items-center gap-2"
            >
              <span>Get Started</span>
              <ArrowRight className="w-4 h-4" />
            </Link>

            <button
              onClick={() => loginDemo()}
              disabled={isLoading}
              className="px-5 py-2.5 rounded-lg bg-slate-900 hover:bg-slate-800 text-slate-200 border border-slate-700 font-medium text-sm transition-colors flex items-center gap-2"
            >
              <span>Explore Platform (Demo Mode)</span>
            </button>
          </div>

          {/* Visual Engineering Growth Loop Diagram */}
          <div className="mt-16 bg-slate-900/90 border border-slate-800 rounded-2xl p-8 max-w-3xl mx-auto text-left shadow-xl">
            <div className="flex items-center justify-between border-b border-slate-800 pb-4 mb-6">
              <div className="text-xs font-semibold uppercase tracking-wider text-slate-400 font-mono">
                The ForgeAI Closed-Loop Growth Pipeline
              </div>
              <div className="text-xs text-blue-400 font-mono">Continuous Feedback Architecture</div>
            </div>

            <div className="flex flex-col md:flex-row items-center justify-between gap-4 text-center">
              {/* Stage 1 */}
              <div className="p-4 rounded-xl bg-slate-800/60 border border-slate-700/60 flex-1 w-full">
                <div className="w-7 h-7 mx-auto mb-2 rounded bg-blue-500/20 text-blue-400 flex items-center justify-center font-bold text-xs">
                  1
                </div>
                <div className="text-xs font-semibold text-white">COLLECT DATA</div>
                <div className="text-[11px] text-slate-400 mt-1">GitHub • Projects • Quizzes • Code</div>
              </div>

              <div className="text-slate-500 font-bold hidden md:block">→</div>
              <div className="text-slate-500 font-bold md:hidden">↓</div>

              {/* Stage 2 */}
              <div className="p-4 rounded-xl bg-slate-800/60 border border-slate-700/60 flex-1 w-full">
                <div className="w-7 h-7 mx-auto mb-2 rounded bg-indigo-500/20 text-indigo-400 flex items-center justify-center font-bold text-xs">
                  2
                </div>
                <div className="text-xs font-semibold text-white">ANALYZE</div>
                <div className="text-[11px] text-slate-400 mt-1">Deterministic Scoring • DNA Radar</div>
              </div>

              <div className="text-slate-500 font-bold hidden md:block">→</div>
              <div className="text-slate-500 font-bold md:hidden">↓</div>

              {/* Stage 3 */}
              <div className="p-4 rounded-xl bg-slate-800/60 border border-slate-700/60 flex-1 w-full">
                <div className="w-7 h-7 mx-auto mb-2 rounded bg-amber-500/20 text-amber-400 flex items-center justify-center font-bold text-xs">
                  3
                </div>
                <div className="text-xs font-semibold text-white">IDENTIFY GAPS</div>
                <div className="text-[11px] text-slate-400 mt-1">Skill Gap Severity • Focus Areas</div>
              </div>

              <div className="text-slate-500 font-bold hidden md:block">→</div>
              <div className="text-slate-500 font-bold md:hidden">↓</div>

              {/* Stage 4 */}
              <div className="p-4 rounded-xl bg-slate-800/60 border border-emerald-700/40 bg-emerald-950/20 flex-1 w-full">
                <div className="w-7 h-7 mx-auto mb-2 rounded bg-emerald-500/20 text-emerald-400 flex items-center justify-center font-bold text-xs">
                  4
                </div>
                <div className="text-xs font-semibold text-emerald-300">PRACTICE & GROW</div>
                <div className="text-[11px] text-slate-400 mt-1">Missions • Debugging • Interviews</div>
              </div>
            </div>
          </div>
        </section>

        {/* Major Feature Highlights Grid */}
        <section className="py-16 px-6 max-w-6xl mx-auto border-t border-slate-800/80">
          <div className="text-center mb-12">
            <h2 className="text-2xl sm:text-3xl font-bold text-white tracking-tight">
              A Complete Suite for Modern Developer Mastery
            </h2>
            <p className="text-slate-400 text-sm mt-2">
              Designed as a professional engineering platform, not a generic trivia quiz.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {/* Feature 1 */}
            <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
              <div className="p-2.5 w-fit rounded-lg bg-blue-500/10 text-blue-400 border border-blue-500/20 mb-4">
                <GitBranch className="w-5 h-5" />
              </div>
              <h3 className="text-base font-semibold text-white">GitHub Intelligence</h3>
              <p className="text-xs text-slate-400 mt-2 leading-relaxed">
                Connects to real GitHub repositories, profiles language distribution, calculates health scores, and highlights automated architectural strengths and improvements.
              </p>
            </div>

            {/* Feature 2 */}
            <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
              <div className="p-2.5 w-fit rounded-lg bg-indigo-500/10 text-indigo-400 border border-indigo-500/20 mb-4">
                <Milestone className="w-5 h-5" />
              </div>
              <h3 className="text-base font-semibold text-white">Personalized Roadmaps</h3>
              <p className="text-xs text-slate-400 mt-2 leading-relaxed">
                Dynamically generated 5-phase engineering curriculum that adapts in real-time based on detected skill gaps across testing, debugging, and system design.
              </p>
            </div>

            {/* Feature 3 */}
            <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
              <div className="p-2.5 w-fit rounded-lg bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 mb-4">
                <Code2 className="w-5 h-5" />
              </div>
              <h3 className="text-base font-semibold text-white">AI Code Review Engine</h3>
              <p className="text-xs text-slate-400 mt-2 leading-relaxed">
                Deep multi-language code analysis across Java, Python, TypeScript, Go, C++, and SQL, identifying security anti-patterns, complexity hotspots, and refactorings.
              </p>
            </div>

            {/* Feature 4 */}
            <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
              <div className="p-2.5 w-fit rounded-lg bg-amber-500/10 text-amber-400 border border-amber-500/20 mb-4">
                <Bug className="w-5 h-5" />
              </div>
              <h3 className="text-base font-semibold text-white">Debugging Engine</h3>
              <p className="text-xs text-slate-400 mt-2 leading-relaxed">
                Hands-on challenges isolating race conditions, memory leaks, and off-by-one errors. Evaluates both the code fix and the student's root-cause explanation.
              </p>
            </div>

            {/* Feature 5 */}
            <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
              <div className="p-2.5 w-fit rounded-lg bg-rose-500/10 text-rose-400 border border-rose-500/20 mb-4">
                <Mic className="w-5 h-5" />
              </div>
              <h3 className="text-base font-semibold text-white">AI Technical Interview</h3>
              <p className="text-xs text-slate-400 mt-2 leading-relaxed">
                Role-specific technical hiring simulations across Backend, Frontend, and Full Stack, evaluating technical correctness, problem-solving structure, and communication.
              </p>
            </div>

            {/* Feature 6 */}
            <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
              <div className="p-2.5 w-fit rounded-lg bg-cyan-500/10 text-cyan-400 border border-cyan-500/20 mb-4">
                <LineChart className="w-5 h-5" />
              </div>
              <h3 className="text-base font-semibold text-white">Engineering Analytics</h3>
              <p className="text-xs text-slate-400 mt-2 leading-relaxed">
                Transparent 10-dimension deterministic score calculation: Programming (20%), Problem Solving (15%), Projects (15%), GitHub (10%), Debugging (10%), Testing (8%), and System Design (8%).
              </p>
            </div>
          </div>
        </section>
      </main>

      {/* Public Footer */}
      <footer className="border-t border-slate-800 py-8 px-6 text-center text-xs text-slate-400 font-mono">
        ForgeAI • Final-Year Computer Science Project • Java 21 Spring Boot + Next.js 14
      </footer>
    </div>
  );
}

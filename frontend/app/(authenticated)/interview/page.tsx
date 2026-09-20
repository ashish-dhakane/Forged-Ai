'use client';

// AI Technical Interview simulation page conducting role-specific rounds and producing hiring scorecards.
import React, { useState } from 'react';
import {
  Mic,
  Sparkles,
  Award,
  ArrowRight,
  CheckCircle2,
  AlertTriangle,
  Play,
  RotateCcw,
  MessageSquare,
} from 'lucide-react';
import {
  startInterview,
  submitInterviewAnswer,
  evaluateInterview,
} from '../../../services/interviewService';
import { Interview, InterviewEvaluation } from '../../../types';
import DemoBadge from '../../../components/layout/DemoBadge';

const roles = [
  'Software Engineer',
  'Backend Developer',
  'Frontend Developer',
  'Full Stack Developer',
];

const difficulties = ['Beginner', 'Intermediate', 'Advanced'];

const categories = [
  'Full Stack & System Architecture',
  'Data Structures & Algorithms',
  'Database Management & Locking',
  'Operating Systems & Concurrency',
  'Computer Networks & Protocols',
  'Software Engineering Principles',
  'Behavioral & Cultural Readiness',
];

export default function InterviewPage() {
  const [role, setRole] = useState('Backend Developer');
  const [difficulty, setDifficulty] = useState('Intermediate');
  const [category, setCategory] = useState('Full Stack & System Architecture');

  const [interview, setInterview] = useState<Interview | null>(null);
  const [currentQIndex, setCurrentQIndex] = useState(0);
  const [currentAnswer, setCurrentAnswer] = useState('');
  const [evaluation, setEvaluation] = useState<InterviewEvaluation | null>(null);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  // Launches a new interview simulation session.
  const handleStartInterview = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setEvaluation(null);
    setCurrentQIndex(0);
    setCurrentAnswer('');
    try {
      const newInterview = await startInterview(role, difficulty, category);
      setInterview(newInterview);
    } catch (err) {
      console.error('Failed to start interview:', err);
    } finally {
      setLoading(false);
    }
  };

  // Submits the candidate's answer for the current question and advances.
  const handleNextQuestion = async () => {
    if (!interview || !currentAnswer.trim()) return;
    setSubmitting(true);
    try {
      const q = interview.questions[currentQIndex];
      await submitInterviewAnswer(q.id, currentAnswer);

      if (currentQIndex < interview.questions.length - 1) {
        setCurrentQIndex(currentQIndex + 1);
        setCurrentAnswer('');
      } else {
        // Conclude interview and evaluate
        const report = await evaluateInterview(interview.id);
        setEvaluation(report);
      }
    } catch (err) {
      console.error('Answer submission error:', err);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="space-y-6 max-w-5xl">
      {/* Configuration Header */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h2 className="text-base font-semibold text-white">AI Technical Interview Simulation</h2>
              <DemoBadge />
            </div>
            <p className="text-xs text-slate-400 mt-1">
              Practice real-world engineering interviews tailored to your target role with instant hiring rubric feedback.
            </p>
          </div>
        </div>
      </div>

      {/* Mode 1: Configuration Form (Before Starting) */}
      {!interview && !evaluation && (
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 sm:p-8 max-w-2xl mx-auto shadow-sm">
          <h3 className="text-sm font-bold text-white mb-4">Configure Interview Round</h3>
          <form onSubmit={handleStartInterview} className="space-y-4">
            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">Target Engineering Role</label>
              <select
                value={role}
                onChange={(e) => setRole(e.target.value)}
                className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 focus:outline-none focus:border-blue-500"
              >
                {roles.map((r) => (
                  <option key={r} value={r}>{r}</option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">Difficulty Level</label>
              <div className="grid grid-cols-3 gap-2">
                {difficulties.map((diff) => (
                  <button
                    key={diff}
                    type="button"
                    onClick={() => setDifficulty(diff)}
                    className={`py-2 px-3 rounded-lg text-xs font-medium border text-center transition-colors ${
                      difficulty === diff
                        ? 'bg-blue-600/15 border-blue-500 text-blue-300'
                        : 'bg-slate-950 border-slate-800 text-slate-400 hover:border-slate-700'
                    }`}
                  >
                    {diff}
                  </button>
                ))}
              </div>
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">Technical Category</label>
              <select
                value={category}
                onChange={(e) => setCategory(e.target.value)}
                className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 focus:outline-none focus:border-blue-500"
              >
                {categories.map((c) => (
                  <option key={c} value={c}>{c}</option>
                ))}
              </select>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full mt-4 py-2.5 px-4 rounded-lg bg-blue-600 hover:bg-blue-500 text-white font-medium text-xs transition-colors flex items-center justify-center gap-2 shadow-sm disabled:opacity-50"
            >
              <Play className="w-3.5 h-3.5" />
              <span>{loading ? 'Initializing Interview Room...' : 'Start Technical Round'}</span>
            </button>
          </form>
        </div>
      )}

      {/* Mode 2: Live Question & Answer Stage */}
      {interview && !evaluation && (
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 sm:p-8 space-y-6">
          <div className="flex items-center justify-between pb-4 border-b border-slate-800">
            <div className="flex items-center gap-2">
              <span className="text-[10px] font-mono uppercase tracking-wider text-blue-400">
                {interview.role} • {interview.difficulty} Round
              </span>
            </div>
            <span className="text-xs text-slate-400 font-mono">
              Question {currentQIndex + 1} of {interview.questions.length}
            </span>
          </div>

          {/* Question Box */}
          <div className="p-5 rounded-xl bg-slate-950 border border-slate-800">
            <div className="text-[10px] font-mono text-slate-400 uppercase mb-1">
              Domain: {interview.questions[currentQIndex]?.category}
            </div>
            <h3 className="text-sm font-semibold text-white leading-relaxed">
              {interview.questions[currentQIndex]?.questionText}
            </h3>
          </div>

          {/* Answer Box */}
          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1.5">
              Candidate Explanation & Technical Justification:
            </label>
            <textarea
              rows={6}
              required
              value={currentAnswer}
              onChange={(e) => setCurrentAnswer(e.target.value)}
              placeholder="State your technical answer clearly. Discuss trade-offs, concurrency hazards, and architectural implications..."
              className="w-full p-3.5 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 leading-relaxed focus:outline-none focus:border-blue-500"
            />
          </div>

          <div className="flex items-center justify-between pt-2">
            <button
              onClick={() => { setInterview(null); setEvaluation(null); }}
              className="text-xs text-slate-400 hover:text-white"
            >
              Abandon Round
            </button>
            <button
              onClick={handleNextQuestion}
              disabled={submitting || !currentAnswer.trim()}
              className="px-5 py-2 rounded-lg bg-blue-600 hover:bg-blue-500 text-white font-medium text-xs transition-colors flex items-center gap-2 shadow-sm disabled:opacity-50"
            >
              <span>
                {currentQIndex < interview.questions.length - 1
                  ? 'Submit & Next Question'
                  : 'Submit & Conclude Interview'}
              </span>
              <ArrowRight className="w-3.5 h-3.5" />
            </button>
          </div>
        </div>
      )}

      {/* Mode 3: Final Hiring Rubric Scorecard */}
      {evaluation && (
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-8 space-y-6 animate-in fade-in">
          <div className="flex items-center justify-between pb-4 border-b border-slate-800">
            <div>
              <span className="text-[10px] font-mono uppercase tracking-wider text-slate-400">
                Evaluation Report
              </span>
              <h3 className="text-lg font-bold text-white mt-0.5">Interview Performance Report</h3>
            </div>
            <span className="px-3 py-1 rounded text-xs font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/30">
              {evaluation.readinessVerdict}
            </span>
          </div>

          {/* Scores Breakdown */}
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 text-center">
            <div className="p-4 rounded-xl bg-slate-950 border border-slate-800">
              <span className="text-[10px] font-mono text-slate-400 uppercase">Overall</span>
              <div className="text-2xl font-bold font-mono text-blue-400 mt-0.5">{evaluation.overallScore}%</div>
            </div>
            <div className="p-4 rounded-xl bg-slate-950 border border-slate-800">
              <span className="text-[10px] font-mono text-slate-400 uppercase">Technical Depth</span>
              <div className="text-2xl font-bold font-mono text-emerald-400 mt-0.5">{evaluation.technicalScore}%</div>
            </div>
            <div className="p-4 rounded-xl bg-slate-950 border border-slate-800">
              <span className="text-[10px] font-mono text-slate-400 uppercase">Communication</span>
              <div className="text-2xl font-bold font-mono text-indigo-400 mt-0.5">{evaluation.communicationScore}%</div>
            </div>
            <div className="p-4 rounded-xl bg-slate-950 border border-slate-800">
              <span className="text-[10px] font-mono text-slate-400 uppercase">Problem Solving</span>
              <div className="text-2xl font-bold font-mono text-amber-400 mt-0.5">{evaluation.problemSolvingScore}%</div>
            </div>
          </div>

          {/* Summary */}
          <div className="p-4 rounded-xl bg-slate-950 border border-slate-800 text-xs text-slate-300 leading-relaxed">
            <span className="text-slate-400 font-semibold block mb-1">Evaluator Summary:</span>
            {evaluation.summary}
          </div>

          {/* Strengths & Weaknesses Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="p-4 rounded-xl bg-slate-950 border border-slate-800">
              <span className="text-xs font-semibold text-emerald-400 flex items-center gap-1.5 mb-2">
                <CheckCircle2 className="w-3.5 h-3.5" />
                Demonstrated Strengths
              </span>
              <ul className="space-y-1.5 text-xs text-slate-300">
                {evaluation.strengths.map((s, idx) => (
                  <li key={idx} className="flex items-start gap-1.5">
                    <span className="text-emerald-500">•</span>
                    <span>{s}</span>
                  </li>
                ))}
              </ul>
            </div>

            <div className="p-4 rounded-xl bg-slate-950 border border-slate-800">
              <span className="text-xs font-semibold text-amber-400 flex items-center gap-1.5 mb-2">
                <AlertTriangle className="w-3.5 h-3.5" />
                Improvement Suggestions
              </span>
              <ul className="space-y-1.5 text-xs text-slate-300">
                {evaluation.improvementSuggestions.map((s, idx) => (
                  <li key={idx} className="flex items-start gap-1.5">
                    <span className="text-amber-500">•</span>
                    <span>{s}</span>
                  </li>
                ))}
              </ul>
            </div>
          </div>

          <div className="pt-2 flex justify-center">
            <button
              onClick={() => { setInterview(null); setEvaluation(null); }}
              className="px-5 py-2 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-200 text-xs font-medium transition-colors"
            >
              Start Another Interview
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

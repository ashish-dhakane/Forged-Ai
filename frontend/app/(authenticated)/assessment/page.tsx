'use client';

// Skill Assessment interface allowing candidates to take technical evaluations and review score breakdowns.
import React, { useEffect, useState } from 'react';
import {
  FileCheck2,
  CheckCircle2,
  XCircle,
  Award,
  ArrowRight,
  Clock,
  Sparkles,
  HelpCircle,
  RotateCcw,
} from 'lucide-react';
import {
  getAllAssessments,
  getAssessmentById,
  submitAssessmentAnswers,
  getUserAssessmentResults,
} from '../../../services/assessmentService';
import { Assessment, AssessmentResult, Question } from '../../../types';
import ProgressBar from '../../../components/ui/ProgressBar';

export default function AssessmentPage() {
  const [assessments, setAssessments] = useState<Assessment[]>([]);
  const [activeAssessment, setActiveAssessment] = useState<Assessment | null>(null);
  const [selectedAnswers, setSelectedAnswers] = useState<Record<number, number>>({});
  const [result, setResult] = useState<AssessmentResult | null>(null);
  const [history, setHistory] = useState<AssessmentResult[]>([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  // Loads catalog of available engineering skill assessments on mount.
  useEffect(() => {
    async function loadData() {
      try {
        const [allAssessments, pastResults] = await Promise.all([
          getAllAssessments(),
          getUserAssessmentResults(),
        ]);
        setAssessments(allAssessments);
        setHistory(pastResults);
      } catch (err) {
        console.error('Failed to load assessments:', err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, []);

  // Opens an assessment and initializes question options.
  const handleStartAssessment = async (id: number) => {
    setLoading(true);
    setResult(null);
    setSelectedAnswers({});
    try {
      const fullAssessment = await getAssessmentById(id);
      setActiveAssessment(fullAssessment);
    } catch (err) {
      console.error('Failed to load assessment questions:', err);
    } finally {
      setLoading(false);
    }
  };

  // Records user option choice for a question.
  const handleSelectOption = (questionId: number, optionIndex: number) => {
    setSelectedAnswers((prev) => ({
      ...prev,
      [questionId]: optionIndex,
    }));
  };

  // Submits the candidate's answers for evaluation by the Spring Boot backend.
  const handleSubmit = async () => {
    if (!activeAssessment) return;
    setSubmitting(true);
    try {
      const evalResult = await submitAssessmentAnswers(activeAssessment.id, selectedAnswers);
      setResult(evalResult);
      // Refresh past results list
      const pastResults = await getUserAssessmentResults();
      setHistory(pastResults);
    } catch (err) {
      console.error('Failed to submit assessment:', err);
    } finally {
      setSubmitting(false);
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
    <div className="space-y-8 max-w-5xl">
      {/* Active Assessment View */}
      {activeAssessment && !result ? (
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 sm:p-8">
          <div className="flex items-center justify-between pb-4 border-b border-slate-800 mb-6">
            <div>
              <span className="text-xs font-mono uppercase tracking-wider text-blue-400">
                {activeAssessment.category} Evaluation
              </span>
              <h2 className="text-lg font-bold text-white mt-0.5">{activeAssessment.title}</h2>
            </div>
            <button
              onClick={() => setActiveAssessment(null)}
              className="text-xs text-slate-400 hover:text-white px-3 py-1 rounded bg-slate-800"
            >
              Exit Assessment
            </button>
          </div>

          <div className="space-y-6">
            {activeAssessment.questions && activeAssessment.questions.length > 0 ? (
              activeAssessment.questions.map((q, idx) => {
                let options: string[] = [];
                try {
                  options = JSON.parse(q.optionsJson);
                } catch {
                  options = ['Option A', 'Option B', 'Option C', 'Option D'];
                }

                return (
                  <div key={q.id} className="p-5 rounded-xl bg-slate-950 border border-slate-800">
                    <div className="text-xs text-slate-400 font-mono mb-2">Question {idx + 1} of {activeAssessment.questions?.length}</div>
                    <p className="text-sm font-medium text-slate-100 leading-relaxed">{q.questionText}</p>

                    <div className="mt-4 space-y-2.5">
                      {options.map((option, optIdx) => {
                        const isSelected = selectedAnswers[q.id] === optIdx;
                        return (
                          <button
                            key={optIdx}
                            type="button"
                            onClick={() => handleSelectOption(q.id, optIdx)}
                            className={`w-full text-left p-3 rounded-lg text-xs font-medium border transition-all flex items-center justify-between ${
                              isSelected
                                ? 'bg-blue-600/15 border-blue-500 text-blue-200'
                                : 'bg-slate-900/80 border-slate-800 text-slate-300 hover:border-slate-700'
                            }`}
                          >
                            <span>{option}</span>
                            <div className={`w-4 h-4 rounded-full border flex items-center justify-center ${isSelected ? 'border-blue-500 bg-blue-500' : 'border-slate-700'}`}>
                              {isSelected && <div className="w-1.5 h-1.5 rounded-full bg-white" />}
                            </div>
                          </button>
                        );
                      })}
                    </div>
                  </div>
                );
              })
            ) : (
              <div className="text-xs text-slate-400 py-6 text-center">No questions cataloged for this test.</div>
            )}
          </div>

          <div className="mt-8 pt-4 border-t border-slate-800 flex items-center justify-between">
            <span className="text-xs text-slate-400">
              {Object.keys(selectedAnswers).length} of {activeAssessment.questions?.length || 0} answered
            </span>
            <button
              onClick={handleSubmit}
              disabled={submitting || Object.keys(selectedAnswers).length === 0}
              className="px-5 py-2 rounded-lg bg-blue-600 hover:bg-blue-500 text-white font-medium text-xs transition-colors flex items-center gap-2 shadow-sm disabled:opacity-50"
            >
              <span>{submitting ? 'Evaluating...' : 'Submit Assessment'}</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </button>
          </div>
        </div>
      ) : null}

      {/* Assessment Evaluation Outcome Card */}
      {result && (
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-8 text-center animate-in fade-in duration-300">
          <div className="w-12 h-12 mx-auto rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 flex items-center justify-center mb-3">
            <Award className="w-6 h-6" />
          </div>

          <span className="text-xs font-mono uppercase tracking-wider text-slate-400">Assessment Evaluated</span>
          <h2 className="text-2xl font-bold text-white mt-1">{result.assessmentTitle}</h2>

          <div className="mt-6 max-w-sm mx-auto p-4 rounded-xl bg-slate-950 border border-slate-800">
            <div className="text-3xl font-extrabold text-blue-400 font-mono">{result.scorePercentage}%</div>
            <div className="text-xs text-slate-400 mt-1">
              {result.correctCount} of {result.totalQuestions} Questions Answered Correctly
            </div>
            <div className="mt-3">
              <span className={`inline-block px-3 py-0.5 rounded text-xs font-semibold ${
                result.performanceTier === 'Strong'
                  ? 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/30'
                  : result.performanceTier === 'Average'
                  ? 'bg-amber-500/15 text-amber-400 border border-amber-500/30'
                  : 'bg-rose-500/15 text-rose-400 border border-rose-500/30'
              }`}>
                Performance Tier: {result.performanceTier}
              </span>
            </div>
          </div>

          <p className="text-xs text-slate-300 max-w-md mx-auto mt-4 leading-relaxed">
            Your score has been dynamically integrated into your Engineering Score and Skill Gap analysis.
          </p>

          <div className="mt-6 flex justify-center gap-3">
            <button
              onClick={() => { setActiveAssessment(null); setResult(null); }}
              className="px-4 py-2 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-200 text-xs font-medium transition-colors"
            >
              Return to Catalog
            </button>
          </div>
        </div>
      )}

      {/* Catalog of Assessments */}
      {!activeAssessment && !result && (
        <div className="space-y-6">
          <div>
            <h2 className="text-base font-semibold text-white">Engineering Assessment Catalog</h2>
            <p className="text-xs text-slate-400">
              Verified evaluations measuring algorithmic problem solving, testing, system architecture, and defensive engineering.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {assessments.map((a) => (
              <div
                key={a.id}
                className="bg-slate-900 border border-slate-800 rounded-xl p-5 flex flex-col justify-between hover:border-slate-700 transition-colors"
              >
                <div>
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-[10px] font-semibold px-2 py-0.5 rounded bg-blue-500/10 text-blue-400 border border-blue-500/20 font-mono uppercase">
                      {a.category}
                    </span>
                    <span className="text-[11px] text-slate-400 font-mono">{a.difficulty}</span>
                  </div>
                  <h3 className="text-sm font-bold text-white mt-1">{a.title}</h3>
                  <p className="text-xs text-slate-400 mt-2 leading-relaxed">{a.description}</p>
                </div>

                <div className="mt-5 pt-3 border-t border-slate-800 flex items-center justify-between text-xs">
                  <span className="text-slate-400 flex items-center gap-1">
                    <Clock className="w-3.5 h-3.5" />
                    {a.durationMinutes} mins
                  </span>
                  <button
                    onClick={() => handleStartAssessment(a.id)}
                    className="text-xs font-semibold text-blue-400 hover:text-blue-300 flex items-center gap-1"
                  >
                    <span>Start Test</span>
                    <ArrowRight className="w-3 h-3" />
                  </button>
                </div>
              </div>
            ))}
          </div>

          {/* Previous Submissions History */}
          {history.length > 0 && (
            <div className="mt-10 bg-slate-900 border border-slate-800 rounded-xl p-6">
              <h3 className="text-sm font-semibold text-white mb-3">Completed Assessment History</h3>
              <div className="divide-y divide-slate-800">
                {history.map((h) => (
                  <div key={h.id} className="py-3 flex items-center justify-between text-xs">
                    <div>
                      <span className="font-semibold text-white">{h.assessmentTitle || h.category}</span>
                      <span className="text-slate-400 ml-2">({h.correctCount}/{h.totalQuestions} correct)</span>
                    </div>
                    <div className="flex items-center gap-3">
                      <span className="font-mono font-bold text-blue-400">{h.scorePercentage}%</span>
                      <span className={`px-2 py-0.5 rounded text-[10px] font-semibold ${
                        h.performanceTier === 'Strong' ? 'text-emerald-400 bg-emerald-500/10' : 'text-amber-400 bg-amber-500/10'
                      }`}>
                        {h.performanceTier}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

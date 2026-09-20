'use client';

// AI Code Review workbench providing automated static analysis, security audits, and educational refactoring.
import React, { useState } from 'react';
import {
  Code2,
  Sparkles,
  ShieldAlert,
  Zap,
  CheckCircle2,
  AlertCircle,
  Copy,
  Check,
  FileCode,
} from 'lucide-react';
import { analyzeCode } from '../../../services/codeReviewService';
import { CodeReviewFeedback } from '../../../types';

const supportedLanguages = [
  'Java',
  'Python',
  'TypeScript',
  'JavaScript',
  'Go',
  'Rust',
  'C++',
  'C',
  'C#',
  'Kotlin',
  'PHP',
  'SQL',
];

const sampleSnippets: Record<string, string> = {
  Java: `public class UserService {\n    // Potential SQL injection and missing null check\n    public User findUser(String username) {\n        String query = "SELECT * FROM users WHERE name = '" + username + "'";\n        return executeRawQuery(query);\n    }\n}`,
  Python: `def fetch_user_orders(user_id, cache={}):\n    # Bug: Mutable default dictionary argument and missing error handling\n    if user_id in cache:\n        return cache[user_id]\n    orders = db.query(f"SELECT * FROM orders WHERE user_id = {user_id}")\n    cache[user_id] = orders\n    return orders`,
  TypeScript: `export async function transferFunds(fromId: string, toId: string, amount: number) {\n  // Missing transaction boundary and input validation\n  const balance = await getBalance(fromId);\n  if (balance >= amount) {\n    await deductBalance(fromId, amount);\n    await addBalance(toId, amount);\n  }\n}`,
  Go: `func ProcessTasks(tasks []string) {\n    // Concurrency bug: loop variable capture in goroutines\n    for _, task := range tasks {\n        go func() {\n            execute(task)\n        }()\n    }\n}`,
};

export default function CodeReviewPage() {
  const [language, setLanguage] = useState('Java');
  const [code, setCode] = useState(sampleSnippets['Java']);
  const [feedback, setFeedback] = useState<CodeReviewFeedback | null>(null);
  const [loading, setLoading] = useState(false);
  const [copied, setCopied] = useState(false);

  // Sends the student's code to the backend AI code-review engine.
  const handleAnalyze = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!code.trim()) return;
    setLoading(true);
    try {
      const result = await analyzeCode(code, language);
      setFeedback(result);
    } catch (err) {
      console.error('Code review analysis error:', err);
    } finally {
      setLoading(false);
    }
  };

  // Loads a preset sample snippet when switching languages.
  const handleLanguageChange = (newLang: string) => {
    setLanguage(newLang);
    if (sampleSnippets[newLang]) {
      setCode(sampleSnippets[newLang]);
    }
  };

  // Copies refactored code example to clipboard.
  const handleCopyCode = () => {
    if (feedback?.refactoredCode) {
      navigator.clipboard.writeText(feedback.refactoredCode);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }
  };

  return (
    <div className="space-y-6 max-w-6xl">
      {/* Overview header */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h2 className="text-base font-semibold text-white">AI Engineering Code Review</h2>
              <span className="text-[10px] px-2 py-0.5 rounded bg-indigo-500/10 text-indigo-300 border border-indigo-500/20 font-mono">
                Multi-Language AST & Heuristic
              </span>
            </div>
            <p className="text-xs text-slate-400 mt-1">
              Submit your code for instant architectural, security, complexity, and idiomatic refactoring evaluation.
            </p>
          </div>

          <div className="flex items-center gap-2">
            <span className="text-xs text-slate-400">Language:</span>
            <select
              value={language}
              onChange={(e) => handleLanguageChange(e.target.value)}
              className="bg-slate-950 border border-slate-800 rounded-lg px-3 py-1.5 text-xs text-slate-200 font-mono focus:outline-none focus:border-blue-500"
            >
              {supportedLanguages.map((lang) => (
                <option key={lang} value={lang}>
                  {lang}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Main 2-Column Split: Code Input (Left) & AI Review (Right) */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Left Column: Code Editor */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 flex flex-col justify-between shadow-sm">
          <div>
            <div className="flex items-center justify-between pb-3 border-b border-slate-800 mb-3">
              <div className="flex items-center gap-2 text-xs font-semibold text-slate-300">
                <FileCode className="w-4 h-4 text-blue-400" />
                <span>Source Code Buffer ({language})</span>
              </div>
              <button
                type="button"
                onClick={() => setCode(sampleSnippets[language] || '')}
                className="text-[11px] text-blue-400 hover:text-blue-300"
              >
                Load Sample Snippet
              </button>
            </div>

            <textarea
              rows={16}
              value={code}
              onChange={(e) => setCode(e.target.value)}
              placeholder="Paste your source code here..."
              className="w-full p-3.5 bg-slate-950 border border-slate-800 rounded-lg font-mono text-xs text-slate-200 leading-relaxed focus:outline-none focus:border-blue-500 resize-none"
            />
          </div>

          <div className="mt-4 pt-3 border-t border-slate-800 flex items-center justify-between">
            <span className="text-[11px] text-slate-400 font-mono">
              Lines: {code.split('\n').length} • Characters: {code.length}
            </span>
            <button
              onClick={handleAnalyze}
              disabled={loading || !code.trim()}
              className="px-5 py-2 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs font-medium transition-colors flex items-center gap-2 shadow-sm disabled:opacity-50"
            >
              <Sparkles className="w-3.5 h-3.5" />
              <span>{loading ? 'Analyzing Code...' : 'Analyze with AI'}</span>
            </button>
          </div>
        </div>

        {/* Right Column: AI Structured Feedback */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 flex flex-col justify-between shadow-sm">
          {feedback ? (
            <div className="space-y-4">
              <div className="flex items-center justify-between pb-3 border-b border-slate-800">
                <div>
                  <span className="text-[10px] font-mono uppercase text-indigo-400">Review Summary</span>
                  <div className="text-xs text-slate-300 mt-0.5 leading-relaxed">{feedback.summary}</div>
                </div>
                <div className="text-right shrink-0 ml-4">
                  <div className="text-2xl font-bold font-mono text-emerald-400">
                    {feedback.qualityScore}/100
                  </div>
                  <span className="text-[10px] text-slate-400 uppercase font-mono">Quality Score</span>
                </div>
              </div>

              {/* Security & Issues */}
              <div className="space-y-3">
                {feedback.securityConcerns && feedback.securityConcerns.length > 0 && (
                  <div className="p-3 rounded-lg bg-rose-500/10 border border-rose-500/20">
                    <span className="text-xs font-semibold text-rose-400 flex items-center gap-1.5 mb-1.5">
                      <ShieldAlert className="w-3.5 h-3.5" />
                      Security Audit
                    </span>
                    <ul className="space-y-1 text-xs text-slate-300">
                      {feedback.securityConcerns.map((sec, idx) => (
                        <li key={idx} className="flex items-start gap-1.5">
                          <span className="text-rose-400 font-bold">•</span>
                          <span>{sec}</span>
                        </li>
                      ))}
                    </ul>
                  </div>
                )}

                {feedback.issues && feedback.issues.length > 0 && (
                  <div className="p-3 rounded-lg bg-amber-500/10 border border-amber-500/20">
                    <span className="text-xs font-semibold text-amber-400 flex items-center gap-1.5 mb-1.5">
                      <AlertCircle className="w-3.5 h-3.5" />
                      Potential Issues & Bugs
                    </span>
                    <ul className="space-y-1 text-xs text-slate-300">
                      {feedback.issues.map((iss, idx) => (
                        <li key={idx} className="flex items-start gap-1.5">
                          <span className="text-amber-400 font-bold">•</span>
                          <span>{iss}</span>
                        </li>
                      ))}
                    </ul>
                  </div>
                )}
              </div>

              {/* Suggested Improved Code */}
              {feedback.refactoredCode && (
                <div className="mt-4 pt-3 border-t border-slate-800">
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-xs font-semibold text-slate-300 flex items-center gap-1.5">
                      <Code2 className="w-3.5 h-3.5 text-emerald-400" />
                      Suggested Idiomatic Refactoring
                    </span>
                    <button
                      onClick={handleCopyCode}
                      className="text-[11px] text-slate-400 hover:text-white flex items-center gap-1"
                    >
                      {copied ? <Check className="w-3 h-3 text-emerald-400" /> : <Copy className="w-3 h-3" />}
                      <span>{copied ? 'Copied' : 'Copy'}</span>
                    </button>
                  </div>

                  <pre className="p-3.5 rounded-lg bg-slate-950 border border-slate-800 font-mono text-[11px] text-slate-300 overflow-x-auto max-h-48 leading-relaxed scrollbar-thin">
                    <code>{feedback.refactoredCode}</code>
                  </pre>
                </div>
              )}
            </div>
          ) : (
            <div className="h-full flex flex-col items-center justify-center py-20 text-center text-slate-400">
              <Code2 className="w-10 h-10 text-slate-700 mb-3" />
              <p className="text-xs max-w-xs">
                Paste your code on the left and click "Analyze with AI" to inspect quality scores, security vulnerabilities, and clean refactoring patterns.
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

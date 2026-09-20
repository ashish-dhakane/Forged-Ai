'use client';

// Platform Settings page managing developer identity, API integration keys, and Demo Mode toggles.
import React, { useEffect, useState } from 'react';
import {
  Settings as SettingsIcon,
  User,
  Github,
  Key,
  ShieldCheck,
  Save,
  CheckCircle2,
  Info,
  Layers,
} from 'lucide-react';
import { apiRequest } from '../../../services/api';
import DemoBadge from '../../../components/layout/DemoBadge';

export default function SettingsPage() {
  const [name, setName] = useState('');
  const [bio, setBio] = useState('');
  const [githubUsername, setGithubUsername] = useState('');
  const [githubToken, setGithubToken] = useState('');
  const [aiApiKey, setAiApiKey] = useState('');
  const [demoMode, setDemoMode] = useState(true);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState(false);

  // Fetches current user settings and integration status on mount.
  useEffect(() => {
    async function loadSettings() {
      try {
        const data = await apiRequest<any>('/api/settings');
        setName(data.name || '');
        setBio(data.bio || '');
        setGithubUsername(data.githubUsername || '');
        setDemoMode(data.demoModeActive || false);
      } catch (err) {
        console.error('Failed to load settings:', err);
      } finally {
        setLoading(false);
      }
    }
    loadSettings();
  }, []);

  // Saves updated profile and developer configuration to backend.
  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setSuccess(false);
    try {
      await apiRequest('/api/settings', {
        method: 'PUT',
        body: JSON.stringify({
          name,
          bio,
          githubUsername,
          githubToken,
          aiApiKey,
          demoMode,
        }),
      });
      setSuccess(true);
      setTimeout(() => setSuccess(false), 2500);
    } catch (err) {
      console.error('Failed to update settings:', err);
    } finally {
      setSaving(false);
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
    <div className="space-y-8 max-w-4xl">
      {/* Settings Header */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
        <div className="flex items-center justify-between">
          <div>
            <div className="flex items-center gap-2">
              <h2 className="text-base font-semibold text-white">Platform Settings & Integrations</h2>
              <DemoBadge />
            </div>
            <p className="text-xs text-slate-400 mt-1">
              Configure student profile metadata, GitHub access tokens, and toggle between Live and Demo modes.
            </p>
          </div>
        </div>
      </div>

      <form onSubmit={handleSave} className="space-y-6">
        {/* Profile Card */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 space-y-4">
          <h3 className="text-sm font-semibold text-white flex items-center gap-2">
            <User className="w-4 h-4 text-blue-400" />
            <span>Developer Identity</span>
          </h3>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">Display Name</label>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="w-full px-3 py-2 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 focus:outline-none focus:border-blue-500"
              />
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">GitHub Handle</label>
              <input
                type="text"
                value={githubUsername}
                onChange={(e) => setGithubUsername(e.target.value)}
                className="w-full px-3 py-2 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 focus:outline-none focus:border-blue-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">Professional Bio / Specialty</label>
            <textarea
              rows={2}
              value={bio}
              onChange={(e) => setBio(e.target.value)}
              className="w-full px-3 py-2 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 focus:outline-none focus:border-blue-500"
            />
          </div>
        </div>

        {/* Integration Credentials Card */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6 space-y-4">
          <h3 className="text-sm font-semibold text-white flex items-center gap-2">
            <Key className="w-4 h-4 text-amber-400" />
            <span>External API Integrations (Optional)</span>
          </h3>
          <p className="text-xs text-slate-400">
            ForgeAI runs fully offline with built-in heuristic AST reviews and sample data. Add keys only for live external calls.
          </p>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">GitHub Personal Access Token</label>
              <input
                type="password"
                value={githubToken}
                onChange={(e) => setGithubToken(e.target.value)}
                placeholder="ghp_••••••••••••••••"
                className="w-full px-3 py-2 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 placeholder-slate-400 focus:outline-none focus:border-blue-500"
              />
              <span className="text-[10px] text-slate-400 mt-1 block">Increases rate limit from 60 to 5000 requests/hr.</span>
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">AI Service API Key</label>
              <input
                type="password"
                value={aiApiKey}
                onChange={(e) => setAiApiKey(e.target.value)}
                placeholder="sk-••••••••••••••••"
                className="w-full px-3 py-2 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 placeholder-slate-400 focus:outline-none focus:border-blue-500"
              />
              <span className="text-[10px] text-slate-400 mt-1 block">OpenAI / Gemini compatible. (Never exposed to browser)</span>
            </div>
          </div>
        </div>

        {/* Viva Demonstration Mode Card */}
        <div className="bg-slate-900 border border-slate-800 rounded-xl p-6">
          <div className="flex items-start justify-between">
            <div>
              <span className="text-xs font-semibold text-white flex items-center gap-1.5">
                <Info className="w-4 h-4 text-amber-400" />
                <span>University Viva Demonstration Mode</span>
              </span>
              <p className="text-xs text-slate-400 mt-1 max-w-xl">
                When enabled, pre-seeded sample data for all 12 modules is active so you can deliver a reliable, uninterrupted presentation even without active internet or third-party API credentials.
              </p>
            </div>

            <div className="flex items-center gap-2">
              <span className="text-xs font-medium text-slate-300 font-mono">
                {demoMode ? 'ENABLED' : 'DISABLED'}
              </span>
              <button
                type="button"
                onClick={() => setDemoMode(!demoMode)}
                className={`w-11 h-6 flex items-center rounded-full p-1 transition-colors ${
                  demoMode ? 'bg-blue-600' : 'bg-slate-800'
                }`}
              >
                <div
                  className={`bg-white w-4 h-4 rounded-full shadow-md transform transition-transform ${
                    demoMode ? 'translate-x-5' : 'translate-x-0'
                  }`}
                />
              </button>
            </div>
          </div>
        </div>

        {/* Save Button */}
        <div className="flex items-center justify-between pt-2">
          {success && (
            <span className="text-xs text-emerald-400 flex items-center gap-1.5 font-medium">
              <CheckCircle2 className="w-4 h-4" />
              <span>Settings updated successfully!</span>
            </span>
          )}
          {!success && <div />}

          <button
            type="submit"
            disabled={saving}
            className="px-5 py-2 rounded-lg bg-blue-600 hover:bg-blue-500 text-white font-medium text-xs transition-colors flex items-center gap-2 shadow-sm disabled:opacity-50"
          >
            <Save className="w-3.5 h-3.5" />
            <span>{saving ? 'Saving...' : 'Save Settings'}</span>
          </button>
        </div>
      </form>
    </div>
  );
}

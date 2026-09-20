// Linear progress indicator component for skill mastery, roadmap milestones, and mission completion.
import React from 'react';

interface ProgressBarProps {
  percentage: number;
  label?: string;
  valueText?: string;
  color?: 'blue' | 'emerald' | 'amber' | 'indigo';
  size?: 'sm' | 'md';
}

export default function ProgressBar({
  percentage,
  label,
  valueText,
  color = 'blue',
  size = 'md',
}: ProgressBarProps) {
  const clamped = Math.min(100, Math.max(0, percentage));

  const barColor = {
    blue: 'bg-blue-500',
    emerald: 'bg-emerald-500',
    amber: 'bg-amber-500',
    indigo: 'bg-indigo-500',
  }[color];

  const heightClass = size === 'sm' ? 'h-1.5' : 'h-2.5';

  return (
    <div className="w-full">
      {(label || valueText) && (
        <div className="flex justify-between items-center text-xs mb-1.5 font-medium">
          {label && <span className="text-slate-300">{label}</span>}
          {valueText ? (
            <span className="text-slate-400 font-mono">{valueText}</span>
          ) : (
            <span className="text-slate-400 font-mono">{clamped}%</span>
          )}
        </div>
      )}
      <div className={`w-full bg-slate-800 rounded-full overflow-hidden ${heightClass}`}>
        <div
          className={`${heightClass} rounded-full ${barColor} transition-all duration-500 ease-out`}
          style={{ width: `${clamped}%` }}
        />
      </div>
    </div>
  );
}

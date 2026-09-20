// Badge clearly indicating that displayed metrics originate from sample engineering data.
import React from 'react';
import { Info } from 'lucide-react';

export default function DemoBadge({ className = '' }: { className?: string }) {
  return (
    <span
      title="This metric is calculated from realistic sample engineering portfolio data for university viva demonstration."
      className={`inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-medium bg-amber-500/10 text-amber-400 border border-amber-500/25 ${className}`}
    >
      <Info className="w-3 h-3" />
      <span>DEMO DATA</span>
    </span>
  );
}

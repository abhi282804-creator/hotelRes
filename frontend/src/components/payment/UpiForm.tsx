import React from 'react';
import { Smartphone } from 'lucide-react';

interface UpiFormProps {
  upiId: string;
  onUpiIdChange: (value: string) => void;
}

export const UpiForm: React.FC<UpiFormProps> = ({ upiId, onUpiIdChange }) => {
  return (
    <div className="bg-slate-50 p-5 rounded-2xl border border-slate-200 space-y-3">
      <label htmlFor="upiId" className="text-xs font-bold text-slate-600 uppercase tracking-wider block flex items-center gap-1.5">
        <Smartphone className="w-4 h-4 text-accent" />
        Enter UPI ID
      </label>

      <input
        type="text"
        id="upiId"
        required
        placeholder="username@upi or mobile@okaxis"
        value={upiId}
        onChange={(e) => onUpiIdChange(e.target.value)}
        className="w-full bg-white border border-slate-200 text-brand-900 text-sm font-medium rounded-xl p-3 focus:ring-2 focus:ring-accent/20 focus:border-accent outline-none"
      />
      <p className="text-[11px] text-slate-500">
        A payment request will be sent to your UPI application for authorization.
      </p>
    </div>
  );
};

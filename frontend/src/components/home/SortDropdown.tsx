import React from 'react';
import { ArrowUpDown } from 'lucide-react';

interface SortDropdownProps {
  sortBy: 'price' | 'rating';
  onSortChange: (sort: 'price' | 'rating') => void;
}

export const SortDropdown: React.FC<SortDropdownProps> = ({
  sortBy,
  onSortChange,
}) => {
  return (
    <div className="bg-white p-4 rounded-2xl border border-slate-200/80 shadow-sm flex flex-col justify-between">
      <label htmlFor="sortSelect" className="text-xs font-bold text-slate-500 uppercase tracking-wider mb-2 flex items-center gap-1.5">
        <ArrowUpDown className="w-3.5 h-3.5 text-accent" />
        Sort By
      </label>

      <select
        id="sortSelect"
        value={sortBy}
        onChange={(e) => onSortChange(e.target.value as 'price' | 'rating')}
        className="w-full bg-slate-50 border border-slate-200 text-brand-900 text-sm font-medium rounded-xl p-2.5 focus:ring-2 focus:ring-accent/20 focus:border-accent outline-none cursor-pointer"
      >
        <option value="price">Lowest Price First</option>
        <option value="rating">Highest Rated First</option>
      </select>
    </div>
  );
};

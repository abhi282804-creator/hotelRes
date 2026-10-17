import React from 'react';
import { MapPin } from 'lucide-react';

interface LocationFilterProps {
  locations: string[];
  selectedLocation: string;
  onSelectLocation: (location: string) => void;
}

export const LocationFilter: React.FC<LocationFilterProps> = ({
  locations,
  selectedLocation,
  onSelectLocation,
}) => {
  return (
    <div className="flex items-center gap-2 overflow-x-auto pb-2 scrollbar-none">
      <div className="flex items-center gap-1.5 px-3 py-2 text-xs font-bold uppercase tracking-wider text-slate-500 bg-slate-100 rounded-lg shrink-0">
        <MapPin className="w-3.5 h-3.5 text-accent" />
        <span>Destination:</span>
      </div>
      {locations.map((loc) => {
        const isAll = loc === 'all';
        const label = isAll ? 'All Cities' : loc;
        const isSelected = selectedLocation.toLowerCase() === loc.toLowerCase();
        return (
          <button
            key={loc}
            type="button"
            onClick={() => onSelectLocation(loc)}
            className={`flex items-center gap-1.5 px-3.5 py-2 rounded-xl text-xs font-semibold whitespace-nowrap transition-all duration-200 ${
              isSelected
                ? 'bg-brand-900 text-white shadow-md shadow-brand-900/25 scale-[1.02]'
                : 'bg-white text-slate-700 hover:bg-slate-100 border border-slate-200/80 shadow-sm'
            }`}
          >
            {!isAll && <MapPin className="w-3 h-3 text-accent" />}
            <span>{label}</span>
          </button>
        );
      })}
    </div>
  );
};

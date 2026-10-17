import React from 'react';
import { Sparkles, ShieldCheck } from 'lucide-react';

export const HeroBanner: React.FC = () => {
  return (
    <div className="relative overflow-hidden bg-brand-900 text-white rounded-3xl mb-12 border border-slate-800 shadow-2xl">
      {/* Background Subtle Overlay Pattern */}
      <div className="absolute inset-0 bg-gradient-to-r from-brand-950/90 via-brand-900/80 to-accent/20 z-10" />

      <div
        className="absolute inset-0 bg-cover bg-center opacity-30 mix-blend-overlay scale-105 transform transition-transform duration-1000"
        style={{ backgroundImage: `url('${import.meta.env.BASE_URL}images/hotel1.jpg')` }}
      />

      <div className="relative z-20 max-w-5xl mx-auto px-6 py-16 sm:py-24 text-center">

        {/* Badge */}
        <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-accent/20 border border-accent/40 text-accent-light text-xs font-semibold uppercase tracking-widest mb-6">
          <Sparkles className="w-3.5 h-3.5" />
          The Premier Luxury Hotel Network
        </div>

        {/* Heading */}
        <h1 className="font-serif text-4xl sm:text-6xl font-bold tracking-tight text-white mb-6 leading-tight">
          Discover Exceptional Stays & Unforgettable Retreats
        </h1>

        {/* Subtext */}
        <p className="max-w-2xl mx-auto text-base sm:text-lg text-slate-300 font-normal leading-relaxed mb-8">
          Explore curated 5-star suites, beachfront villas, and mountain lodges. Experience seamless reservations backed by real-time room availability.
        </p>

        {/* Trust Chips */}
        <div className="flex flex-wrap justify-center items-center gap-6 text-xs sm:text-sm text-slate-300 font-medium">
          <div className="flex items-center gap-2 bg-slate-800/60 px-3.5 py-1.5 rounded-lg border border-slate-700">
            <ShieldCheck className="w-4 h-4 text-teal-500" />
            Verified Real-Time Inventory
          </div>
          <div className="flex items-center gap-2 bg-slate-800/60 px-3.5 py-1.5 rounded-lg border border-slate-700">
            <Sparkles className="w-4 h-4 text-gold-500" />
            Instant Booking Confirmation
          </div>
        </div>

      </div>
    </div>
  );
};

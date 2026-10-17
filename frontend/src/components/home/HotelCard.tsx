import React from 'react';
import { Link } from 'react-router-dom';
import { Hotel } from '../../types';
import { Star, MapPin, ArrowRight } from 'lucide-react';

interface HotelCardProps {
  hotel: Hotel;
}

export const HotelCard: React.FC<HotelCardProps> = ({ hotel }) => {
  return (
    <div className="bg-white rounded-3xl overflow-hidden border border-slate-200/80 shadow-sm card-hover-effect flex flex-col justify-between group">

      {/* Image Container with Floating Badges */}
      <div className="relative h-56 overflow-hidden bg-slate-100">
        <img
          src={`${import.meta.env.BASE_URL}${hotel.images[0]}`}
          alt={hotel.name}
          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
          onError={(e) => {
            // Fallback thumbnail
            (e.target as HTMLImageElement).src = `${import.meta.env.BASE_URL}images/hotel1.jpg`;
          }}
        />
        <div className="absolute inset-0 bg-gradient-to-t from-brand-950/60 via-transparent to-transparent" />

        {/* Category Pill */}
        <div className="absolute top-3.5 left-3.5 px-3 py-1 rounded-full bg-slate-900/80 backdrop-blur-md text-[11px] font-bold text-white uppercase tracking-wider border border-white/20">
          {hotel.category}
        </div>

        {/* Rating Pill */}
        <div className="absolute top-3.5 right-3.5 flex items-center gap-1 px-2.5 py-1 rounded-full bg-white/90 backdrop-blur-md text-xs font-bold text-brand-900 shadow-md">
          <Star className="w-3.5 h-3.5 text-gold-500 fill-gold-500" />
          <span>{hotel.rating.toFixed(1)}</span>
        </div>

        {/* Floating Location Overlay */}
        <div className="absolute bottom-3.5 left-3.5 right-3.5 flex items-center text-white/90 text-xs font-medium gap-1 truncate">
          <MapPin className="w-3.5 h-3.5 text-accent-light shrink-0" />
          <span className="truncate">{hotel.location}</span>
        </div>
      </div>

      {/* Body Content */}
      <div className="p-5 flex-1 flex flex-col justify-between">
        <div>
          <h3 className="font-serif text-xl font-bold text-brand-900 mb-2 group-hover:text-accent transition-colors line-clamp-1">
            {hotel.name}
          </h3>
          <p className="text-slate-500 text-xs leading-relaxed line-clamp-2 mb-4 font-normal">
            {hotel.description}
          </p>
        </div>

        {/* Price & Action Row */}
        <div className="pt-4 border-t border-slate-100 flex items-center justify-between">
          <div>
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">Price Range</span>
            <div className="text-sm font-bold text-brand-900">
              ₹{hotel.priceMin.toLocaleString()} - ₹{hotel.priceMax.toLocaleString()}
            </div>
          </div>

          <Link
            to={`/hotels/${hotel.id}`}
            className="inline-flex items-center gap-1.5 px-4 py-2.5 rounded-xl bg-accent hover:bg-accent-hover text-white text-xs font-bold shadow-md shadow-accent/20 transition-all duration-200 group-hover:translate-x-0.5"
          >
            <span>Reserve</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </Link>
        </div>

      </div>

    </div>
  );
};

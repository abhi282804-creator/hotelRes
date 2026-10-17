import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { Hotel } from '../types';
import { hotelService } from '../services/hotelService';
import { ImageGallery } from '../components/hotel/ImageGallery';
import { AnalyticsChart } from '../components/hotel/AnalyticsChart';
import { BookingForm } from '../components/hotel/BookingForm';
import { Star, MapPin, CheckCircle, ArrowLeft, AlertCircle } from 'lucide-react';

export const HotelDetailsPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [hotel, setHotel] = useState<Hotel | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchHotelDetails() {
      if (!id) return;
      setLoading(true);
      setError(null);
      try {
        const hotelIdNum = parseInt(id, 10);
        const data = await hotelService.getHotelById(hotelIdNum);
        setHotel(data);
      } catch (err: any) {
        setError(err.message || 'Hotel details could not be loaded');
      } finally {
        setLoading(false);
      }
    }

    fetchHotelDetails();
  }, [id]);

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 animate-pulse space-y-8">
        <div className="bg-slate-200 h-10 w-1/3 rounded-xl" />
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div className="lg:col-span-2 space-y-4">
            <div className="bg-slate-200 h-[400px] rounded-3xl" />
            <div className="bg-slate-200 h-24 rounded-2xl" />
          </div>
          <div className="bg-slate-200 h-[500px] rounded-3xl" />
        </div>
      </div>
    );
  }

  if (error || !hotel) {
    return (
      <div className="max-w-xl mx-auto px-4 py-20 text-center">
        <div className="bg-red-50 border border-red-200 rounded-3xl p-12 space-y-4">
          <AlertCircle className="w-10 h-10 text-red-500 mx-auto" />
          <h3 className="font-serif text-2xl font-bold text-red-900">Hotel Not Found</h3>
          <p className="text-sm text-red-700">{error || 'The requested hotel property could not be found.'}</p>
          <Link
            to="/"
            className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl bg-accent text-white text-sm font-semibold shadow-md"
          >
            <ArrowLeft className="w-4 h-4" />
            Return to Discovery
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Back Button Link */}
      <Link
        to="/"
        className="inline-flex items-center gap-2 text-xs font-bold text-slate-500 hover:text-accent uppercase tracking-wider mb-6 transition-colors"
      >
        <ArrowLeft className="w-4 h-4" />
        Back to Hotels
      </Link>

      {/* Main Header Title & Badges */}
      <div className="mb-8 pb-6 border-b border-slate-200/80 flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-3 mb-2">
            <span className="px-3 py-1 rounded-full bg-accent/10 text-accent text-xs font-bold uppercase tracking-wider">
              {hotel.category}
            </span>
            <span className="text-xs font-mono font-medium text-slate-400">Hotel ID: #{hotel.backendHotelId}</span>
          </div>
          <h1 className="font-serif text-4xl font-bold text-brand-900">{hotel.name}</h1>
          <p className="text-sm text-slate-500 flex items-center gap-1.5 mt-2">
            <MapPin className="w-4 h-4 text-accent" />
            {hotel.location}
          </p>
        </div>

        <div className="flex items-center gap-4">
          <div className="flex items-center gap-1.5 px-4 py-2 rounded-2xl bg-white border border-slate-200 shadow-sm">
            <Star className="w-5 h-5 text-gold-500 fill-gold-500" />
            <span className="text-lg font-bold text-brand-900">{hotel.rating.toFixed(1)}</span>
            <span className="text-xs text-slate-400 font-medium">/ 5.0</span>
          </div>

          <div className="bg-slate-900 text-white px-5 py-2.5 rounded-2xl text-right">
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">Price Rate</span>
            <span className="text-base font-bold">₹{hotel.priceMin.toLocaleString()} - ₹{hotel.priceMax.toLocaleString()}</span>
          </div>
        </div>
      </div>

      {/* Grid Layout: Left Details (2 cols) & Right Booking Form (1 col) */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">

        {/* Left Column: Gallery, Specs, Overview, Analytics */}
        <div className="lg:col-span-2 space-y-10">

          {/* Gallery */}
          <ImageGallery images={hotel.images} hotelName={hotel.name} />

          {/* Description Card */}
          <div className="bg-white p-6 sm:p-8 rounded-3xl border border-slate-200/80 shadow-sm space-y-4">
            <h3 className="font-serif text-2xl font-bold text-brand-900">About the Property</h3>
            <p className="text-slate-600 text-sm leading-relaxed">{hotel.description}</p>

            <div className="pt-4 border-t border-slate-100">
              <h4 className="text-xs font-bold text-slate-400 uppercase tracking-wider mb-3">Included Amenities</h4>
              <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
                {hotel.amenities.map((amenity, idx) => (
                  <div key={idx} className="flex items-center gap-2 text-xs font-semibold text-slate-700 bg-slate-50 p-2.5 rounded-xl border border-slate-200/60">
                    <CheckCircle className="w-4 h-4 text-emerald-500 shrink-0" />
                    <span>{amenity}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>

          {/* Location Overview Section */}
          <div className="bg-slate-100/80 p-6 sm:p-8 rounded-3xl border border-slate-200 space-y-2">
            <h3 className="font-serif text-xl font-bold text-brand-900">Location Overview</h3>
            <p className="text-slate-600 text-sm leading-relaxed">{hotel.locationOverview}</p>
          </div>

          {/* Analytics Chart Component (Jan-Jul Graph) */}
          <AnalyticsChart />

        </div>

        {/* Right Column: Sticky Booking Form */}
        <div className="lg:col-span-1">
          <BookingForm hotelId={hotel.backendHotelId} hotelName={hotel.name} />
        </div>

      </div>
    </div>
  );
};

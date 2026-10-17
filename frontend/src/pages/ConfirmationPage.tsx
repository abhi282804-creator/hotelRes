import React from 'react';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { CheckCircle2, Building, Calendar, Users, BedDouble, ArrowRight, Home } from 'lucide-react';

export const ConfirmationPage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const details = location.state?.reservationDetails;

  if (!details) {
    return (
      <div className="max-w-xl mx-auto px-4 py-20 text-center space-y-4">
        <h3 className="font-serif text-2xl font-bold text-brand-900">No Reservation Selected</h3>
        <p className="text-sm text-slate-500">Please start a reservation from a hotel details page.</p>
        <Link
          to="/"
          className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl bg-accent text-white text-sm font-semibold shadow-md"
        >
          <Home className="w-4 h-4" />
          Return Home
        </Link>
      </div>
    );
  }

  const handleProceedPayment = () => {
    navigate('/payment', {
      state: {
        reservationId: details.reservationId,
        hotelName: details.hotelName,
        checkinDate: details.checkinDate,
        checkoutDate: details.checkoutDate,
        guests: details.guests,
        roomType: details.roomType,
        priceDisplay: details.priceDisplay,
      },
    });
  };

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div className="bg-white p-8 sm:p-12 rounded-3xl border border-slate-200/80 shadow-xl space-y-8">

        {/* Success Header */}
        <div className="text-center space-y-3">
          <div className="w-16 h-16 rounded-full bg-emerald-100 text-emerald-600 flex items-center justify-center mx-auto shadow-md">
            <CheckCircle2 className="w-10 h-10" />
          </div>
          <h1 className="font-serif text-3xl font-bold text-brand-900">Reservation Confirmed!</h1>
          <p className="text-sm text-slate-500">
            Thank you for choosing <strong className="text-brand-900">{details.hotelName}</strong>! Your reservation has been successfully confirmed.
          </p>
        </div>

        {/* Details Card */}
        <div className="bg-slate-50 p-6 rounded-2xl border border-slate-200 space-y-4">
          <div className="pb-3 border-b border-slate-200 flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Reservation Reference</span>
            <span className="font-mono text-lg font-bold text-accent">{details.reservationId}</span>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-xs text-slate-600">
            <div>
              <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block mb-0.5">Customer ID</span>
              <span className="font-semibold text-brand-900">{details.customerId}</span>
            </div>

            <div>
              <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block mb-0.5">Hotel ID / Room ID</span>
              <span className="font-semibold text-brand-900">Hotel #{details.hotelId} • Room #{details.roomId}</span>
            </div>

            <div className="flex items-center space-x-2">
              <Calendar className="w-4 h-4 text-accent shrink-0" />
              <div>
                <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">Check-in Date</span>
                <span className="font-semibold text-brand-900">{details.checkinDate}</span>
              </div>
            </div>

            <div className="flex items-center space-x-2">
              <Calendar className="w-4 h-4 text-accent shrink-0" />
              <div>
                <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">Check-out Date</span>
                <span className="font-semibold text-brand-900">{details.checkoutDate}</span>
              </div>
            </div>

            <div className="flex items-center space-x-2">
              <Users className="w-4 h-4 text-accent shrink-0" />
              <div>
                <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">Guests</span>
                <span className="font-semibold text-brand-900">{details.guests} Guest(s)</span>
              </div>
            </div>

            <div className="flex items-center space-x-2">
              <BedDouble className="w-4 h-4 text-accent shrink-0" />
              <div>
                <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">Room Category</span>
                <span className="font-semibold text-brand-900">{details.roomType}</span>
              </div>
            </div>
          </div>

          {details.specialRequests && (
            <div className="pt-3 border-t border-slate-200">
              <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block mb-0.5">Special Requests</span>
              <p className="text-xs font-medium text-slate-700">{details.specialRequests}</p>
            </div>
          )}
        </div>

        {/* CTA Area */}
        <div className="text-center pt-2">
          <button
            onClick={handleProceedPayment}
            className="w-full sm:w-auto px-8 py-4 rounded-xl bg-accent hover:bg-accent-hover text-white text-base font-bold shadow-lg shadow-accent/25 transition-all duration-200 inline-flex items-center justify-center gap-2 hover:scale-[1.02]"
          >
            <span>Proceed to Payment</span>
            <ArrowRight className="w-5 h-5" />
          </button>
        </div>

      </div>
    </div>
  );
};

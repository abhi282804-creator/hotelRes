import React from 'react';
import { ReservationItem } from '../../types';
import { Building, Calendar, Users, BedDouble, CheckCircle2, Trash2 } from 'lucide-react';

interface ReservationCardProps {
  reservation: ReservationItem;
  onCancelClick: (res: ReservationItem) => void;
}

export const ReservationCard: React.FC<ReservationCardProps> = ({
  reservation,
  onCancelClick,
}) => {
  return (
    <div className="bg-white p-6 rounded-3xl border border-slate-200/80 shadow-sm card-hover-effect flex flex-col justify-between space-y-5">

      <div className="flex items-start justify-between pb-4 border-b border-slate-100">
        <div className="flex items-center space-x-3">
          <div className="p-3 rounded-2xl bg-accent/10 text-accent">
            <Building className="w-6 h-6" />
          </div>
          <div>
            <h3 className="font-serif text-lg font-bold text-brand-900">{reservation.hotelName}</h3>
            <p className="text-xs text-slate-400 font-mono">ID: #{reservation.reservationId}</p>
          </div>
        </div>

        <span className="inline-flex items-center gap-1 px-3 py-1 rounded-full bg-emerald-50 text-emerald-700 border border-emerald-200 text-xs font-bold">
          <CheckCircle2 className="w-3.5 h-3.5" />
          Confirmed
        </span>
      </div>

      <div className="grid grid-cols-2 gap-4 text-xs text-slate-600">
        <div className="flex items-center space-x-2">
          <Calendar className="w-4 h-4 text-accent shrink-0" />
          <div>
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">Check-in</span>
            <span className="font-semibold text-brand-900">{reservation.checkinDate}</span>
          </div>
        </div>

        <div className="flex items-center space-x-2">
          <Calendar className="w-4 h-4 text-accent shrink-0" />
          <div>
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">Check-out</span>
            <span className="font-semibold text-brand-900">{reservation.checkoutDate}</span>
          </div>
        </div>

        <div className="flex items-center space-x-2">
          <Users className="w-4 h-4 text-accent shrink-0" />
          <div>
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">Guests</span>
            <span className="font-semibold text-brand-900">{reservation.guests} Guest(s)</span>
          </div>
        </div>

        <div className="flex items-center space-x-2">
          <BedDouble className="w-4 h-4 text-accent shrink-0" />
          <div>
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">Room Type</span>
            <span className="font-semibold text-brand-900">{reservation.roomType}</span>
          </div>
        </div>
      </div>

      <div className="pt-3 flex justify-end">
        <button
          onClick={() => onCancelClick(reservation)}
          className="inline-flex items-center gap-1.5 px-4 py-2 rounded-xl bg-red-50 hover:bg-red-100 text-red-600 border border-red-200 text-xs font-bold transition-all"
        >
          <Trash2 className="w-3.5 h-3.5" />
          Cancel Reservation
        </button>
      </div>

    </div>
  );
};

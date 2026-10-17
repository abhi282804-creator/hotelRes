import React from 'react';
import { ReservationItem } from '../../types';
import { AlertTriangle, X } from 'lucide-react';

interface CancelModalProps {
  reservation: ReservationItem | null;
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  cancelling: boolean;
}

export const CancelModal: React.FC<CancelModalProps> = ({
  reservation,
  isOpen,
  onClose,
  onConfirm,
  cancelling,
}) => {
  if (!isOpen || !reservation) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-brand-950/70 backdrop-blur-sm animate-fade-in">
      <div className="bg-white rounded-3xl max-w-md w-full p-6 sm:p-8 shadow-2xl border border-slate-200 relative">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 p-2 text-slate-400 hover:text-slate-600 rounded-lg"
        >
          <X className="w-5 h-5" />
        </button>

        <div className="text-center space-y-3 mb-6">
          <div className="w-14 h-14 rounded-2xl bg-amber-100 text-amber-600 flex items-center justify-center mx-auto">
            <AlertTriangle className="w-7 h-7" />
          </div>
          <h3 className="font-serif text-2xl font-bold text-brand-900">Cancel Reservation?</h3>
          <p className="text-xs text-slate-500">
            Are you sure you want to cancel the following reservation? This action will remove the stay from your active bookings.
          </p>
        </div>

        <div className="bg-slate-50 p-4 rounded-2xl border border-slate-200 text-xs space-y-2 mb-6">
          <p><strong className="text-slate-700">Reservation ID:</strong> #{reservation.reservationId}</p>
          <p><strong className="text-slate-700">Hotel:</strong> {reservation.hotelName}</p>
          <p><strong className="text-slate-700">Dates:</strong> {reservation.checkinDate} to {reservation.checkoutDate}</p>
          <p><strong className="text-slate-700">Guests:</strong> {reservation.guests} Guest(s)</p>
          <p><strong className="text-slate-700">Room Type:</strong> {reservation.roomType}</p>
        </div>

        <div className="grid grid-cols-2 gap-3">
          <button
            onClick={onClose}
            className="py-3 rounded-xl border border-slate-200 text-slate-700 text-sm font-bold hover:bg-slate-50 transition-colors"
          >
            No, Go Back
          </button>

          <button
            onClick={onConfirm}
            disabled={cancelling}
            className="py-3 rounded-xl bg-red-600 hover:bg-red-700 text-white text-sm font-bold shadow-md transition-colors disabled:opacity-50"
          >
            {cancelling ? 'Canceling...' : 'Yes, Cancel'}
          </button>
        </div>

      </div>
    </div>
  );
};

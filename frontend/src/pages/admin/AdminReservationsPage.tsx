import React, { useState, useEffect, useMemo } from 'react';
import { adminService } from '../../services/adminService';
import { AdminReservation } from '../../types/admin';
import {
  CalendarCheck,
  RotateCw,
  Search,
  AlertCircle,
  CheckCircle2,
  Trash2,
  Loader2,
  X,
} from 'lucide-react';

export const AdminReservationsPage: React.FC = () => {
  const [reservations, setReservations] = useState<AdminReservation[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [selectedForCancel, setSelectedForCancel] = useState<AdminReservation | null>(null);
  const [cancelling, setCancelling] = useState<boolean>(false);

  const fetchReservations = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await adminService.getReservations();
      setReservations(data);
    } catch (err: any) {
      setError(err.message || 'Failed to fetch reservations');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchReservations();
  }, []);

  const filteredReservations = useMemo(() => {
    if (!searchTerm.trim()) return reservations;
    const term = searchTerm.toLowerCase();
    return reservations.filter(
      (r) =>
        r.reservationId.toLowerCase().includes(term) ||
        r.customerId.toLowerCase().includes(term) ||
        r.hotelName.toLowerCase().includes(term) ||
        r.roomType.toLowerCase().includes(term)
    );
  }, [reservations, searchTerm]);

  const handleConfirmCancel = async () => {
    if (!selectedForCancel) return;
    setCancelling(true);
    setError(null);
    setSuccessMsg(null);
    try {
      await adminService.cancelReservation(selectedForCancel.reservationId);
      setSuccessMsg(`Reservation ${selectedForCancel.reservationId} was cancelled successfully.`);
      setReservations((prev) =>
        prev.filter((r) => r.reservationId !== selectedForCancel.reservationId)
      );
      setSelectedForCancel(null);
    } catch (err: any) {
      setError(err.message || 'Failed to cancel reservation');
    } finally {
      setCancelling(false);
    }
  };

  return (
    <div className="p-6 lg:p-8 space-y-6 max-w-7xl mx-auto">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 pb-6 border-b border-slate-800">
        <div>
          <h1 className="text-2xl font-bold font-serif text-white tracking-wide flex items-center gap-2.5">
            <CalendarCheck className="w-6 h-6 text-accent" />
            Reservation Management
          </h1>
          <p className="text-xs text-slate-400 mt-1">
            Inspect stored reservations, filter by booking reference, and perform administrative cancellations
          </p>
        </div>
        <button
          onClick={fetchReservations}
          disabled={loading}
          className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 text-xs font-semibold border border-slate-700 transition-colors disabled:opacity-50 self-start sm:self-auto cursor-pointer"
        >
          <RotateCw className={`w-3.5 h-3.5 ${loading ? 'animate-spin text-accent' : ''}`} />
          <span>Refresh</span>
        </button>
      </div>

      {error && (
        <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/20 text-rose-300 text-xs flex items-center gap-3">
          <AlertCircle className="w-5 h-5 shrink-0 text-rose-400" />
          <span>{error}</span>
        </div>
      )}

      {successMsg && (
        <div className="p-4 rounded-xl bg-emerald-500/10 border border-emerald-500/20 text-emerald-300 text-xs flex items-center gap-3">
          <CheckCircle2 className="w-5 h-5 shrink-0 text-emerald-400" />
          <span>{successMsg}</span>
        </div>
      )}

      <div className="flex flex-col sm:flex-row items-center justify-between gap-4">
        <div className="relative w-full sm:w-80">
          <Search className="w-4 h-4 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-500 pointer-events-none" />
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Search reference, customer, hotel..."
            className="w-full pl-10 pr-4 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-200 placeholder-slate-500 focus:outline-none focus:ring-1 focus:ring-accent focus:border-accent transition-all"
          />
        </div>
        <span className="text-xs text-slate-400 font-mono self-end sm:self-auto">
          Total: <span className="font-bold text-white">{filteredReservations.length}</span> records
        </span>
      </div>

      <div className="bg-slate-900 border border-slate-800 rounded-2xl shadow-xl overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead>
              <tr className="border-b border-slate-800 bg-slate-900/80 text-slate-400 uppercase tracking-wider font-semibold">
                <th className="p-4">Reference</th>
                <th className="p-4">Customer</th>
                <th className="p-4">Property</th>
                <th className="p-4">Dates</th>
                <th className="p-4">Room & Guests</th>
                <th className="p-4">Requests</th>
                <th className="p-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60 font-medium text-slate-300">
              {loading && reservations.length === 0 ? (
                <tr>
                  <td colSpan={7} className="py-12 text-center text-slate-400">
                    <Loader2 className="w-6 h-6 text-accent animate-spin mx-auto mb-2" />
                    Loading reservations...
                  </td>
                </tr>
              ) : filteredReservations.length === 0 ? (
                <tr>
                  <td colSpan={7} className="py-12 text-center text-slate-500">
                    No matching reservations found
                  </td>
                </tr>
              ) : (
                filteredReservations.map((r) => (
                  <tr key={r.reservationId} className="hover:bg-slate-800/40 transition-colors">
                    <td className="p-4 font-mono font-bold text-accent">{r.reservationId}</td>
                    <td className="p-4 font-mono text-slate-400">{r.customerId}</td>
                    <td className="p-4 text-white font-medium">{r.hotelName}</td>
                    <td className="p-4 text-slate-300 whitespace-nowrap">
                      {r.checkinDate} → {r.checkoutDate}
                    </td>
                    <td className="p-4 capitalize">
                      {r.roomType} ({r.guests} guests)
                    </td>
                    <td className="p-4 max-w-xs truncate text-slate-400">
                      {r.specialRequests || '—'}
                    </td>
                    <td className="p-4 text-right">
                      <button
                        onClick={() => setSelectedForCancel(r)}
                        className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-rose-400 hover:text-white hover:bg-rose-600/80 border border-rose-500/30 text-xs font-semibold transition-all cursor-pointer"
                      >
                        <Trash2 className="w-3.5 h-3.5" />
                        <span>Cancel</span>
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {selectedForCancel && (
        <div className="fixed inset-0 z-50 bg-black/70 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 max-w-md w-full shadow-2xl relative">
            <button
              onClick={() => setSelectedForCancel(null)}
              className="absolute top-4 right-4 text-slate-400 hover:text-white"
            >
              <X className="w-5 h-5" />
            </button>
            <div className="flex items-center gap-3 text-rose-400 mb-4">
              <AlertCircle className="w-6 h-6" />
              <h3 className="font-serif text-lg font-bold text-white">Cancel Reservation?</h3>
            </div>
            <p className="text-xs text-slate-300 leading-relaxed mb-4">
              Are you sure you want to cancel reservation{' '}
              <strong className="text-accent font-mono">{selectedForCancel.reservationId}</strong> for{' '}
              <span className="text-white">{selectedForCancel.hotelName}</span>? This action will permanently remove
              the booking from the database.
            </p>
            <div className="flex items-center justify-end gap-3 mt-6">
              <button
                onClick={() => setSelectedForCancel(null)}
                disabled={cancelling}
                className="px-4 py-2 rounded-xl bg-slate-800 text-slate-300 text-xs font-semibold hover:bg-slate-700 transition-colors"
              >
                Keep Booking
              </button>
              <button
                onClick={handleConfirmCancel}
                disabled={cancelling}
                className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-rose-600 hover:bg-rose-500 text-white text-xs font-semibold shadow-lg shadow-rose-600/25 transition-all"
              >
                {cancelling ? <Loader2 className="w-4 h-4 animate-spin" /> : <Trash2 className="w-4 h-4" />}
                <span>Confirm Cancellation</span>
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ReservationItem } from '../types';
import { reservationService } from '../services/reservationService';
import { ReservationCard } from '../components/reservations/ReservationCard';
import { CancelModal } from '../components/reservations/CancelModal';
import { useAuth } from '../context/AuthContext';
import { CalendarCheck, AlertCircle, RefreshCw, BookmarkX, LogIn, UserPlus } from 'lucide-react';

export const ReservationManagementPage: React.FC = () => {
  const { user, isAuthenticated, loading: authLoading } = useAuth();
  const [reservations, setReservations] = useState<ReservationItem[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  // Cancellation state
  const [selectedRes, setSelectedRes] = useState<ReservationItem | null>(null);
  const [isCancelModalOpen, setIsCancelModalOpen] = useState<boolean>(false);
  const [cancelling, setCancelling] = useState<boolean>(false);
  const [cancelSuccessMsg, setCancelSuccessMsg] = useState<string | null>(null);

  const fetchReservations = async () => {
    if (!isAuthenticated) {
      setReservations([]);
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const data = await reservationService.getAllReservations();
      if (Array.isArray(data)) {
        setReservations(data);
      } else {
        setReservations([]);
      }
    } catch (err: any) {
      setError(err.message || 'Failed to load reservation history');
      setReservations([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!authLoading) {
      if (isAuthenticated) {
        fetchReservations();
      } else {
        setReservations([]);
        setLoading(false);
      }
    }
  }, [isAuthenticated, authLoading, user]);

  const handleOpenCancelModal = (res: ReservationItem) => {
    setSelectedRes(res);
    setIsCancelModalOpen(true);
  };

  const handleConfirmCancel = async () => {
    if (!selectedRes) return;
    setCancelling(true);
    try {
      const response = await reservationService.cancelReservation(selectedRes.reservationId);
      if (response.success) {
        setCancelSuccessMsg(`Reservation ID #${selectedRes.reservationId} has been successfully canceled.`);
        setReservations((prev) => prev.filter((r) => r.reservationId !== selectedRes.reservationId));
      } else {
        setError(response.message || 'Failed to cancel reservation');
      }
    } catch (err: any) {
      console.error('Cancel failed:', err);
      setError(err.message || 'An error occurred while canceling your reservation.');
    } finally {
      setCancelling(false);
      setIsCancelModalOpen(false);
      setSelectedRes(null);
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">

      {/* Page Header */}
      <div className="flex items-center justify-between mb-8 pb-4 border-b border-slate-200/80">
        <div>
          <h1 className="font-serif text-3xl font-bold text-brand-900">My Reservations</h1>
          <p className="text-xs text-slate-500 mt-1">
            {isAuthenticated
              ? `Logged in as ${user?.firstName ? `${user.firstName} ${user.lastName || ''}` : user?.email}`
              : 'Sign in to inspect and manage your hotel reservations'}
          </p>
        </div>
        <div className="p-3 rounded-2xl bg-accent/10 text-accent">
          <CalendarCheck className="w-6 h-6" />
        </div>
      </div>

      {cancelSuccessMsg && (
        <div className="mb-6 bg-emerald-50 border border-emerald-200 rounded-2xl p-4 text-xs text-emerald-700 font-semibold flex items-center justify-between">
          <span>{cancelSuccessMsg}</span>
          <button onClick={() => setCancelSuccessMsg(null)} className="text-emerald-900 hover:underline">Dismiss</button>
        </div>
      )}

      {!authLoading && !isAuthenticated ? (
        <div className="bg-white border border-slate-200/80 rounded-3xl p-12 text-center max-w-lg mx-auto my-12 shadow-sm">
          <div className="w-14 h-14 bg-accent/10 text-accent rounded-2xl flex items-center justify-center mx-auto mb-4">
            <LogIn className="w-7 h-7" />
          </div>
          <h2 className="font-serif text-2xl font-bold text-brand-900 mb-2">Sign In Required</h2>
          <p className="text-xs text-slate-600 mb-6 leading-relaxed">
            Please log in to your account to view your verified booking history, check-in dates, and room details.
          </p>
          <div className="flex flex-col sm:flex-row items-center justify-center gap-3">
            <Link
              to="/login"
              className="w-full sm:w-auto px-6 py-2.5 rounded-xl bg-accent hover:bg-amber-600 text-white font-semibold text-xs shadow-md shadow-accent/25 transition-all inline-flex items-center justify-center gap-2"
            >
              <LogIn className="w-4 h-4" />
              <span>Sign In to Account</span>
            </Link>
            <Link
              to="/signup"
              className="w-full sm:w-auto px-6 py-2.5 rounded-xl bg-slate-100 hover:bg-slate-200 text-slate-700 font-semibold text-xs transition-all inline-flex items-center justify-center gap-2"
            >
              <UserPlus className="w-4 h-4" />
              <span>Create Account</span>
            </Link>
          </div>
        </div>
      ) : loading || authLoading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[1, 2, 3].map((idx) => (
            <div key={idx} className="bg-white rounded-3xl h-64 p-6 border border-slate-200 animate-pulse" />
          ))}
        </div>
      ) : error ? (
        <div className="bg-red-50 border border-red-200 rounded-3xl p-12 text-center max-w-lg mx-auto">
          <AlertCircle className="w-8 h-8 text-red-500 mx-auto mb-3" />
          <h3 className="font-bold text-red-900 mb-1">Error Loading Bookings</h3>
          <p className="text-xs text-red-700 mb-4">{error}</p>
          <button
            onClick={fetchReservations}
            className="px-4 py-2 rounded-xl bg-red-600 text-white text-xs font-semibold inline-flex items-center gap-1.5 cursor-pointer"
          >
            <RefreshCw className="w-4 h-4" /> Retry
          </button>
        </div>
      ) : reservations.length === 0 ? (
        <div className="bg-white border border-slate-200/80 rounded-3xl p-16 text-center max-w-md mx-auto my-12">
          <BookmarkX className="w-12 h-12 text-slate-400 mx-auto mb-3" />
          <h3 className="font-serif text-2xl font-bold text-brand-900 mb-2">No Active Reservations</h3>
          <p className="text-xs text-slate-500">You currently have no upcoming hotel reservations.</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {reservations.map((res) => (
            <ReservationCard
              key={res.reservationId}
              reservation={res}
              onCancelClick={handleOpenCancelModal}
            />
          ))}
        </div>
      )}

      {/* Cancellation Confirmation Modal */}
      <CancelModal
        reservation={selectedRes}
        isOpen={isCancelModalOpen}
        onClose={() => setIsCancelModalOpen(false)}
        onConfirm={handleConfirmCancel}
        cancelling={cancelling}
      />
    </div>
  );
};

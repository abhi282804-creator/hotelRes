import React from 'react';
import { useLocation, Link } from 'react-router-dom';
import { CheckCircle2, AlertCircle, ArrowLeft, CalendarCheck, Home } from 'lucide-react';

export const PaymentStatusPage: React.FC = () => {
  const location = useLocation();
  const state = location.state || {};

  const success = state.success ?? true;
  const message = state.message || 'Payment processed successfully.';
  const reservationId = state.reservationId || 'RES234874001';
  const amount = state.amount || 32500;

  return (
    <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
      <div className="bg-white p-8 sm:p-12 rounded-3xl border border-slate-200/80 shadow-xl text-center space-y-8">

        {success ? (
          <>
            <div className="w-20 h-20 rounded-full bg-emerald-100 text-emerald-600 flex items-center justify-center mx-auto shadow-md">
              <CheckCircle2 className="w-12 h-12" />
            </div>

            <div className="space-y-2">
              <h1 className="font-serif text-3xl font-bold text-brand-900">Payment Successful!</h1>
              <p className="text-sm text-slate-600 max-w-md mx-auto">{message}</p>
            </div>

            <div className="bg-slate-50 p-6 rounded-2xl border border-slate-200 text-xs space-y-2.5 max-w-md mx-auto text-left">
              <div className="flex justify-between">
                <span className="text-slate-500 font-medium">Reservation ID</span>
                <span className="font-bold text-accent font-mono">#{reservationId}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-slate-500 font-medium">Amount Paid</span>
                <span className="font-bold text-brand-900">₹{amount.toLocaleString('en-IN')}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-slate-500 font-medium">Payment Status</span>
                <span className="font-bold text-emerald-600 uppercase tracking-wider">Completed</span>
              </div>
            </div>

            <div className="flex flex-col sm:flex-row gap-3 justify-center pt-4">
              <Link
                to="/reservations"
                className="px-6 py-3.5 rounded-xl bg-accent hover:bg-accent-hover text-white text-sm font-bold shadow-md inline-flex items-center justify-center gap-2"
              >
                <CalendarCheck className="w-4 h-4" />
                Return to Reservations
              </Link>
              <Link
                to="/"
                className="px-6 py-3.5 rounded-xl border border-slate-200 text-slate-700 text-sm font-bold hover:bg-slate-50 inline-flex items-center justify-center gap-2"
              >
                <Home className="w-4 h-4" />
                Go to Homepage
              </Link>
            </div>
          </>
        ) : (
          <>
            <div className="w-20 h-20 rounded-full bg-red-100 text-red-600 flex items-center justify-center mx-auto shadow-md">
              <AlertCircle className="w-12 h-12" />
            </div>

            <div className="space-y-2">
              <h1 className="font-serif text-3xl font-bold text-brand-900">Payment Failed</h1>
              <p className="text-sm text-red-600 max-w-md mx-auto">{message}</p>
            </div>

            <div className="flex flex-col sm:flex-row gap-3 justify-center pt-4">
              <Link
                to="/payment"
                state={{ reservationId, amount }}
                className="px-6 py-3.5 rounded-xl bg-accent hover:bg-accent-hover text-white text-sm font-bold shadow-md inline-flex items-center justify-center gap-2"
              >
                <ArrowLeft className="w-4 h-4" />
                Try Payment Again
              </Link>
            </div>
          </>
        )}

      </div>
    </div>
  );
};

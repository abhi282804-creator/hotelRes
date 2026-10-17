import React, { useState } from 'react';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { PaymentMethod } from '../types';
import { paymentService } from '../services/paymentService';
import { MethodSelector } from '../components/payment/MethodSelector';
import { UpiForm } from '../components/payment/UpiForm';
import { QrScannerView } from '../components/payment/QrScannerView';
import { CardForm } from '../components/payment/CardForm';
import { OrderSummary } from '../components/payment/OrderSummary';
import { Lock, AlertCircle, ShieldCheck, Home } from 'lucide-react';

export const PaymentPage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const state = location.state || {};

  const reservationId = state.reservationId || 'RES234874001';
  const priceDisplay = state.priceDisplay || '30000 - 35000';
  const hotelName = state.hotelName || 'Hotel Grand Palace';

  // Calculate amount matching payment.jsp formula: ((min + max) / 2) * nights
  const prices = priceDisplay.split(' - ').map((p: string) => parseFloat(p.replace(/[^0-9.]/g, '')));
  const minPrice = prices[0] || 30000;
  const maxPrice = prices[1] || 35000;
  const nights = state.nights || 1;
  const totalAmount = ((minPrice + maxPrice) / 2) * nights;

  // Method & Inputs State
  const [paymentMethod, setPaymentMethod] = useState<PaymentMethod>('UPI');
  const [upiId, setUpiId] = useState('');
  const [cardName, setCardName] = useState('');
  const [cardNumber, setCardNumber] = useState('');
  const [expiryDate, setExpiryDate] = useState('');
  const [cvv, setCvv] = useState('');

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const response = await paymentService.processPayment({
        reservationId,
        amount: totalAmount,
        paymentMethod,
        upiId,
        cardName,
        cardNumber,
        expiryDate,
        cvv,
      });

      navigate('/payment/status', {
        state: {
          success: response.success,
          message: response.message,
          reservationId,
          amount: totalAmount,
          paymentMethod,
        },
      });
    } catch (err: any) {
      setError(err.message || 'Payment processing failed. Please verify fields and try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div className="mb-8 flex items-center justify-between">
        <div>
          <h1 className="font-serif text-3xl font-bold text-brand-900">Payment Gateway</h1>
          <p className="text-xs text-slate-500 mt-1">256-Bit SSL Encrypted & PCI-DSS Compliant</p>
        </div>
        <div className="flex items-center gap-1.5 text-xs font-semibold text-emerald-700 bg-emerald-50 px-3 py-1.5 rounded-full border border-emerald-200">
          <ShieldCheck className="w-4 h-4 text-emerald-600" />
          Secure Gateway
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">

        {/* Left Column (2 Cols): Payment Options Form */}
        <div className="lg:col-span-2 space-y-8 bg-white p-6 sm:p-8 rounded-3xl border border-slate-200/80 shadow-lg">

          {error && (
            <div className="bg-red-50 border border-red-200 rounded-2xl p-4 text-xs text-red-700 font-medium flex items-center gap-2">
              <AlertCircle className="w-5 h-5 shrink-0 text-red-500" />
              <span>{error}</span>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">

            {/* Method Selector */}
            <MethodSelector
              selectedMethod={paymentMethod}
              onSelectMethod={setPaymentMethod}
            />

            {/* Dynamic Interface Panels */}
            {paymentMethod === 'UPI' && (
              <UpiForm upiId={upiId} onUpiIdChange={setUpiId} />
            )}

            {paymentMethod === 'QRScanner' && (
              <QrScannerView />
            )}

            {(paymentMethod === 'DebitCard' || paymentMethod === 'CreditCard') && (
              <CardForm
                cardName={cardName}
                cardNumber={cardNumber}
                expiryDate={expiryDate}
                cvv={cvv}
                onCardNameChange={setCardName}
                onCardNumberChange={setCardNumber}
                onExpiryDateChange={setExpiryDate}
                onCvvChange={setCvv}
              />
            )}

            {/* Submit CTA */}
            <button
              type="submit"
              disabled={loading}
              className="w-full py-4 rounded-xl bg-emerald-600 hover:bg-emerald-700 text-white text-base font-bold shadow-lg shadow-emerald-600/25 transition-all duration-200 flex items-center justify-center gap-2 hover:scale-[1.01] disabled:opacity-50"
            >
              {loading ? (
                <span>Processing Payment...</span>
              ) : (
                <>
                  <Lock className="w-5 h-5" />
                  <span>Pay ₹{totalAmount.toLocaleString('en-IN', { minimumFractionDigits: 2 })} Now</span>
                </>
              )}
            </button>
          </form>

        </div>

        {/* Right Column (1 Col): Order Summary */}
        <div className="lg:col-span-1">
          <OrderSummary
            reservationId={reservationId}
            hotelName={hotelName}
            checkinDate={state.checkinDate}
            checkoutDate={state.checkoutDate}
            guests={state.guests}
            roomType={state.roomType}
            priceDisplay={priceDisplay}
            amount={totalAmount}
          />
        </div>

      </div>
    </div>
  );
};

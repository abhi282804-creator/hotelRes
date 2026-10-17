import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { RoomType } from '../../types';
import { RoomTypePicker } from './RoomTypePicker';
import { reservationService } from '../../services/reservationService';
import { useAuth } from '../../context/AuthContext';
import { Calendar, Users, FileText, AlertCircle, ArrowRight, ShieldCheck } from 'lucide-react';

interface BookingFormProps {
  hotelId: number;
  hotelName: string;
}

export const BookingForm: React.FC<BookingFormProps> = ({ hotelId, hotelName }) => {
  const navigate = useNavigate();
  const { user } = useAuth();

  // Form state
  const [checkin, setCheckin] = useState<string>('');
  const [checkout, setCheckout] = useState<string>('');
  const [guests, setGuests] = useState<number>(2);
  const [roomType, setRoomType] = useState<RoomType>('standard');
  const [specialRequests, setSpecialRequests] = useState<string>('');
  const [priceDisplay, setPriceDisplay] = useState<string>('10000 - 12000');

  // Execution state
  const [loading, setLoading] = useState<boolean>(false);
  const [isFullyBooked, setIsFullyBooked] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  // Set default dates (today & tomorrow)
  useEffect(() => {
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 2);

    setCheckin(today.toISOString().split('T')[0]);
    setCheckout(tomorrow.toISOString().split('T')[0]);
  }, []);

  // Check availability from Reservation1Dao
  useEffect(() => {
    async function fetchAvailability() {
      try {
        const res = await reservationService.checkAvailability(hotelId);
        if (res.isFullyBooked) {
          setIsFullyBooked(true);
        }
      } catch (err) {
        console.error('Availability check failed:', err);
      }
    }
    fetchAvailability();
  }, [hotelId]);

  // Handle Room Type price calculation update (matching legacy updatePrice())
  const handleRoomTypeChange = (selected: RoomType) => {
    setRoomType(selected);
    if (selected === 'standard') {
      setPriceDisplay('10000 - 12000');
    } else if (selected === 'deluxe') {
      setPriceDisplay('15000 - 18000');
    } else if (selected === 'suite') {
      setPriceDisplay('20000 - 25000');
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (isFullyBooked) return;

    setError(null);
    setLoading(true);

    try {
      // Use authenticated customer ID if logged in, otherwise generate customer reference
      const randomCust = user?.customerId
        ? 'C' + String(user.customerId).padStart(5, '0')
        : 'C' + String(Math.floor(Math.random() * 89999) + 10000);
      const randomRoom = String(Math.floor(Math.random() * 299) + 1).padStart(3, '0');

      const response = await reservationService.createReservation({
        customerId: randomCust,
        hotelId: String(hotelId),
        roomId: randomRoom,
        checkin,
        checkout,
        guests,
        roomType,
        specialRequests,
        priceDisplay,
      });

      if (response.success) {
        // Navigate to confirmation page passing state
        navigate('/reservation/confirm', {
          state: {
            reservationDetails: {
              reservationId: response.reservationId || `RES${randomCust}${randomRoom}`,
              customerId: response.customerId || randomCust,
              hotelId: response.hotelId || String(hotelId),
              hotelName: hotelName,
              roomId: response.roomId || randomRoom,
              checkinDate: response.checkinDate || checkin,
              checkoutDate: response.checkoutDate || checkout,
              guests: response.guests || guests,
              roomType: response.roomType || roomType,
              specialRequests: response.specialRequests || specialRequests,
              priceDisplay: priceDisplay,
            },
          },
        });
      } else {
        setError(response.message || 'Failed to submit reservation. Please try again.');
      }
    } catch (err: any) {
      setError(err.message || 'An error occurred while connecting to the server.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white p-6 sm:p-8 rounded-3xl border border-slate-200/80 shadow-lg sticky top-28">
      <div className="flex items-center justify-between pb-6 border-b border-slate-100 mb-6">
        <div>
          <h3 className="font-serif text-2xl font-bold text-brand-900">Make a Reservation</h3>
          <p className="text-xs text-slate-500 mt-1">Instant confirmation • Zero booking fees</p>
        </div>
        <div className="p-2 rounded-xl bg-accent/10 text-accent">
          <ShieldCheck className="w-6 h-6" />
        </div>
      </div>

      {isFullyBooked ? (
        <div className="bg-red-50 border border-red-200 rounded-2xl p-6 text-center text-red-700">
          <AlertCircle className="w-8 h-8 text-red-500 mx-auto mb-2" />
          <h4 className="font-bold text-base mb-1">Fully Booked</h4>
          <p className="text-xs">All rooms are currently booked for this hotel. Please try another hotel or change dates.</p>
        </div>
      ) : (
        <form onSubmit={handleSubmit} className="space-y-6">
          {error && (
            <div className="bg-red-50 border border-red-200 rounded-xl p-3.5 text-xs text-red-700 font-medium flex items-center gap-2">
              <AlertCircle className="w-4 h-4 shrink-0 text-red-500" />
              <span>{error}</span>
            </div>
          )}

          {/* Dates Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label htmlFor="checkin" className="text-xs font-bold text-slate-500 uppercase tracking-wider block mb-1.5 flex items-center gap-1.5">
                <Calendar className="w-3.5 h-3.5 text-accent" />
                Check-in Date
              </label>
              <input
                type="date"
                id="checkin"
                required
                value={checkin}
                onChange={(e) => setCheckin(e.target.value)}
                className="w-full bg-slate-50 border border-slate-200 text-brand-900 text-sm font-medium rounded-xl p-3 focus:ring-2 focus:ring-accent/20 focus:border-accent outline-none"
              />
            </div>

            <div>
              <label htmlFor="checkout" className="text-xs font-bold text-slate-500 uppercase tracking-wider block mb-1.5 flex items-center gap-1.5">
                <Calendar className="w-3.5 h-3.5 text-accent" />
                Check-out Date
              </label>
              <input
                type="date"
                id="checkout"
                required
                value={checkout}
                onChange={(e) => setCheckout(e.target.value)}
                className="w-full bg-slate-50 border border-slate-200 text-brand-900 text-sm font-medium rounded-xl p-3 focus:ring-2 focus:ring-accent/20 focus:border-accent outline-none"
              />
            </div>
          </div>

          {/* Guest Count */}
          <div>
            <label htmlFor="guests" className="text-xs font-bold text-slate-500 uppercase tracking-wider block mb-1.5 flex items-center gap-1.5">
              <Users className="w-3.5 h-3.5 text-accent" />
              Number of Guests
            </label>
            <input
              type="number"
              id="guests"
              min={1}
              max={10}
              required
              value={guests}
              onChange={(e) => setGuests(Number(e.target.value))}
              className="w-full bg-slate-50 border border-slate-200 text-brand-900 text-sm font-medium rounded-xl p-3 focus:ring-2 focus:ring-accent/20 focus:border-accent outline-none"
            />
          </div>

          {/* Room Type Picker */}
          <RoomTypePicker
            selectedRoomType={roomType}
            onSelectRoomType={handleRoomTypeChange}
            priceDisplay={priceDisplay}
          />

          {/* Special Requests */}
          <div>
            <label htmlFor="specialRequests" className="text-xs font-bold text-slate-500 uppercase tracking-wider block mb-1.5 flex items-center gap-1.5">
              <FileText className="w-3.5 h-3.5 text-accent" />
              Special Requests (Optional)
            </label>
            <input
              type="text"
              id="specialRequests"
              placeholder="e.g. Early check-in, quiet room, high floor..."
              value={specialRequests}
              onChange={(e) => setSpecialRequests(e.target.value)}
              className="w-full bg-slate-50 border border-slate-200 text-brand-900 text-sm font-medium rounded-xl p-3 focus:ring-2 focus:ring-accent/20 focus:border-accent outline-none"
            />
          </div>

          {/* Submit Button */}
          <button
            type="submit"
            disabled={loading}
            className="w-full py-4 rounded-xl bg-accent hover:bg-accent-hover text-white text-base font-bold shadow-lg shadow-accent/25 transition-all duration-200 flex items-center justify-center gap-2 hover:scale-[1.01] disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? (
              <span>Creating Reservation...</span>
            ) : (
              <>
                <span>Reserve Now</span>
                <ArrowRight className="w-5 h-5" />
              </>
            )}
          </button>
        </form>
      )}
    </div>
  );
};

export type RoomType = 'standard' | 'deluxe' | 'suite';
export type PaymentMethod = 'UPI' | 'QRScanner' | 'DebitCard' | 'CreditCard';
export type HotelCategory = 'beach' | 'city' | 'mountain' | 'luxury' | 'lake';

export interface Hotel {
  id: number;
  backendHotelId: number;
  name: string;
  category: HotelCategory;
  price: number;
  priceMin: number;
  priceMax: number;
  rating: number;
  location: string;
  description: string;
  locationOverview: string;
  images: string[];
  amenities: string[];
}

export interface User {
  customerId?: number | string;
  firstName?: string;
  lastName?: string;
  email: string;
  phone?: string;
}

export interface ConfirmReservationPayload {
  customerId?: string;
  hotelId: string | number;
  roomId?: string;
  checkin: string;
  checkout: string;
  guests: number;
  roomType: RoomType;
  specialRequests?: string;
  priceDisplay?: string;
}

export interface ReservationResponse {
  success: boolean;
  message: string;
  reservationId?: string;
  customerId?: string;
  roomId?: string;
  hotelId?: string;
  checkinDate?: string;
  checkoutDate?: string;
  guests?: number;
  roomType?: string;
  specialRequests?: string;
}

export interface ReservationItem {
  reservationId: number | string;
  hotelName: string;
  checkinDate: string;
  checkoutDate: string;
  guests: number;
  roomType: string;
}

export interface ProcessPaymentPayload {
  reservationId: string;
  amount: number;
  paymentMethod: PaymentMethod;
  upiId?: string;
  cardName?: string;
  cardNumber?: string;
  expiryDate?: string;
  cvv?: string;
}

export interface PaymentResponse {
  success: boolean;
  message: string;
  reservationId?: string;
  amount?: number;
  paymentMethod?: string;
}

export interface AvailabilityResponse {
  hotelId: number;
  reservationCount: number;
  isFullyBooked: boolean;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  error?: string;
}

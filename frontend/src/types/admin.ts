export interface AdminUser {
  username: string;
  fullName: string;
}

export interface AdminMetrics {
  successfulPaymentAmount: number;
  storedReservations: number;
  totalCustomers: number;
  totalHotels: number;
  totalRooms: number;
}

export interface AdminReservation {
  reservationId: string;
  customerId: string;
  hotelId: number;
  hotelName: string;
  checkinDate: string;
  checkoutDate: string;
  guests: number;
  roomType: string;
  specialRequests?: string;
}

export interface AdminPayment {
  paymentId: number;
  reservationId: string;
  amount: number;
  paymentMethod: string;
  paymentStatus: string;
  paymentDate: string;
  maskedCardNumber?: string;
}

export interface AdminCustomer {
  customerId: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
}

export interface AdminDashboardData {
  success: boolean;
  metrics: AdminMetrics;
  recentReservations: AdminReservation[];
  recentPayments: AdminPayment[];
}

export interface AdminAuthResponse {
  authenticated?: boolean;
  success?: boolean;
  message?: string;
  admin?: AdminUser;
}

export interface AdminActionResponse {
  success: boolean;
  message: string;
}

import { apiRequest, apiUrl, toFormUrlEncoded } from './api';
import {
  AdminAuthResponse,
  AdminActionResponse,
  AdminDashboardData,
  AdminReservation,
  AdminCustomer,
  AdminPayment,
} from '../types/admin';

export const adminService = {
  async login(username: string, password: string): Promise<AdminAuthResponse> {
    return apiRequest<AdminAuthResponse>(apiUrl('/admin/auth'), {
      method: 'POST',
      body: toFormUrlEncoded({
        action: 'login',
        username,
        password,
      }),
    });
  },

  async logout(): Promise<AdminActionResponse> {
    return apiRequest<AdminActionResponse>(apiUrl('/admin/auth'), {
      method: 'POST',
      body: toFormUrlEncoded({ action: 'logout' }),
    });
  },

  async checkAuth(): Promise<AdminAuthResponse> {
    return apiRequest<AdminAuthResponse>(apiUrl('/admin/auth'), {
      method: 'GET',
    });
  },

  async getMetrics(): Promise<AdminDashboardData> {
    return apiRequest<AdminDashboardData>(apiUrl('/admin/metrics'), {
      method: 'GET',
    });
  },

  async getReservations(): Promise<AdminReservation[]> {
    return apiRequest<AdminReservation[]>(apiUrl('/admin/reservations'), {
      method: 'GET',
    });
  },

  async cancelReservation(reservationId: string): Promise<AdminActionResponse> {
    return apiRequest<AdminActionResponse>(apiUrl('/admin/reservations/cancel'), {
      method: 'POST',
      body: toFormUrlEncoded({ reservationId }),
    });
  },

  async getCustomers(): Promise<AdminCustomer[]> {
    return apiRequest<AdminCustomer[]>(apiUrl('/admin/customers'), {
      method: 'GET',
    });
  },

  async getPayments(): Promise<AdminPayment[]> {
    return apiRequest<AdminPayment[]>(apiUrl('/admin/payments'), {
      method: 'GET',
    });
  },
};

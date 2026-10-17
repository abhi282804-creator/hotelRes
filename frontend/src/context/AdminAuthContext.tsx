import React, { createContext, useContext, useState, useEffect } from 'react';
import { AdminUser } from '../types/admin';
import { adminService } from '../services/adminService';

interface AdminAuthContextType {
  admin: AdminUser | null;
  isAuthenticated: boolean;
  loading: boolean;
  login: (username: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
}

const AdminAuthContext = createContext<AdminAuthContextType | undefined>(undefined);

export const AdminAuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [admin, setAdmin] = useState<AdminUser | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    async function initAuth() {
      try {
        const res = await adminService.checkAuth();
        if (res.authenticated && res.admin) {
          setAdmin(res.admin);
        } else {
          setAdmin(null);
        }
      } catch (err) {
        console.error('Admin session check failed:', err);
        setAdmin(null);
      } finally {
        setLoading(false);
      }
    }
    initAuth();
  }, []);

  const login = async (username: string, password: string) => {
    const res = await adminService.login(username, password);
    if (res.success && res.admin) {
      setAdmin(res.admin);
    } else {
      throw new Error(res.message || 'Invalid administrator credentials');
    }
  };

  const logout = async () => {
    try {
      await adminService.logout();
    } finally {
      setAdmin(null);
    }
  };

  return (
    <AdminAuthContext.Provider
      value={{
        admin,
        isAuthenticated: !!admin,
        loading,
        login,
        logout,
      }}
    >
      {children}
    </AdminAuthContext.Provider>
  );
};

export const useAdminAuth = (): AdminAuthContextType => {
  const context = useContext(AdminAuthContext);
  if (!context) {
    throw new Error('useAdminAuth must be used within an AdminAuthProvider');
  }
  return context;
};

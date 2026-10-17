package com.dao;

import com.util.DBConnection;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AdminDao {

    public static class AdminUser {
        private final int adminId;
        private final String username;
        private final String fullName;

        public AdminUser(int adminId, String username, String fullName) {
            this.adminId = adminId;
            this.username = username;
            this.fullName = fullName;
        }

        public int getAdminId() {
            return adminId;
        }

        public String getUsername() {
            return username;
        }

        public String getFullName() {
            return fullName;
        }
    }

    public static class AdminMetrics {
        private final double successfulPaymentAmount;
        private final int storedReservations;
        private final int totalCustomers;
        private final int totalHotels;
        private final int totalRooms;

        public AdminMetrics(double successfulPaymentAmount, int storedReservations, int totalCustomers, int totalHotels, int totalRooms) {
            this.successfulPaymentAmount = successfulPaymentAmount;
            this.storedReservations = storedReservations;
            this.totalCustomers = totalCustomers;
            this.totalHotels = totalHotels;
            this.totalRooms = totalRooms;
        }

        public double getSuccessfulPaymentAmount() {
            return successfulPaymentAmount;
        }

        public int getStoredReservations() {
            return storedReservations;
        }

        public int getTotalCustomers() {
            return totalCustomers;
        }

        public int getTotalHotels() {
            return totalHotels;
        }

        public int getTotalRooms() {
            return totalRooms;
        }
    }

    public static class AdminReservation {
        private String reservationId;
        private String customerId;
        private int hotelId;
        private String hotelName;
        private String checkinDate;
        private String checkoutDate;
        private int guests;
        private String roomType;
        private String specialRequests;

        public AdminReservation() {
        }

        public String getReservationId() {
            return reservationId;
        }

        public void setReservationId(String reservationId) {
            this.reservationId = reservationId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public int getHotelId() {
            return hotelId;
        }

        public void setHotelId(int hotelId) {
            this.hotelId = hotelId;
        }

        public String getHotelName() {
            return hotelName;
        }

        public void setHotelName(String hotelName) {
            this.hotelName = hotelName;
        }

        public String getCheckinDate() {
            return checkinDate;
        }

        public void setCheckinDate(String checkinDate) {
            this.checkinDate = checkinDate;
        }

        public String getCheckoutDate() {
            return checkoutDate;
        }

        public void setCheckoutDate(String checkoutDate) {
            this.checkoutDate = checkoutDate;
        }

        public int getGuests() {
            return guests;
        }

        public void setGuests(int guests) {
            this.guests = guests;
        }

        public String getRoomType() {
            return roomType;
        }

        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }

        public String getSpecialRequests() {
            return specialRequests;
        }

        public void setSpecialRequests(String specialRequests) {
            this.specialRequests = specialRequests;
        }
    }

    public static class AdminCustomer {
        private final int customerId;
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String phone;

        public AdminCustomer(int customerId, String firstName, String lastName, String email, String phone) {
            this.customerId = customerId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
        }

        public int getCustomerId() {
            return customerId;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }
    }

    public static class AdminPayment {
        private final int paymentId;
        private final String reservationId;
        private final double amount;
        private final String paymentMethod;
        private final String paymentStatus;
        private final String paymentDate;
        private final String maskedCardNumber;

        public AdminPayment(int paymentId, String reservationId, double amount, String paymentMethod, String paymentStatus, String paymentDate, String maskedCardNumber) {
            this.paymentId = paymentId;
            this.reservationId = reservationId;
            this.amount = amount;
            this.paymentMethod = paymentMethod;
            this.paymentStatus = paymentStatus;
            this.paymentDate = paymentDate;
            this.maskedCardNumber = maskedCardNumber;
        }

        public int getPaymentId() {
            return paymentId;
        }

        public String getReservationId() {
            return reservationId;
        }

        public double getAmount() {
            return amount;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public String getMaskedCardNumber() {
            return maskedCardNumber;
        }
    }

    public AdminDao() {
    }

    public static String hashPassword(String password, String salt) {
        try {
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65536, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public boolean verifyPassword(String password, String expectedHash, String salt) {
        String computedHash = hashPassword(password, salt);
        byte[] expectedBytes = Base64.getDecoder().decode(expectedHash);
        byte[] computedBytes = Base64.getDecoder().decode(computedHash);
        return slowEquals(expectedBytes, computedBytes);
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    public AdminUser authenticate(String username, String password) {
        String sql = "SELECT admin_id, username, password_hash, salt, full_name FROM admins WHERE username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                String hash = rs.getString("password_hash");
                String salt = rs.getString("salt");
                if (verifyPassword(password, hash, salt)) {
                    return new AdminUser(
                        rs.getInt("admin_id"),
                        rs.getString("username"),
                        rs.getString("full_name")
                    );
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return null;
    }

    public AdminMetrics getMetrics() {
        double successfulPaymentAmount = 0.0;
        int storedReservations = 0;
        int totalCustomers = 0;
        int totalHotels = 0;
        int totalRooms = 0;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            ps = conn.prepareStatement("SELECT COALESCE(SUM(amount), 0) FROM payments WHERE payment_success = 1");
            rs = ps.executeQuery();
            if (rs.next()) {
                successfulPaymentAmount = rs.getDouble(1);
            }
            rs.close();
            ps.close();

            ps = conn.prepareStatement("SELECT COUNT(*) FROM reservations");
            rs = ps.executeQuery();
            if (rs.next()) {
                storedReservations = rs.getInt(1);
            }
            rs.close();
            ps.close();

            ps = conn.prepareStatement("SELECT COUNT(*) FROM customers");
            rs = ps.executeQuery();
            if (rs.next()) {
                totalCustomers = rs.getInt(1);
            }
            rs.close();
            ps.close();

            ps = conn.prepareStatement("SELECT COUNT(*) FROM hotels");
            rs = ps.executeQuery();
            if (rs.next()) {
                totalHotels = rs.getInt(1);
            }
            rs.close();
            ps.close();

            ps = conn.prepareStatement("SELECT COUNT(*) FROM rooms");
            rs = ps.executeQuery();
            if (rs.next()) {
                totalRooms = rs.getInt(1);
            }
            rs.close();
            ps.close();
            ps = null;

            return new AdminMetrics(successfulPaymentAmount, storedReservations, totalCustomers, totalHotels, totalRooms);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return new AdminMetrics(0.0, 0, 0, 0, 0);
    }

    public List<AdminReservation> getAllReservations() {
        return getReservationsWithLimit(0);
    }

    public List<AdminReservation> getRecentReservations(int limit) {
        return getReservationsWithLimit(limit);
    }

    private List<AdminReservation> getReservationsWithLimit(int limit) {
        List<AdminReservation> list = new ArrayList<>();
        String sql = "SELECT r.reservation_id, r.customer_id, r.hotel_id, " +
                     "COALESCE(h.hotel_name, CONCAT('Hotel #', r.hotel_id)) AS hotel_name, " +
                     "r.checkin_date, r.checkout_date, r.guests, r.room_type, r.special_requests " +
                     "FROM reservations r " +
                     "LEFT JOIN hotels h ON (r.hotel_id = h.hotel_id OR (h.legacy_id IS NOT NULL AND r.hotel_id = h.legacy_id)) " +
                     "ORDER BY r.checkin_date DESC";
        if (limit > 0) {
            sql += " LIMIT " + limit;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                AdminReservation item = new AdminReservation();
                item.setReservationId(rs.getString("reservation_id"));
                item.setCustomerId(rs.getString("customer_id"));
                item.setHotelId(rs.getInt("hotel_id"));
                item.setHotelName(rs.getString("hotel_name"));
                item.setCheckinDate(rs.getString("checkin_date"));
                item.setCheckoutDate(rs.getString("checkout_date"));
                item.setGuests(rs.getInt("guests"));
                item.setRoomType(rs.getString("room_type"));
                item.setSpecialRequests(rs.getString("special_requests"));
                list.add(item);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return list;
    }

    public boolean cancelReservation(String reservationId) {
        if (reservationId == null || reservationId.trim().isEmpty()) {
            return false;
        }
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, reservationId.trim());
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(null, ps, conn);
        }
    }

    public List<AdminCustomer> getAllCustomers() {
        List<AdminCustomer> list = new ArrayList<>();
        String sql = "SELECT customer_id, first_name, last_name, email, phone FROM customers ORDER BY customer_id ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new AdminCustomer(
                    rs.getInt("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return list;
    }

    public List<AdminPayment> getAllPayments() {
        return getPaymentsWithLimit(0);
    }

    public List<AdminPayment> getRecentPayments(int limit) {
        return getPaymentsWithLimit(limit);
    }

    private List<AdminPayment> getPaymentsWithLimit(int limit) {
        List<AdminPayment> list = new ArrayList<>();
        String sql = "SELECT payment_id, reservation_id, amount, payment_method, payment_status, payment_date, card_number " +
                     "FROM payments ORDER BY payment_id DESC";
        if (limit > 0) {
            sql += " LIMIT " + limit;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String rawCard = rs.getString("card_number");
                String masked = maskCardNumber(rawCard);
                list.add(new AdminPayment(
                    rs.getInt("payment_id"),
                    rs.getString("reservation_id"),
                    rs.getDouble("amount"),
                    rs.getString("payment_method"),
                    rs.getString("payment_status"),
                    rs.getString("payment_date"),
                    masked
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return list;
    }

    private String maskCardNumber(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return null;
        }
        String digits = raw.replaceAll("\\s+", "");
        if (digits.length() <= 4) {
            return "**** " + digits;
        }
        String last4 = digits.substring(digits.length() - 4);
        return "**** **** **** " + last4;
    }

    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException ignored) {
            }
        }
        if (conn != null) {
            DBConnection.closeConnection(conn);
        }
    }
}

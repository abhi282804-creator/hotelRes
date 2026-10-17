package com.dao;

import com.bean.Reservation1Bean;
import com.util.DBConnection;
import java.sql.*;

public class Reservation1Dao {

    public int saveReservation(Reservation1Bean reservation) {
        int status = 0;
        try {
            Connection con = DBConnection.getConnection();
            String query = "INSERT INTO reservation1 (customerId, roomId, hotelId, checkin, checkout, guests, roomType, specialRequests, priceDisplay) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, reservation.getCustomerId());
            ps.setString(2, reservation.getRoomId());
            ps.setInt(3, reservation.getHotelId());
            ps.setString(4, reservation.getCheckin());
            ps.setString(5, reservation.getCheckout());
            ps.setInt(6, reservation.getGuests());
            ps.setString(7, reservation.getRoomType());
            ps.setString(8, reservation.getSpecialRequests());
            ps.setString(9, reservation.getPriceDisplay());

            status = ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int getReservationCount(int hotelId) {
        int count = 0;
        try {
            Connection con = DBConnection.getConnection();
            // ConfirmReservationDao persists confirmed bookings in reservations.
            // Count that same model so the existing four-booking rule applies
            // to confirmed bookings.
            String query = "SELECT COUNT(*) FROM reservations WHERE hotel_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, hotelId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}

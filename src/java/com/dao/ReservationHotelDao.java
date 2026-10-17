package com.dao;

import com.bean.ReservationHotel;
import com.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReservationHotelDao {

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        return DBConnection.getConnection();
    }

    public boolean saveReservation(ReservationHotel reservation) {
        String query = "INSERT INTO Reservations (hotel_id, room_id, customer_id, checkin_date, checkout_date, guests, room_type, special_requests, price_range) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reservation.getHotelId());
            stmt.setString(2, reservation.getRoomId());
            stmt.setString(3, reservation.getCustomerId());
            stmt.setString(4, reservation.getCheckinDate());
            stmt.setString(5, reservation.getCheckoutDate());
            stmt.setInt(6, reservation.getGuests());
            stmt.setString(7, reservation.getRoomType());
            stmt.setString(8, reservation.getSpecialRequests());
            stmt.setString(9, reservation.getPriceRange());

            return stmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}

package com.dao;

import com.bean.Reservationbean;
import com.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        return DBConnection.getConnection();
    }

    public List<Reservationbean> getAllReservations() throws SQLException {
        List<Reservationbean> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Reservationbean reservation = new Reservationbean();
                reservation.setReservationId(rs.getInt("id"));
                reservation.setHotelName(rs.getString("hotel_name"));
                reservation.setCheckinDate(rs.getString("checkin_date"));
                reservation.setCheckoutDate(rs.getString("checkout_date"));
                reservation.setGuests(rs.getInt("guests"));
                reservation.setRoomType(rs.getString("room_type"));
                reservations.add(reservation);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException(e.getMessage(), e);
        }
        return reservations;
    }

    public boolean cancelReservation(int reservationId) throws SQLException {
        String query = "DELETE FROM reservations WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (ClassNotFoundException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
}
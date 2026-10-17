package com.servlet.api;

import com.bean.ConfirmReservationBean;
import com.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Read/delete adapter for the reservation model already written by
 * ConfirmReservationDao. It intentionally leaves all legacy DAOs untouched.
 */
public class ApiReservationDao {
    private static final String SELECT_RESERVATIONS =
            "SELECT r.reservation_id, r.customer_id, r.hotel_id, r.room_id, r.checkin_date, "
            + "r.checkout_date, r.guests, r.room_type, r.special_requests, "
            + "COALESCE(h.hotel_name, CONCAT('Hotel ID ', r.hotel_id)) AS hotel_name "
            + "FROM reservations r "
            + "LEFT JOIN hotels h ON r.hotel_id = h.hotel_id "
            + "ORDER BY r.checkin_date DESC";

    private static final String SELECT_CUSTOMER_RESERVATIONS =
            "SELECT r.reservation_id, r.customer_id, r.hotel_id, r.room_id, r.checkin_date, "
            + "r.checkout_date, r.guests, r.room_type, r.special_requests, "
            + "COALESCE(h.hotel_name, CONCAT('Hotel ID ', r.hotel_id)) AS hotel_name "
            + "FROM reservations r "
            + "LEFT JOIN hotels h ON r.hotel_id = h.hotel_id "
            + "WHERE (r.customer_id = ? AND ? IS NOT NULL) "
            + "   OR (r.customer_id = ? AND ? IS NOT NULL) "
            + "   OR (r.customer_id = ? AND ? IS NOT NULL) "
            + "ORDER BY r.checkin_date DESC";

    private static final String SELECT_BY_ID =
            "SELECT r.reservation_id, r.customer_id, r.hotel_id, r.room_id, r.checkin_date, "
            + "r.checkout_date, r.guests, r.room_type, r.special_requests, "
            + "COALESCE(h.hotel_name, CONCAT('Hotel ID ', r.hotel_id)) AS hotel_name "
            + "FROM reservations r "
            + "LEFT JOIN hotels h ON r.hotel_id = h.hotel_id "
            + "WHERE r.reservation_id = ?";

    private static final String DELETE_RESERVATION =
            "DELETE FROM reservations WHERE reservation_id = ?";

    private static final String DELETE_CUSTOMER_RESERVATION =
            "DELETE FROM reservations WHERE reservation_id = ? "
            + "AND ((customer_id = ? AND ? IS NOT NULL) "
            + "  OR (customer_id = ? AND ? IS NOT NULL) "
            + "  OR (customer_id = ? AND ? IS NOT NULL))";

    public List<ConfirmReservationBean> getAllReservations()
            throws ClassNotFoundException, SQLException {
        List<ConfirmReservationBean> reservations = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_RESERVATIONS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                reservations.add(mapRow(resultSet));
            }
        }
        return reservations;
    }

    public List<ConfirmReservationBean> getReservationsForCustomer(String numericId, String legacyFormattedId, String email)
            throws ClassNotFoundException, SQLException {
        List<ConfirmReservationBean> reservations = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_CUSTOMER_RESERVATIONS)) {
            statement.setString(1, numericId);
            statement.setString(2, numericId);
            statement.setString(3, legacyFormattedId);
            statement.setString(4, legacyFormattedId);
            statement.setString(5, email);
            statement.setString(6, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    reservations.add(mapRow(resultSet));
                }
            }
        }
        return reservations;
    }

    public ConfirmReservationBean getReservationById(String reservationId)
            throws ClassNotFoundException, SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setString(1, reservationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null;
    }

    public boolean deleteByReservationId(String reservationId)
            throws ClassNotFoundException, SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_RESERVATION)) {
            statement.setString(1, reservationId);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean deleteByReservationIdAndCustomer(String reservationId, String numericId, String legacyFormattedId, String email)
            throws ClassNotFoundException, SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CUSTOMER_RESERVATION)) {
            statement.setString(1, reservationId);
            statement.setString(2, numericId);
            statement.setString(3, numericId);
            statement.setString(4, legacyFormattedId);
            statement.setString(5, legacyFormattedId);
            statement.setString(6, email);
            statement.setString(7, email);
            return statement.executeUpdate() == 1;
        }
    }

    private ConfirmReservationBean mapRow(ResultSet rs) throws SQLException {
        ConfirmReservationBean reservation = new ConfirmReservationBean();
        reservation.setReservationId(rs.getString("reservation_id"));
        reservation.setCustomerId(rs.getString("customer_id"));
        reservation.setHotelId(rs.getString("hotel_id"));
        reservation.setRoomId(rs.getString("room_id"));
        reservation.setCheckinDate(rs.getString("checkin_date"));
        reservation.setCheckoutDate(rs.getString("checkout_date"));
        reservation.setGuests(rs.getInt("guests"));
        reservation.setRoomType(rs.getString("room_type"));
        reservation.setSpecialRequests(rs.getString("special_requests"));
        reservation.setHotelName(rs.getString("hotel_name"));
        return reservation;
    }
}

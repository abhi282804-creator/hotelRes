package com.dao;

import com.bean.Processpaymentbean;
import com.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProcesspaymentDao {

    public boolean savePayment(Processpaymentbean payment) throws ClassNotFoundException {

        boolean result = false;

        String query = "INSERT INTO payments " +
                "(reservation_id, cardholder_name, card_number, expiry_date, CVV, " +
                "amount, payment_method, payment_success, payment_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, payment.getReservationId());

            stmt.setString(2, payment.getCardholderName());
            stmt.setString(3, payment.getCardNumber());
            stmt.setString(4, payment.getExpiryDate());
            stmt.setString(5, payment.getCvv());

            stmt.setDouble(6, payment.getAmount());

            stmt.setString(7, payment.getPaymentMethod());
            stmt.setBoolean(8, payment.isPaymentSuccess());

            String status = payment.isPaymentSuccess()
                    ? "SUCCESS"
                    : "FAILED";

            stmt.setString(9, status);

            result = stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
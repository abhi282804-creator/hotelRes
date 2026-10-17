package com.servlet.api;

import com.bean.Processpaymentbean;
import com.dao.ProcesspaymentDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/payment")
public class ApiPaymentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProcesspaymentDao paymentDao;

    @Override
    public void init() {
        paymentDao = new ProcesspaymentDao();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // =========================
        // Get basic payment details
        // =========================

        String reservationId = request.getParameter("reservationId");
        String amountStr = request.getParameter("amount");
        String paymentMethod = request.getParameter("paymentMethod");

        if (reservationId == null || reservationId.trim().isEmpty()
                || paymentMethod == null || paymentMethod.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            out.write(
                "{\"success\":false," +
                "\"message\":\"Missing reservationId or paymentMethod\"}"
            );

            return;
        }

        // =========================
        // Amount
        // =========================

        double amount = 0.0;

        try {

            if (amountStr != null && !amountStr.trim().isEmpty()) {
                amount = Double.parseDouble(amountStr.trim());
            }

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            out.write(
                "{\"success\":false," +
                "\"message\":\"Invalid payment amount\"}"
            );

            return;
        }

        // =========================
        // Payment variables
        // =========================

        boolean paymentSuccess = false;

        String cardName = null;
        String cardNumber = null;
        String expiryDate = null;
        String cvv = null;

        // =========================
        // UPI
        // =========================

        if ("UPI".equalsIgnoreCase(paymentMethod)) {

            String upiId = request.getParameter("upiId");

            if (upiId != null && !upiId.trim().isEmpty()) {
                paymentSuccess = true;
            }

        }

        // =========================
        // QR Scanner
        // =========================

        else if ("QRScanner".equalsIgnoreCase(paymentMethod)) {

            paymentSuccess = true;
        }

        // =========================
        // Debit / Credit Card
        // =========================

        else if ("DebitCard".equalsIgnoreCase(paymentMethod)
                || "CreditCard".equalsIgnoreCase(paymentMethod)) {

            cardName = request.getParameter("cardName");
            cardNumber = request.getParameter("cardNumber");
            expiryDate = request.getParameter("expiryDate");
            cvv = request.getParameter("cvv");

            if (cardName != null
                    && !cardName.trim().isEmpty()
                    && cardNumber != null
                    && cardNumber.trim().matches("\\d{16}")
                    && expiryDate != null
                    && expiryDate.trim().matches("\\d{2}/\\d{2}")
                    && cvv != null
                    && cvv.trim().matches("\\d{3}")) {

                paymentSuccess = true;
            }
        }

        // =========================
        // Unsupported payment method
        // =========================

        else {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            out.write(
                "{\"success\":false," +
                "\"message\":\"Unsupported payment method\"}"
            );

            return;
        }

        // =========================
        // Create payment bean
        // =========================

        Processpaymentbean bean = new Processpaymentbean();

        bean.setReservationId(reservationId.trim());

        bean.setCardholderName(
            cardName != null ? cardName.trim() : null
        );

        bean.setCardNumber(
            cardNumber != null ? cardNumber.trim() : null
        );

        bean.setExpiryDate(
            expiryDate != null ? expiryDate.trim() : null
        );

        bean.setCvv(
            cvv != null ? cvv.trim() : null
        );

        bean.setAmount(amount);

        bean.setPaymentMethod(paymentMethod.trim());

        bean.setPaymentSuccess(paymentSuccess);

        // =========================
        // Save payment
        // =========================

        try {

            boolean isSaved = paymentDao.savePayment(bean);

            if (isSaved && paymentSuccess) {

                out.write(
                    "{\"success\":true," +
                    "\"message\":\"Payment of Rs " + amount +
                    " for Reservation ID " +
                    escapeJson(reservationId) +
                    " was successful!\"," +
                    "\"reservationId\":\"" +
                    escapeJson(reservationId) + "\"," +
                    "\"amount\":" + amount + "," +
                    "\"paymentMethod\":\"" +
                    escapeJson(paymentMethod) +
                    "\"}"
                );

            } else {

                response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
                );

                out.write(
                    "{\"success\":false," +
                    "\"message\":\"Payment failed or validation failed. Please check inputs and try again.\"," +
                    "\"reservationId\":\"" +
                    escapeJson(reservationId) +
                    "\"}"
                );
            }

        } catch (ClassNotFoundException e) {

            e.printStackTrace();

            response.setStatus(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            out.write(
                "{\"success\":false," +
                "\"message\":\"Database class loading error\"}"
            );
        }
    }

    // =========================
    // JSON escaping
    // =========================

    private String escapeJson(String input) {

        if (input == null) {
            return "";
        }

        return input
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
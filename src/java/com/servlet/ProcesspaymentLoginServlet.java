package com.servlet;

import com.bean.Processpaymentbean;
import com.dao.ProcesspaymentDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ProcessPayment")
public class ProcesspaymentLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reservationId = request.getParameter("reservationId");
        double amount = Double.parseDouble(request.getParameter("amount"));
        String paymentMethod = request.getParameter("paymentMethod");
        boolean paymentSuccess = false;

        // Simulated payment validation (in a real app, integrate with payment gateway)
        if ("UPI".equals(paymentMethod) || "QRScanner".equals(paymentMethod)) {
            paymentSuccess = true;
        } else if ("DebitCard".equals(paymentMethod) || "CreditCard".equals(paymentMethod)) {
            paymentSuccess = true; // Add card validation if necessary
        }

        // Set payment details in the bean
        Processpaymentbean payment = new Processpaymentbean();
        payment.setReservationId(reservationId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentSuccess(paymentSuccess);

        // Save payment to the database
        ProcesspaymentDao dao = new ProcesspaymentDao();
        boolean isSaved;
        try {
            isSaved = dao.savePayment(payment);
        } catch (ClassNotFoundException e) {
            // Preserve the existing failure behavior when the JDBC driver is unavailable.
            e.printStackTrace();
            response.sendRedirect("paymentFailed.jsp");
            return;
        }

        // Redirect based on success
        if (isSaved && paymentSuccess) {
            response.sendRedirect("paymentSuccess.jsp");
        } else {
            response.sendRedirect("paymentFailed.jsp");
        }
    }
}

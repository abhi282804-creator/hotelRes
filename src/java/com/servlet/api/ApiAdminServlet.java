package com.servlet.api;

import com.dao.AdminDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/admin/*")
public class ApiAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AdminDao adminDao;

    @Override
    public void init() {
        adminDao = new AdminDao();
    }

    private boolean checkAdminAuth(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"error\":\"Unauthorized admin access\"}");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        if (!checkAdminAuth(request, response, out)) {
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            pathInfo = "/metrics";
        }

        if ("/metrics".equalsIgnoreCase(pathInfo)) {
            handleGetMetrics(response, out);
        } else if ("/reservations".equalsIgnoreCase(pathInfo)) {
            handleGetReservations(response, out);
        } else if ("/customers".equalsIgnoreCase(pathInfo)) {
            handleGetCustomers(response, out);
        } else if ("/payments".equalsIgnoreCase(pathInfo)) {
            handleGetPayments(response, out);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.write("{\"error\":\"Unknown admin endpoint\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        if (!checkAdminAuth(request, response, out)) {
            return;
        }

        String pathInfo = request.getPathInfo();
        if ("/reservations/cancel".equalsIgnoreCase(pathInfo)) {
            handleCancelReservation(request, response, out);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.write("{\"error\":\"Unknown admin action\"}");
        }
    }

    private void handleGetMetrics(HttpServletResponse response, PrintWriter out) {
        AdminDao.AdminMetrics metrics = adminDao.getMetrics();
        List<AdminDao.AdminReservation> recentReservations = adminDao.getRecentReservations(5);
        List<AdminDao.AdminPayment> recentPayments = adminDao.getRecentPayments(5);

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"success\": true,\n");
        sb.append("  \"metrics\": {\n");
        sb.append("    \"successfulPaymentAmount\": ").append(metrics.getSuccessfulPaymentAmount()).append(",\n");
        sb.append("    \"storedReservations\": ").append(metrics.getStoredReservations()).append(",\n");
        sb.append("    \"totalCustomers\": ").append(metrics.getTotalCustomers()).append(",\n");
        sb.append("    \"totalHotels\": ").append(metrics.getTotalHotels()).append(",\n");
        sb.append("    \"totalRooms\": ").append(metrics.getTotalRooms()).append("\n");
        sb.append("  },\n");

        sb.append("  \"recentReservations\": [\n");
        for (int i = 0; i < recentReservations.size(); i++) {
            sb.append("    ").append(reservationToJson(recentReservations.get(i)));
            if (i < recentReservations.size() - 1) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
        sb.append("  ],\n");

        sb.append("  \"recentPayments\": [\n");
        for (int i = 0; i < recentPayments.size(); i++) {
            sb.append("    ").append(paymentToJson(recentPayments.get(i)));
            if (i < recentPayments.size() - 1) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
        sb.append("  ]\n");
        sb.append("}");
        out.write(sb.toString());
    }

    private void handleGetReservations(HttpServletResponse response, PrintWriter out) {
        List<AdminDao.AdminReservation> list = adminDao.getAllReservations();
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append("  ").append(reservationToJson(list.get(i)));
            if (i < list.size() - 1) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
        sb.append("]");
        out.write(sb.toString());
    }

    private void handleCancelReservation(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String reservationId = request.getParameter("reservationId");
        if (reservationId == null || reservationId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\":false,\"message\":\"Reservation ID is required\"}");
            return;
        }

        boolean success = adminDao.cancelReservation(reservationId.trim());
        if (success) {
            out.write("{\"success\":true,\"message\":\"Reservation cancelled successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\":false,\"message\":\"Failed to cancel reservation\"}");
        }
    }

    private void handleGetCustomers(HttpServletResponse response, PrintWriter out) {
        List<AdminDao.AdminCustomer> list = adminDao.getAllCustomers();
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < list.size(); i++) {
            AdminDao.AdminCustomer c = list.get(i);
            sb.append("  {\n")
              .append("    \"customerId\": ").append(c.getCustomerId()).append(",\n")
              .append("    \"firstName\": \"").append(escapeJson(c.getFirstName())).append("\",\n")
              .append("    \"lastName\": \"").append(escapeJson(c.getLastName())).append("\",\n")
              .append("    \"email\": \"").append(escapeJson(c.getEmail())).append("\",\n")
              .append("    \"phone\": \"").append(escapeJson(c.getPhone())).append("\"\n")
              .append("  }");
            if (i < list.size() - 1) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
        sb.append("]");
        out.write(sb.toString());
    }

    private void handleGetPayments(HttpServletResponse response, PrintWriter out) {
        List<AdminDao.AdminPayment> list = adminDao.getAllPayments();
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append("  ").append(paymentToJson(list.get(i)));
            if (i < list.size() - 1) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
        sb.append("]");
        out.write(sb.toString());
    }

    private String reservationToJson(AdminDao.AdminReservation r) {
        return "{\n" +
               "  \"reservationId\": \"" + escapeJson(r.getReservationId()) + "\",\n" +
               "  \"customerId\": \"" + escapeJson(r.getCustomerId()) + "\",\n" +
               "  \"hotelId\": " + r.getHotelId() + ",\n" +
               "  \"hotelName\": \"" + escapeJson(r.getHotelName()) + "\",\n" +
               "  \"checkinDate\": \"" + escapeJson(r.getCheckinDate()) + "\",\n" +
               "  \"checkoutDate\": \"" + escapeJson(r.getCheckoutDate()) + "\",\n" +
               "  \"guests\": " + r.getGuests() + ",\n" +
               "  \"roomType\": \"" + escapeJson(r.getRoomType()) + "\",\n" +
               "  \"specialRequests\": \"" + escapeJson(r.getSpecialRequests() != null ? r.getSpecialRequests() : "") + "\"\n" +
               "}";
    }

    private String paymentToJson(AdminDao.AdminPayment p) {
        String card = (p.getMaskedCardNumber() != null) ? "\"" + escapeJson(p.getMaskedCardNumber()) + "\"" : "null";
        return "{\n" +
               "  \"paymentId\": " + p.getPaymentId() + ",\n" +
               "  \"reservationId\": \"" + escapeJson(p.getReservationId()) + "\",\n" +
               "  \"amount\": " + p.getAmount() + ",\n" +
               "  \"paymentMethod\": \"" + escapeJson(p.getPaymentMethod()) + "\",\n" +
               "  \"paymentStatus\": \"" + escapeJson(p.getPaymentStatus()) + "\",\n" +
               "  \"paymentDate\": \"" + escapeJson(p.getPaymentDate()) + "\",\n" +
               "  \"maskedCardNumber\": " + card + "\n" +
               "}";
    }

    private String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}

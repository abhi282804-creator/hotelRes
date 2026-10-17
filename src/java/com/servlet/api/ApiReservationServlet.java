package com.servlet.api;

import com.bean.ConfirmReservationBean;
import com.dao.ConfirmReservationDao;
import com.dao.LoginDao;
import com.dao.Reservation1Dao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@WebServlet("/api/reservations/*")
public class ApiReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ConfirmReservationDao confirmDao;
    private Reservation1Dao reservation1Dao;
    private ApiReservationDao reservationDao;
    private LoginDao loginDao;
    private Random random;

    @Override
    public void init() {
        confirmDao = new ConfirmReservationDao();
        reservation1Dao = new Reservation1Dao();
        reservationDao = new ApiReservationDao();
        loginDao = new LoginDao();
        random = new Random();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();

        if ("/availability".equalsIgnoreCase(pathInfo)) {
            handleAvailabilityCheck(request, response, out);
        } else {
            handleGetAllReservations(request, response, out);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();

        if ("/cancel".equalsIgnoreCase(pathInfo)) {
            handleCancelReservation(request, response, out);
        } else {
            handleCreateReservation(request, response, out);
        }
    }

    private void handleAvailabilityCheck(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String hotelIdStr = request.getParameter("hotelId");
        if (hotelIdStr == null || hotelIdStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Missing hotelId parameter\"}");
            return;
        }

        try {
            int hotelId = Integer.parseInt(hotelIdStr.trim());
            int count = reservation1Dao.getReservationCount(hotelId);
            boolean isFullyBooked = count >= 4;
            out.write("{\"hotelId\":" + hotelId + ",\"reservationCount\":" + count + ",\"isFullyBooked\":" + isFullyBooked + "}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Invalid hotelId format\"}");
        }
    }

    private void handleCreateReservation(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String roomId = request.getParameter("roomId");
        String hotelId = request.getParameter("hotelId");
        String checkinDate = request.getParameter("checkin");
        String checkoutDate = request.getParameter("checkout");
        String guestsStr = request.getParameter("guests");
        String roomType = request.getParameter("roomType");
        String specialRequests = request.getParameter("specialRequests");

        // Obtain customer identity authoritatively from authenticated session if logged in
        HttpSession session = request.getSession(false);
        String customerId = null;
        if (session != null && session.getAttribute("user") != null) {
            String userEmail = (String) session.getAttribute("user");
            Integer custId = (Integer) session.getAttribute("customerId");
            String legacyId = (String) session.getAttribute("customerLegacyId");
            if (legacyId != null) {
                customerId = legacyId;
            } else if (custId != null) {
                customerId = String.format("C%05d", custId);
            } else if (userEmail != null) {
                LoginDao.CustomerRecord cust = loginDao.getCustomerByEmail(userEmail);
                if (cust != null) {
                    customerId = String.format("C%05d", cust.getCustomerId());
                    session.setAttribute("customerId", cust.getCustomerId());
                    session.setAttribute("customerLegacyId", customerId);
                }
            }
        }

        if (customerId == null || customerId.trim().isEmpty()) {
            customerId = request.getParameter("customerId");
            if (customerId == null || customerId.trim().isEmpty()) {
                customerId = "C" + String.format("%05d", random.nextInt(99999) + 1);
            }
        }

        if (roomId == null || roomId.trim().isEmpty()) {
            roomId = String.format("%03d", random.nextInt(300) + 1);
        }

        if (hotelId == null || checkinDate == null || checkoutDate == null || guestsStr == null || roomType == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\":false,\"message\":\"Missing required reservation parameters\"}");
            return;
        }

        int guests = 1;
        try {
            guests = Integer.parseInt(guestsStr.trim());
        } catch (NumberFormatException ignored) {}

        String reservationId = "RES" + customerId + roomId;

        ConfirmReservationBean bean = new ConfirmReservationBean();
        bean.setReservationId(reservationId);
        bean.setCustomerId(customerId);
        bean.setHotelId(hotelId);
        bean.setRoomId(roomId);
        bean.setCheckinDate(checkinDate);
        bean.setCheckoutDate(checkoutDate);
        bean.setGuests(guests);
        bean.setRoomType(roomType);
        bean.setSpecialRequests(specialRequests != null ? specialRequests : "");

        boolean isSaved = confirmDao.saveReservation(bean);

        if (isSaved) {
            out.write("{\"success\":true,\"message\":\"Reservation created successfully\"," +
                    "\"reservationId\":\"" + escapeJson(reservationId) + "\"," +
                    "\"customerId\":\"" + escapeJson(customerId) + "\"," +
                    "\"roomId\":\"" + escapeJson(roomId) + "\"," +
                    "\"hotelId\":\"" + escapeJson(hotelId) + "\"," +
                    "\"checkinDate\":\"" + escapeJson(checkinDate) + "\"," +
                    "\"checkoutDate\":\"" + escapeJson(checkoutDate) + "\"," +
                    "\"guests\":" + guests + "," +
                    "\"roomType\":\"" + escapeJson(roomType) + "\"," +
                    "\"specialRequests\":\"" + escapeJson(specialRequests != null ? specialRequests : "") + "\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\":false,\"message\":\"Database error: Unable to save reservation\"}");
        }
    }

    private void handleGetAllReservations(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"error\":\"Unauthorized: Customer authentication required to view reservations\"}");
            return;
        }

        String userEmail = (String) session.getAttribute("user");
        Integer custId = (Integer) session.getAttribute("customerId");
        String legacyId = (String) session.getAttribute("customerLegacyId");
        if (custId == null && userEmail != null) {
            LoginDao.CustomerRecord cust = loginDao.getCustomerByEmail(userEmail);
            if (cust != null) {
                custId = cust.getCustomerId();
                legacyId = String.format("C%05d", custId);
                session.setAttribute("customerId", custId);
                session.setAttribute("customerLegacyId", legacyId);
            }
        }

        try {
            List<ConfirmReservationBean> list = reservationDao.getReservationsForCustomer(
                custId != null ? String.valueOf(custId) : null,
                legacyId,
                userEmail
            );
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                ConfirmReservationBean r = list.get(i);
                String hotelName = (r.getHotelName() != null && !r.getHotelName().isEmpty())
                    ? r.getHotelName()
                    : ("Hotel ID " + r.getHotelId());
                sb.append("{")
                  .append("\"reservationId\":\"").append(escapeJson(r.getReservationId())).append("\",")
                  .append("\"hotelName\":\"").append(escapeJson(hotelName)).append("\",")
                  .append("\"checkinDate\":\"").append(escapeJson(r.getCheckinDate())).append("\",")
                  .append("\"checkoutDate\":\"").append(escapeJson(r.getCheckoutDate())).append("\",")
                  .append("\"guests\":").append(r.getGuests()).append(",")
                  .append("\"roomType\":\"").append(escapeJson(r.getRoomType())).append("\"")
                  .append("}");
                if (i < list.size() - 1) sb.append(",");
            }
            sb.append("]");
            out.write(sb.toString());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\":false,\"message\":\"Unable to load reservations\"}");
        }
    }

    private void handleCancelReservation(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"success\":false,\"message\":\"Unauthorized: Please sign in to cancel reservations\"}");
            return;
        }

        String resIdStr = request.getParameter("reservationId");
        if (resIdStr == null || resIdStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\":false,\"message\":\"Missing reservationId\"}");
            return;
        }

        String userEmail = (String) session.getAttribute("user");
        Integer custId = (Integer) session.getAttribute("customerId");
        String legacyId = (String) session.getAttribute("customerLegacyId");
        if (custId == null && userEmail != null) {
            LoginDao.CustomerRecord cust = loginDao.getCustomerByEmail(userEmail);
            if (cust != null) {
                custId = cust.getCustomerId();
                legacyId = String.format("C%05d", custId);
                session.setAttribute("customerId", custId);
                session.setAttribute("customerLegacyId", legacyId);
            }
        }

        try {
            boolean success = reservationDao.deleteByReservationIdAndCustomer(
                resIdStr.trim(),
                custId != null ? String.valueOf(custId) : null,
                legacyId,
                userEmail
            );
            if (success) {
                out.write("{\"success\":true,\"message\":\"Reservation ID " + escapeJson(resIdStr) + " successfully canceled.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"success\":false,\"message\":\"Reservation was not found or you are not authorized to cancel it.\"}");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\":false,\"message\":\"Database error while canceling reservation\"}");
        }
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}

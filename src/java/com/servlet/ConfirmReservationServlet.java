package com.servlet;

import com.bean.ConfirmReservationBean;
import com.dao.ConfirmReservationDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/confirmReservation")
public class ConfirmReservationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String reservationId = request.getParameter("reservationId");
        String customerId = request.getParameter("customerId");
        String hotelId = request.getParameter("hotelId");
        String roomId = request.getParameter("roomId");
        String checkinDate = request.getParameter("checkin");
        String checkoutDate = request.getParameter("checkout");
        int guests = Integer.parseInt(request.getParameter("guests"));
        String roomType = request.getParameter("roomType");
        String specialRequests = request.getParameter("specialRequests");

        // Set reservation details in the bean
        ConfirmReservationBean reservation = new ConfirmReservationBean();
        reservation.setReservationId(reservationId);
        reservation.setCustomerId(customerId);
        reservation.setHotelId(hotelId);
        reservation.setRoomId(roomId);
        reservation.setCheckinDate(checkinDate);
        reservation.setCheckoutDate(checkoutDate);
        reservation.setGuests(guests);
        reservation.setRoomType(roomType);
        reservation.setSpecialRequests(specialRequests);

        // Save reservation using DAO
        ConfirmReservationDao dao = new ConfirmReservationDao();
        boolean isSaved = dao.saveReservation(reservation);

        // Redirect based on success or failure
        if (isSaved) {
            response.sendRedirect("confirmReservation.jsp");
        } else {
            response.getWriter().println("Error saving reservation.");
        }
    }
}
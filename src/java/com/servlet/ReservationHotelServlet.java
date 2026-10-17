package com.servlet;

import com.bean.ReservationHotel;
import com.dao.ReservationHotelDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ReservationHotelServlet")
public class ReservationHotelServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int hotelId = Integer.parseInt(request.getParameter("hotelId"));
        String roomId = request.getParameter("roomId");
        String customerId = request.getParameter("customerId");
        String checkinDate = request.getParameter("checkin");
        String checkoutDate = request.getParameter("checkout");
        int guests = Integer.parseInt(request.getParameter("guests"));
        String roomType = request.getParameter("roomType");
        String specialRequests = request.getParameter("specialRequests");
        String priceRange = request.getParameter("priceDisplay");

        // Create a ReservationHotel object
        ReservationHotel reservation = new ReservationHotel();
        reservation.setHotelId(hotelId);
        reservation.setRoomId(roomId);
        reservation.setCustomerId(customerId);
        reservation.setCheckinDate(checkinDate);
        reservation.setCheckoutDate(checkoutDate);
        reservation.setGuests(guests);
        reservation.setRoomType(roomType);
        reservation.setSpecialRequests(specialRequests);
        reservation.setPriceRange(priceRange);

        // Use DAO to save the reservation
        ReservationHotelDao dao = new ReservationHotelDao();
        boolean success = dao.saveReservation(reservation);

        if (success) {
            response.getWriter().println("Reservation successful!");
        } else {
            response.getWriter().println("Reservation failed.");
        }
    }
}

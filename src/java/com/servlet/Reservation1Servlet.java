package com.servlet;

import com.bean.Reservation1Bean;
import com.dao.Reservation1Dao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reservation1Servlet")
public class Reservation1Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerId = request.getParameter("customerId");
        String roomId = request.getParameter("roomId");
        int hotelId = Integer.parseInt(request.getParameter("hotelId"));
        String checkin = request.getParameter("checkin");
        String checkout = request.getParameter("checkout");
        int guests = Integer.parseInt(request.getParameter("guests"));
        String roomType = request.getParameter("roomType");
        String specialRequests = request.getParameter("specialRequests");
        String priceDisplay = request.getParameter("priceDisplay");

        Reservation1Bean reservation = new Reservation1Bean();
        reservation.setCustomerId(customerId);
        reservation.setRoomId(roomId);
        reservation.setHotelId(hotelId);
        reservation.setCheckin(checkin);
        reservation.setCheckout(checkout);
        reservation.setGuests(guests);
        reservation.setRoomType(roomType);
        reservation.setSpecialRequests(specialRequests);
        reservation.setPriceDisplay(priceDisplay);

        Reservation1Dao dao = new Reservation1Dao();
        int status = dao.saveReservation(reservation);

        if (status > 0) {
            response.sendRedirect("success.jsp");
        } else {
            response.sendRedirect("error.jsp");
        }
    }
}

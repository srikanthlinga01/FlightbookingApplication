package com.Flights;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.User.Model.*;
import com.Flights.model.*;

@WebServlet("/MyBookingsServlet")
public class MyBookingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    String url = "jdbc:mysql://localhost:3306/flightbooking";
    String db_username = "root";
    String db_password = "root";

    // Load the JDBC driver once when the servlet loads
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        GetUser user = (GetUser) session.getAttribute("loginsuccess");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Bookings> bookingList = new ArrayList<>();
        String error = null;

        try (
            Connection con = DriverManager.getConnection(url, db_username, db_password);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bookings WHERE user_id=?");
        ) {
            ps.setInt(1, user.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bookings booking = new Bookings();
                    booking.setBookingid(rs.getInt("id"));
                    booking.setUserId(rs.getInt("user_id"));
                    booking.setFlightSource(rs.getString("flight_source"));
                    booking.setDestination(rs.getString("destination"));
                    booking.setFlightNumber(rs.getString("flight_number"));
                    booking.setAirline(rs.getString("airline"));
                    booking.setFlightDate(rs.getDate("flight_date"));
                    booking.setBookingDate(rs.getDate("booking_date"));
                    bookingList.add(booking);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "Could not load bookings. Please try again later.";
        }

        request.setAttribute("bookinglist", bookingList);
        if (error != null) {
            request.setAttribute("error", error);
        }
        RequestDispatcher rd = request.getRequestDispatcher("UserBookings.jsp");
        rd.forward(request, response);
    }
}

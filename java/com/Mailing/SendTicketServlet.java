package com.Mailing;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.User.Model.GetUser;
import com.Flights.model.Bookings;

@WebServlet("/SendTicketServlet")
public class SendTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    String url = "jdbc:mysql://localhost:3306/flightbooking";
    String db_username = "root";
    String db_password = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        GetUser user = (GetUser) session.getAttribute("loginsuccess");
        if (user == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String bookingIdStr = request.getParameter("bookingId");
        String email = request.getParameter("email");
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

        // Use session attributes for redirect-based alerts
        if (bookingIdStr == null || bookingIdStr.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            session.setAttribute("sendTicketError", "Please enter an email address.");
            response.sendRedirect("UserBookings.jsp");
            return;
        }
        if (!email.matches(emailRegex)) {
            session.setAttribute("sendTicketError", "Not a valid email. Please provide another email.");
            response.sendRedirect("UserBookings.jsp");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdStr);

            Bookings booking = null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(url, db_username, db_password)) {
                String sql = "SELECT * FROM bookings WHERE id = ? AND user_id = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, bookingId);
                ps.setInt(2, user.getId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    booking = new Bookings();
                    booking.setBookingid(rs.getInt("id"));
                    booking.setFlightSource(rs.getString("flight_source"));
                    booking.setDestination(rs.getString("destination"));
                    booking.setFlightNumber(rs.getString("flight_number"));
                    booking.setAirline(rs.getString("airline"));
                    booking.setFlightDate(rs.getDate("flight_date"));
                    booking.setBookingDate(rs.getDate("booking_date"));
                }
                rs.close();
                ps.close();
            }

            if (booking == null) {
                session.setAttribute("sendTicketError", "Booking not found.");
            } else {
                String subject = "Your Flight Ticket";
                StringBuilder msg = new StringBuilder();
                msg.append("<h3>Your Flight Ticket</h3>");
                msg.append("<p><strong>Booking ID:</strong> ").append(booking.getBookingid()).append("<br>");
                msg.append("<strong>From:</strong> ").append(booking.getFlightSource()).append("<br>");
                msg.append("<strong>To:</strong> ").append(booking.getDestination()).append("<br>");
                msg.append("<strong>Flight Number:</strong> ").append(booking.getFlightNumber()).append("<br>");
                msg.append("<strong>Airline:</strong> ").append(booking.getAirline()).append("<br>");
                msg.append("<strong>Flight Date:</strong> ").append(booking.getFlightDate()).append("<br>");
                msg.append("<strong>Booking Date:</strong> ").append(booking.getBookingDate()).append("</p>");
                msg.append("<p>Thank you for booking with us!</p>");
                boolean emailSent = false;
                try {
                    emailSent = EmailUtility.sendEmail(
                        email,
                        subject,
                        msg.toString(),
                        "srikanth.linga01@gmail.com", // your sender email
                        "skdvszvohgxakwal" // your app password
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (emailSent) {
                    session.setAttribute("sendTicketSuccess", "Email sent successfully!");
                } else {
                    session.setAttribute("sendTicketError", "Email was not sent to your email address. Try again later.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("sendTicketError", "An error occurred while sending your ticket.");
        }

        response.sendRedirect("UserBookings.jsp");
    }
}

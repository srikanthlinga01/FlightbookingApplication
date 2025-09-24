package com.Flights;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;
import com.User.Model.*;
import com.sun.net.httpserver.HttpServer;
import com.Flights.model.*;
import com.Mailing.EmailUtility;

@WebServlet("/BookFlightServlet")
public abstract class BookingFlightServlet extends HttpServer {
    

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

        // --- EMAIL FORMAT VALIDATION ---
        String userEmail = user.getEmail();
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        if (userEmail == null || !userEmail.matches(emailRegex)) {
            request.setAttribute("confirmationMessage", "Booking failed: Your email address is not valid. Please update your profile with a valid email.");
            RequestDispatcher rd = request.getRequestDispatcher("confirmation.jsp");
            rd.forward(request, response);
            return;
        }

        String tripType = request.getParameter("tripType");
        java.sql.Date bookingDate = new java.sql.Date(System.currentTimeMillis());

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, db_username, db_password);

            int outboundBookingId = 0;
            int returnBookingId = 0;

            String sql = "INSERT INTO bookings(user_id,flight_source,destination,flight_number,airline,flight_date,booking_date) VALUES(?,?,?,?,?,?,?)";

            if ("roundtrip".equalsIgnoreCase(tripType)) {
                String[] outbound = request.getParameter("outbound").split("\\|");
                String[] ret = request.getParameter("return").split("\\|");

                // Outbound booking
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, user.getId());
                ps.setString(2, outbound[0]);
                ps.setString(3, outbound[1]);
                ps.setString(4, outbound[2]);
                ps.setString(5, outbound[3]);
                ps.setDate(6, java.sql.Date.valueOf(outbound[4]));
                ps.setDate(7, bookingDate);
                int count = ps.executeUpdate();
                if (count > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) outboundBookingId = rs.getInt(1);
                    rs.close();
                }
                ps.close();

                // Return booking
                ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, user.getId());
                ps.setString(2, ret[0]);
                ps.setString(3, ret[1]);
                ps.setString(4, ret[2]);
                ps.setString(5, ret[3]);
                ps.setDate(6, java.sql.Date.valueOf(ret[4]));
                ps.setDate(7, bookingDate);
                int returnCount = ps.executeUpdate();
                if (returnCount > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) returnBookingId = rs.getInt(1);
                    rs.close();
                }
                ps.close();

            } else {
                // One-way
                String flightSource = request.getParameter("flightSource");
                String destination = request.getParameter("destination");
                String flightNumber = request.getParameter("flightNumber");
                String airline = request.getParameter("airline");
                String flightDate = request.getParameter("flightDate");
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, user.getId());
                ps.setString(2, flightSource);
                ps.setString(3, destination);
                ps.setString(4, flightNumber);
                ps.setString(5, airline);
                ps.setDate(6, java.sql.Date.valueOf(flightDate));
                ps.setDate(7, bookingDate);
                int count = ps.executeUpdate();
                if (count > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) outboundBookingId = rs.getInt(1);
                    rs.close();
                }
                ps.close();
            }

            // Fetch and display confirmation
            Bookings outboundBooking = null;
            Bookings returnBooking = null;

            if (outboundBookingId > 0) {
                outboundBooking = new Bookings();
                String selectSql = "SELECT * FROM bookings WHERE id = ?";
                PreparedStatement ps = con.prepareStatement(selectSql);
                ps.setInt(1, outboundBookingId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    outboundBooking.setBookingid(rs.getInt("id"));
                    outboundBooking.setUserId(rs.getInt("user_id"));
                    outboundBooking.setFlightSource(rs.getString("flight_source"));
                    outboundBooking.setDestination(rs.getString("destination"));
                    outboundBooking.setFlightNumber(rs.getString("flight_number"));
                    outboundBooking.setAirline(rs.getString("airline"));
                    outboundBooking.setFlightDate(rs.getDate("flight_date"));
                    outboundBooking.setBookingDate(rs.getDate("booking_date"));
                }
                rs.close();
                ps.close();
            }

            if ("roundtrip".equalsIgnoreCase(tripType) && returnBookingId > 0) {
                returnBooking = new Bookings();
                String selectSql = "SELECT * FROM bookings WHERE id = ?";
                PreparedStatement ps = con.prepareStatement(selectSql);
                ps.setInt(1, returnBookingId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    returnBooking.setBookingid(rs.getInt("id"));
                    returnBooking.setUserId(rs.getInt("user_id"));
                    returnBooking.setFlightSource(rs.getString("flight_source"));
                    returnBooking.setDestination(rs.getString("destination"));
                    returnBooking.setFlightNumber(rs.getString("flight_number"));
                    returnBooking.setAirline(rs.getString("airline"));
                    returnBooking.setFlightDate(rs.getDate("flight_date"));
                    returnBooking.setBookingDate(rs.getDate("booking_date"));
                }
                rs.close();
                ps.close();
            }

            // Email sending
            String from = "srikanth.linga01@gmail.com"; // Your Gmail
            String appPassword = "skdvszvohgxakwal"; // Your Gmail App Password

            String subject = "Flight Booking Confirmation";
            StringBuilder messageText = new StringBuilder();
            messageText.append("<h3>Your flight booking is confirmed!</h3>");
            if (outboundBooking != null) {
                messageText.append("<p><strong>Outbound Flight Details:</strong><br>");
                messageText.append("From: ").append(outboundBooking.getFlightSource()).append("<br>");
                messageText.append("To: ").append(outboundBooking.getDestination()).append("<br>");
                messageText.append("Flight Number: ").append(outboundBooking.getFlightNumber()).append("<br>");
                messageText.append("Airline: ").append(outboundBooking.getAirline()).append("<br>");
                messageText.append("Date: ").append(outboundBooking.getFlightDate()).append("</p>");
            }
            if ("roundtrip".equalsIgnoreCase(tripType) && returnBooking != null) {
                messageText.append("<p><strong>Return Flight details:</strong><br>");
                messageText.append("From: ").append(returnBooking.getFlightSource()).append("<br>");
                messageText.append("To: ").append(returnBooking.getDestination()).append("<br>");
                messageText.append("Flight Number: ").append(returnBooking.getFlightNumber()).append("<br>");
                messageText.append("Airline: ").append(returnBooking.getAirline()).append("<br>");
                messageText.append("Date: ").append(returnBooking.getFlightDate()).append("</p>");
            }
            messageText.append("<p>Thank you for booking with us!</p>");

            boolean emailSent = false;
            try {
                emailSent = EmailUtility.sendEmail(userEmail, subject, messageText.toString(), from, appPassword);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String confirmationMessage = "Booking successful! ";
            if (emailSent) {
                confirmationMessage += "A confirmation email has been sent to your registered email address.";
            } else {
                confirmationMessage += "However, we couldn't send a confirmation email at this time.";
            }

            request.setAttribute("confirmationMessage", confirmationMessage);

            // Forward to confirmation page
            RequestDispatcher rd = request.getRequestDispatcher("confirmation.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("confirmationMessage", "Booking failed due to a server error. Please try again.");
            RequestDispatcher rd = request.getRequestDispatcher("confirmation.jsp");
            rd.forward(request, response);
        }
    }

}

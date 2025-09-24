package com.Flights;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.time.LocalDate;
import java.sql.Date;

import com.User.Model.GetUser;
import com.Mailing.EmailUtility;

@WebServlet("/RescheduleBookingServlet")
public class RescheduleBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    String url = "jdbc:mysql://localhost:3306/flightbooking";
    String db_username = "root";
    String db_password = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        GetUser user = (GetUser) session.getAttribute("loginsuccess");
        if (user == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String bookingId = request.getParameter("bookingId");
        String newDateStr = request.getParameter("newDate");
        String confirmationMessage = "";

        // Validate input presence
        if (bookingId == null || bookingId.trim().isEmpty() ||
            newDateStr == null || newDateStr.trim().isEmpty()) {
            confirmationMessage = "Please select a new date.";
            request.setAttribute("confirmationMessage", confirmationMessage);
            request.getRequestDispatcher("rescheduleConfirmation.jsp").forward(request, response);
            return;
        }

        try {
            int bookingIdInt = Integer.parseInt(bookingId);
            LocalDate newDate = LocalDate.parse(newDateStr);
            LocalDate today = LocalDate.now();

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, db_username, db_password);

            // Fetch original flight and booking details
            String sql = "SELECT flight_date, flight_source, destination, flight_number, airline FROM bookings WHERE id = ? AND user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingIdInt);
            ps.setInt(2, user.getId());
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                confirmationMessage = "Booking not found.";
                request.setAttribute("confirmationMessage", confirmationMessage);
                request.getRequestDispatcher("rescheduleConfirmation.jsp").forward(request, response);
                return;
            }

            LocalDate flightDate = rs.getDate("flight_date").toLocalDate();
            String flightSource = rs.getString("flight_source");
            String destination = rs.getString("destination");
            String flightNumber = rs.getString("flight_number");
            String airline = rs.getString("airline");

            // Backend validation
            if (newDate.isBefore(today)) {
                confirmationMessage = "New date cannot be before today.";
                request.setAttribute("confirmationMessage", confirmationMessage);
                request.getRequestDispatcher("confirmation.jsp").forward(request, response);
                return;
            }
            if (!newDate.isBefore(flightDate)) {
                confirmationMessage = "New date must be before the original flight date (" + flightDate + ").";
                request.setAttribute("confirmationMessage", confirmationMessage);
                request.getRequestDispatcher("confirmation.jsp").forward(request, response);
                return;
            }

            // Proceed with update
            sql = "UPDATE bookings SET flight_date = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(newDate));
            ps.setInt(2, bookingIdInt);

            int count = ps.executeUpdate();

            if (count > 0) {
                // Email validation
                String userEmail = user.getEmail();
                String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
                boolean validEmail = userEmail != null && userEmail.matches(emailRegex);

                String from = "srikanth.linga01@gmail.com"; // Your Gmail
                String appPassword = "skdvszvohgxakwal"; // Your Gmail App Password

                String subject = "Flight Booking Reschedule Confirmation";
                StringBuilder messageText = new StringBuilder();
                messageText.append("<h3>Your flight booking has been rescheduled!</h3>");
                messageText.append("<p><strong>Booking Details:</strong><br>");
                messageText.append("From: ").append(flightSource).append("<br>");
                messageText.append("To: ").append(destination).append("<br>");
                messageText.append("Flight Number: ").append(flightNumber).append("<br>");
                messageText.append("Airline: ").append(airline).append("<br>");
                messageText.append("New Flight Date: ").append(newDate).append("</p>");
                messageText.append("<p>Thank you for using our service!</p>");

                confirmationMessage = "Booking rescheduled successfully. ";

                if (!validEmail) {
                    confirmationMessage += "However, your email address is not valid. Please update your profile to receive reschedule emails.";
                } else {
                    boolean emailSent = false;
                    try {
                        emailSent = EmailUtility.sendEmail(userEmail, subject, messageText.toString(), from, appPassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (emailSent) {
                        confirmationMessage += "A confirmation email has been sent to your registered email address.";
                    } else {
                        confirmationMessage += "However, we couldn't send a confirmation email at this time.";
                    }
                }
            } else {
                confirmationMessage = "Failed to reschedule booking.";
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            confirmationMessage = "An error occurred while rescheduling.";
        }

        // Always forward to confirmation page with message
        request.setAttribute("confirmationMessage", confirmationMessage);
        request.getRequestDispatcher("confirmation.jsp").forward(request, response);
    }
}

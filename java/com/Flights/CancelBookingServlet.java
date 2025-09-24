package com.Flights;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.time.LocalDate;

import com.User.Model.GetUser;
import com.Mailing.EmailUtility;

@WebServlet("/CancelBookingServlet")
public class CancelBookingServlet extends HttpServlet {
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
        String confirmationMessage = "";

        if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
            confirmationMessage = "Invalid booking ID.";
            request.setAttribute("confirmationMessage", confirmationMessage);
            request.getRequestDispatcher("confirmation.jsp").forward(request, response);
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdStr);

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, db_username, db_password);

            // Fetch flight date to check if cancellation is allowed
            String sql = "SELECT flight_date FROM bookings WHERE id = ? AND user_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, bookingId);
            ps.setInt(2, user.getId());
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                confirmationMessage = "Booking not found.";
                request.setAttribute("confirmationMessage", confirmationMessage);
                rs.close();
                ps.close();
                con.close();
                request.getRequestDispatcher("confirmation.jsp").forward(request, response);
                return;
            }

            LocalDate flightDate = rs.getDate("flight_date").toLocalDate();
            LocalDate today = LocalDate.now();

            rs.close();
            ps.close();

            // Only allow cancellation if cancellation day is before the flight date
            if (!today.isBefore(flightDate)) {
                confirmationMessage = "You can only cancel bookings before the flight date.";
                request.setAttribute("confirmationMessage", confirmationMessage);
                con.close();
                request.getRequestDispatcher("confirmation.jsp").forward(request, response);
                return;
            }

            // Proceed with cancellation
            sql = "DELETE FROM bookings WHERE id = ? AND user_id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, bookingId);
            ps.setInt(2, user.getId());
            int count = ps.executeUpdate();
            ps.close();

            if (count > 0) {
                // Booking cancelled, now send email

                String userEmail = user.getEmail();
                String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
                boolean validEmail = userEmail != null && userEmail.matches(emailRegex);

                String from = "srikanth.linga01@gmail.com"; // Your Gmail
                String appPassword = "skdvszvohgxakwal"; // Your Gmail App Password
                String subject = "Flight Booking Cancellation Confirmation";
                String messageText = "<h3>Your flight booking has been cancelled successfully.</h3>"
                                   + "<p>If you have any questions, please contact our support team.</p>";

                if (!validEmail) {
                    confirmationMessage = "Your booking was cancelled, but your email address is not valid. Please update your profile to receive cancellation emails.";
                } else {
                    boolean emailSent = false;
                    try {
                        emailSent = EmailUtility.sendEmail(userEmail, subject, messageText, from, appPassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (emailSent) {
                        confirmationMessage = "Your booking was cancelled. A confirmation email has been sent to your registered email address.";
                    } else {
                        confirmationMessage = "Your booking was cancelled, but we couldn't send a confirmation email at this time.";
                    }
                }
            } else {
                confirmationMessage = "Your booking could not be cancelled. Please try again.";
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            confirmationMessage = "An error occurred while cancelling your booking.";
        }

        // Always forward to confirmation page with message
        request.setAttribute("confirmationMessage", confirmationMessage);
        request.getRequestDispatcher("confirmation.jsp").forward(request, response);
    }
}

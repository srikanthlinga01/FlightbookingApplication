<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.sql.*,com.Flights.model.Bookings,com.User.Model.GetUser" %>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);

GetUser user = (GetUser) session.getAttribute("loginsuccess");
List<Bookings> bookingList = new ArrayList<>();
if (user != null) {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/flightbooking", "root", "root");
        String sql = "SELECT * FROM bookings WHERE user_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, user.getId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Bookings booking = new Bookings();
            booking.setBookingid(rs.getInt("id"));
            booking.setFlightSource(rs.getString("flight_source"));
            booking.setDestination(rs.getString("destination"));
            booking.setFlightNumber(rs.getString("flight_number"));
            booking.setAirline(rs.getString("airline"));
            booking.setFlightDate(rs.getDate("flight_date"));
            booking.setBookingDate(rs.getDate("booking_date"));
            bookingList.add(booking);
        }
        rs.close();
        ps.close();
        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Bookings - FlightBooking</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body { background-color: #f8f9fa; }
        .main-bg { background: #fff; border-radius: 0; box-shadow: none; padding: 32px 16px 32px 16px; }
        .table thead th {
            background: #1976d2;
            color: #fff;
            font-weight: 600;
            font-size: 1rem;
        }
        .table td, .table th { vertical-align: middle; }
        .action-btns {
            display: flex;
            gap: 0.5rem;
            justify-content: center;
            flex-wrap: wrap;
        }
        .action-btns .btn {
            min-width: 95px;
            font-weight: 500;
            font-size: 0.95rem;
        }
        .no-bookings { margin-top: 30px; }
        .back-home-row {
            margin-top: 32px;
            display: flex;
            justify-content: center;
        }
        .back-home-btn {
            min-width: 180px;
            font-weight: bold;
            font-size: 1.08rem;
            padding: 10px 0;
        }
        .booking-title {
            font-size: 1.2rem;
            color: #1976d2;
            font-weight: 600;
            margin-bottom: 1.2rem;
            letter-spacing: 0.5px;
        }
        @media (max-width: 768px) {
            .main-bg { padding: 10px 2px 18px 2px; }
            .table-responsive { font-size: 0.97rem; }
            .action-btns .btn { min-width: 80px; font-size: 0.93rem; }
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand" href="welcome.jsp">FlightBooking</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <form action="LogoutServlet" method="post" class="d-inline">
                        <button type="submit" class="btn btn-link nav-link p-0" style="color:#fff;">Logout</button>
                    </form>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Alerts Section -->
<%
    String rescheduleSuccess = (String) session.getAttribute("rescheduleSuccess");
    String rescheduleError = (String) session.getAttribute("rescheduleError");
    if (rescheduleSuccess != null) {
%>
    <div class="alert alert-success"><%= rescheduleSuccess %></div>
<%
        session.removeAttribute("rescheduleSuccess");
    } else if (rescheduleError != null) {
%>
    <div class="alert alert-danger"><%= rescheduleError %></div>
<%
        session.removeAttribute("rescheduleError");
    }

    String cancelSuccess = (String) session.getAttribute("cancelSuccess");
    String cancelError = (String) session.getAttribute("cancelError");
    if (cancelSuccess != null) {
%>
    <div class="alert alert-success"><%= cancelSuccess %></div>
<%
        session.removeAttribute("cancelSuccess");
    } else if (cancelError != null) {
%>
    <div class="alert alert-danger"><%= cancelError %></div>
<%
        session.removeAttribute("cancelError");
    }

    // --- Send Ticket Email Alerts ---
    String sendTicketSuccess = (String) session.getAttribute("sendTicketSuccess");
    String sendTicketError = (String) session.getAttribute("sendTicketError");
    if (sendTicketSuccess != null) {
%>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <%= sendTicketSuccess %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
<%
        session.removeAttribute("sendTicketSuccess");
    } else if (sendTicketError != null) {
%>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <%= sendTicketError %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
<%
        session.removeAttribute("sendTicketError");
    }
%>

<div class="container-fluid main-bg">
    <h3 class="booking-title text-center">My Bookings</h3>
    <%
        if (bookingList != null && !bookingList.isEmpty()) {
    %>
    <div class="table-responsive">
        <table class="table table-striped table-bordered align-middle">
            <thead>
                <tr>
                    <th>Booking ID</th>
                    <th>Source</th>
                    <th>Destination</th>
                    <th>Flight Number</th>
                    <th>Airline</th>
                    <th>Flight Date</th>
                    <th>Booking Date</th>
                    <th class="text-center">Actions</th>
                </tr>
            </thead>
            <tbody>
            <%
                for (Bookings booking : bookingList) {
            %>
                <tr>
                    <td><%= booking.getBookingid() %></td>
                    <td><%= booking.getFlightSource() %></td>
                    <td><%= booking.getDestination() %></td>
                    <td><%= booking.getFlightNumber() %></td>
                    <td><%= booking.getAirline() %></td>
                    <td><%= booking.getFlightDate() %></td>
                    <td><%= booking.getBookingDate() %></td>
                    <td>
                        <div class="action-btns">
    						<%
        						java.time.LocalDate today = java.time.LocalDate.now();
        						java.time.LocalDate flightDate = booking.getFlightDate().toLocalDate();
        						boolean canReschedule = today.isBefore(flightDate);
        
        						if (canReschedule) {
            						String todayStr = today.toString();
            						String maxStr = flightDate.minusDays(1).toString();
    						%>
    					<form action="RescheduleBookingServlet" method="post" style="display:inline;">
        					<input type="hidden" name="bookingId" value="<%= booking.getBookingid() %>" />
        					New Date: <input type="date" name="newDate" required 
               								min="<%= todayStr %>" max="<%= maxStr %>">
        							<button type="submit" class="btn btn-warning btn-sm ms-1">Reschedule</button>
    					</form>
    					<%
        					} else {
    					%>
        					<button class="btn btn-secondary btn-sm ms-1" disabled>
            				Reschedule Not Allowed
        					</button>
    					<% } %>
					
                        <!-- Cancel -->
                        <%
                            boolean canCancel = today.isBefore(flightDate);
                        %>
                        <form action="CancelBookingServlet" method="post" style="display:inline;">
                            <input type="hidden" name="bookingId" value="<%= booking.getBookingid() %>" />
                            <button type="submit" class="btn btn-danger btn-sm ms-1"
                                    onclick="return confirm('Are you sure you want to cancel this booking?')"
                                <%= !canCancel ? "disabled" : "" %>>
                                    Cancel your Ticket</button>
                        </form>
                        <!-- Send Ticket Button -->
                        <button type="button" class="btn btn-outline-primary btn-sm send-ticket-btn"
                                data-bs-toggle="modal"
                                data-bs-target="#sendTicketModal"
                                data-booking-id="<%= booking.getBookingid() %>">
                            <i class="bi bi-envelope"></i>
                        </button>
                        </div>
                    </td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
    <%
        } else {
    %>
    <div class="alert alert-info text-center no-bookings">
        No Bookings Found At This Time.
    </div>
    <%
        }
    %>
    <!-- Back to Home Button -->
    <div class="back-home-row">
        <a href="FlightSearchServlet" class="btn btn-primary back-home-btn">Home</a>
    </div>
    
    <!-- Send Ticket Modal (Single, reused for all bookings) -->
    <div class="modal fade" id="sendTicketModal" tabindex="-1" aria-labelledby="sendTicketModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <form method="post" action="SendTicketServlet">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="sendTicketModalLabel">Send Ticket to Email</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              <input type="hidden" id="modalBookingId" name="bookingId" value="">
              <label for="emailInput" class="form-label">Enter Email Address:</label>
              <input type="email" class="form-control" id="emailInput" name="email" required>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
              <button type="submit" class="btn btn-primary">Send Ticket</button>
            </div>
          </div>
        </form>
      </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
document.addEventListener('DOMContentLoaded', function() {
    var sendTicketButtons = document.querySelectorAll('.send-ticket-btn');
    var modalBookingIdInput = document.getElementById('modalBookingId');
    sendTicketButtons.forEach(function(btn) {
        btn.addEventListener('click', function() {
            var bookingId = btn.getAttribute('data-booking-id');
            modalBookingIdInput.value = bookingId;
        });
    });
});
</script>
</body>
</html>

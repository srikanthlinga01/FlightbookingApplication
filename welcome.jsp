<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.User.Model.GetUser" %>
<%@ page import="java.util.List" %>
<%@ page import="com.Flights.model.Flights" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome - FlightBooking</title>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .navbar { margin-bottom: 30px; }
        .container { max-width: 1200px; }
        .card { margin-bottom: 20px; border-radius: 18px; box-shadow: 0 4px 24px rgba(0,0,0,0.08);}
        .table td, .table th { vertical-align: middle; }
        .section-title { color: #1976d2; font-weight: bold; margin-bottom: 20px; }
    </style>
    <script>
    function updateDropdowns() {
        var sourceSelect = document.getElementById("source");
        var destSelect = document.getElementById("destination");
        for (var i = 0; i < sourceSelect.options.length; i++) sourceSelect.options[i].disabled = false;
        for (var i = 0; i < destSelect.options.length; i++) destSelect.options[i].disabled = false;
        var selectedSource = sourceSelect.value;
        var selectedDest = destSelect.value;
        if (selectedSource) {
            for (var i = 0; i < destSelect.options.length; i++) {
                if (destSelect.options[i].value === selectedSource) destSelect.options[i].disabled = true;
            }
        }
        if (selectedDest) {
            for (var i = 0; i < sourceSelect.options.length; i++) {
                if (sourceSelect.options[i].value === selectedDest) sourceSelect.options[i].disabled = true;
            }
        }
        updateReturnRoute();
    }
    function toggleRoundTrip() {
        var isRoundTrip = document.getElementById('roundTrip').checked;
        document.getElementById('returnSection').style.display = isRoundTrip ? 'block' : 'none';
        updateReturnRoute();
    }
    function updateReturnRoute() {
        var isRoundTrip = document.getElementById('roundTrip').checked;
        if (isRoundTrip) {
            var source = document.getElementById('source').value;
            var dest = document.getElementById('destination').value;
            document.getElementById('returnSource').value = dest;
            document.getElementById('returnDestination').value = source;
        }
    }
    window.onload = function() {
        updateDropdowns();
        toggleRoundTrip();
        document.getElementById('source').addEventListener('change', updateDropdowns);
        document.getElementById('destination').addEventListener('change', updateDropdowns);
        document.getElementById('oneWay').addEventListener('change', toggleRoundTrip);
        document.getElementById('roundTrip').addEventListener('change', toggleRoundTrip);
    };
    </script>
</head>
<body>
<%
    String searchError = (String) request.getAttribute("searchError");
    if (searchError != null) {
%>
    <div class="alert alert-danger"><%= searchError %></div>
<%
    }
    GetUser user = (GetUser) session.getAttribute("loginsuccess");
    String selectedFlightSource = request.getParameter("flightSource");
    String selectedDestination = request.getParameter("destination");
    String selectedFlightNumber = request.getParameter("flightNumber");
    String selectedFlightDate = request.getParameter("flightDate");
    String selectedAirline = request.getParameter("airline");
%>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center" href="welcome.jsp">
            <!-- Optional: Add a logo SVG or image here -->
            <!-- <img src="logo.png" alt="Logo" width="32" height="32" class="me-2"> -->
            FlightBooking
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto align-items-center">
    		<% if (user != null) { %>
    		<li class="nav-item me-3">
        	<form action="UserProfile.jsp" method="get" class="d-inline">
            	<button type="submit" class="profile-btn nav-link px-3">Profile</button>
        	</form>
    		</li>
    		<li class="nav-spacer"></li>
    		<li class="nav-item me-3">
        		<form action="LogoutServlet" method="post" class="d-inline">
            		<button type="submit" class="logout-btn nav-link px-3">Logout</button>
        		</form>
    		</li>
    	<% } else { %>
    	<li class="nav-item">
        	<a class="nav-link" href="index.jsp">Login</a>
    	</li>
    	<% } %>
		</ul>
        </div>
    </div>
</nav>


<div class="container bg-white p-4 rounded shadow">
<%
    if (user != null) {
%>
    <h4 class="mb-4 section-title">Welcome to Flight Booking Service</h4>
    <div class="card p-3 mb-3">
        <form action="FlightSearchServlet" method="post" class="row g-3 align-items-end">
            <!-- Trip Type Selection -->
            <div class="col-md-12 mb-2">
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="tripType" id="oneWay" value="oneway" checked>
                    <label class="form-check-label" for="oneWay">One Way</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="tripType" id="roundTrip" value="roundtrip">
                    <label class="form-check-label" for="roundTrip">Round Trip</label>
                </div>
            </div>
            <div class="col-md-3">
                <label for="source" class="form-label">Source</label>
                <select name="source" id="source" class="form-select" required>
                    <option value="">Select Source</option>
                    <%
                        @SuppressWarnings("unchecked")
                        List<String> sources = (List<String>) request.getAttribute("sources");
                        if (sources != null) {
                            for (String src : sources) {
                    %>
                        <option value="<%= src %>"><%= src %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="col-md-3">
                <label for="destination" class="form-label">Destination</label>
                <select name="destination" id="destination" class="form-select" required>
                    <option value="">Select Destination</option>
                    <%
                        @SuppressWarnings("unchecked")
                        List<String> destinations = (List<String>) request.getAttribute("destinations");
                        if (destinations != null) {
                            for (String dest : destinations) {
                    %>
                        <option value="<%= dest %>"><%= dest %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="col-md-3">
                <label for="travelDate" class="form-label">Travel Date</label>
                <%	java.time.LocalDate today = java.time.LocalDate.now();	%>
                <input type="date" class="form-control" name="travelDate" id="travelDate" min="<%= today %>" required />
            </div>
            <div class="col-md-3">
                <button type="submit" class="btn btn-primary w-100">Search</button>
            </div>
            <!-- Return Trip Section -->
            <div class="col-md-12" id="returnSection" style="display:none;">
                <div class="row g-3 align-items-end">
                    <div class="col-md-3">
                        <label for="returnDate" class="form-label">Return Date</label>
                        <input type="date" class="form-control" id="returnDate" name="returnDate">
                        <script>
                            document.addEventListener('DOMContentLoaded', function() {
                                var today = new Date().toISOString().split('T')[0];
                                document.getElementById('travelDate').setAttribute('min', today);
                                document.getElementById('travelDate').addEventListener('change', function() {
                                    var outbound = this.value;
                                    var returnInput = document.getElementById('returnDate');
                                    returnInput.setAttribute('min', outbound);
                                    if (returnInput.value && returnInput.value < outbound) {
                                        returnInput.value = outbound;
                                    }
                                });
                            });
                        </script>
                    </div>
                    <div class="col-md-3">
                        <label for="returnSource" class="form-label">Return Source</label>
                        <input type="text" class="form-control" id="returnSource" name="returnSource" readonly>
                    </div>
                    <div class="col-md-3">
                        <label for="returnDestination" class="form-label">Return Destination</label>
                        <input type="text" class="form-control" id="returnDestination" name="returnDestination" readonly>
                    </div>
                </div>
            </div>
        </form>
    </div>
    
    <%-- Flight Results Section --%>
    <%
        String tripType = (String) request.getAttribute("tripType");
        if ("roundtrip".equalsIgnoreCase(tripType)) {
            @SuppressWarnings("unchecked")
            List<Flights> outboundFlights = (List<Flights>) request.getAttribute("outboundFlights");
            @SuppressWarnings("unchecked")
            List<Flights> returnFlights = (List<Flights>) request.getAttribute("returnFlights");
            if (outboundFlights != null && returnFlights != null && !outboundFlights.isEmpty() && !returnFlights.isEmpty()) {
    %>
    <form id="roundTripBookingForm" action="BookFlightServlet" method="post">
        <input type="hidden" name="tripType" value="roundtrip" />
        <h5>Available Flights</h5>
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>Select</th>
                    <th>Source</th>
                    <th>Destination</th>
                    <th>Flight Number</th>
                    <th>Airline</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
            <%
                for (Flights flight : outboundFlights) {
            %>
                <tr>
                    <td>
                        <input type="radio" name="outbound" value="<%= 
                            flight.getFlight_source() + '|' +
                            flight.getDestination() + '|' +
                            flight.getFlight_number() + '|' +
                            flight.getAirline_name() + '|' +
                            flight.getFlight_date() %>">
                    </td>
                    <td><%= flight.getFlight_source() %></td>
                    <td><%= flight.getDestination() %></td>
                    <td><%= flight.getFlight_number() %></td>
                    <td><%= flight.getAirline_name() %></td>
                    <td><%= flight.getFlight_date() %></td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
        <h5>Available Return Flights</h5>
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>Select</th>
                    <th>Source</th>
                    <th>Destination</th>
                    <th>Flight Number</th>
                    <th>Airline</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
            <%
                for (Flights flight : returnFlights) {
            %>
                <tr>
                    <td>
                        <input type="radio" name="return" value="<%= 
                            flight.getFlight_source() + '|' +
                            flight.getDestination() + '|' +
                            flight.getFlight_number() + '|' +
                            flight.getAirline_name() + '|' +
                            flight.getFlight_date() %>">
                    </td>
                    <td><%= flight.getFlight_source() %></td>
                    <td><%= flight.getDestination() %></td>
                    <td><%= flight.getFlight_number() %></td>
                    <td><%= flight.getAirline_name() %></td>
                    <td><%= flight.getFlight_date() %></td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
        <button type="submit" id="bookBtn" class="btn btn-success" disabled>Book Round Trip</button>
    </form>
    <script>
        function checkSelections() {
            var outbound = document.querySelector('input[name="outbound"]:checked');
            var ret = document.querySelector('input[name="return"]:checked');
            document.getElementById('bookBtn').disabled = !(outbound && ret);
        }
        document.querySelectorAll('input[name="outbound"]').forEach(function(el) {
            el.addEventListener('change', checkSelections);
        });
        document.querySelectorAll('input[name="return"]').forEach(function(el) {
            el.addEventListener('change', checkSelections);
        });
    </script>
    <%
            } else {
    %>
        <div class="alert alert-warning">No outbound or return flights found for the selected dates.</div>
    <%
            }
        } else {
            // One-way trip logic
            @SuppressWarnings("unchecked")
            List<Flights> flights = (List<Flights>) request.getAttribute("flights");
            if (flights != null && !flights.isEmpty()) {
    %>
    <div class="card p-3 mb-4">
        <h5>Available Flights</h5>
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>Source</th>
                    <th>Destination</th>
                    <th>Flight Number</th>
                    <th>Airline</th>
                    <th>Date</th>
                    <th>Select</th>
                </tr>
            </thead>
            <tbody>
            <%
                for (Flights flight : flights) {
            %>
                <tr>
                    <td><%= flight.getFlight_source() %></td>
                    <td><%= flight.getDestination() %></td>
                    <td><%= flight.getFlight_number() %></td>
                    <td><%= flight.getAirline_name() %></td>
                    <td><%= flight.getFlight_date() %></td>
                    <td>
                        <form action="welcome.jsp" method="post" class="d-inline">
                            <input type="hidden" name="flightSource" value="<%= flight.getFlight_source() %>" />
                            <input type="hidden" name="destination" value="<%= flight.getDestination() %>" />
                            <input type="hidden" name="flightNumber" value="<%= flight.getFlight_number() %>" />
                            <input type="hidden" name="flightDate" value="<%= flight.getFlight_date() %>"/>
                            <input type="hidden" name="airline" value="<%= flight.getAirline_name() %>" />
                            <button type="submit" class="btn btn-outline-primary btn-sm">Select</button>
                        </form>
                    </td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
    <%
            } // end if flights
        } // end one-way
    } // end if user
%>
</div>
</body>
</html>

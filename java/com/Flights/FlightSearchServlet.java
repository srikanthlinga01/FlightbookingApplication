package com.Flights;

import java.io.IOException;
import java.util.List;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;


import com.Flights.Dao.FlightsDao;
import com.Flights.model.Flights;

@WebServlet("/FlightSearchServlet")
public class FlightSearchServlet extends HttpServlet {
private static final long serialVersionUID = 1L;

protected void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
FlightsDao dao = new FlightsDao();
try {
request.setAttribute("sources", dao.getAllSources());
request.setAttribute("destinations", dao.getAllDestinations());
request.getRequestDispatcher("welcome.jsp").forward(request, response);
} catch (Exception e) {
e.printStackTrace();
}
}

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

String tripType = request.getParameter("tripType");
String source = request.getParameter("source");
String destination = request.getParameter("destination");
String travelDate = request.getParameter("travelDate");
String returnDate = request.getParameter("returnDate");

FlightsDao dao = new FlightsDao();
try {
request.setAttribute("sources", dao.getAllSources());
request.setAttribute("destinations", dao.getAllDestinations());

// ---- Date validation ----
LocalDate today = LocalDate.now();
LocalDate outbound = null;
LocalDate ret = null;
boolean hasError = false;
String errorMsg = "";

try {
outbound = LocalDate.parse(travelDate);
} catch (DateTimeParseException | NullPointerException ex) {
hasError = true;
errorMsg = "Please select a valid outbound date.";
}

if ("roundtrip".equalsIgnoreCase(tripType)) {
try {
ret = LocalDate.parse(returnDate);
} catch (DateTimeParseException | NullPointerException ex) {
hasError = true;
errorMsg = "Please select a valid return date.";
}
}

if (!hasError && outbound != null && outbound.isBefore(today)) {
hasError = true;
errorMsg = "Outbound date cannot be before today.";
}

if (!hasError && ret != null && ret.isBefore(outbound)) {
hasError = true;
errorMsg = "Return date cannot be before outbound date.";
}

if (hasError) {
request.setAttribute("searchError", errorMsg);
request.getRequestDispatcher("welcome.jsp").forward(request, response);
return;
}
// ---- End date validation ----

if ("roundtrip".equalsIgnoreCase(tripType)) {
List<Flights> outboundFlights = dao.searchFlights(source, destination, travelDate);
List<Flights> returnFlights = dao.searchFlights(destination, source, returnDate);
request.setAttribute("outboundFlights", outboundFlights);
request.setAttribute("returnFlights", returnFlights);
request.setAttribute("tripType", "roundtrip");
} else {
List<Flights> flights = dao.searchFlights(source, destination, travelDate);
request.setAttribute("flights", flights);
request.setAttribute("tripType", "oneway");
}
request.getRequestDispatcher("welcome.jsp").forward(request, response);
} catch (Exception e) {
e.printStackTrace();
}
}

}
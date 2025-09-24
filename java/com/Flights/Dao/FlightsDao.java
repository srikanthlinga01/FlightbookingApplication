package com.Flights.Dao;
import com.Flights.model.Flights;

import java.sql.*;
import java.util.*;

public class FlightsDao {

    String url = "jdbc:mysql://localhost:3306/flightbooking";
    String db_username = "root";
    String db_password = "root";

    public List<String> getAllSources() throws ClassNotFoundException {
        List<String> sources = new ArrayList<>();
        String sql = "SELECT DISTINCT flight_source FROM flights ORDER BY flight_source";
        try (
            Connection con = DriverManager.getConnection(url, db_username, db_password);
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            while (rs.next()) {
                sources.add(rs.getString("flight_source"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sources;
    }

    public List<String> getAllDestinations() throws ClassNotFoundException {
        List<String> destinations = new ArrayList<>();
        String sql = "SELECT DISTINCT destination FROM flights ORDER BY destination";
        try (
            Connection con = DriverManager.getConnection(url, db_username, db_password);
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            while (rs.next()) {
                destinations.add(rs.getString("destination"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return destinations;
    }

    public List<Flights> searchFlights(String source, String destination, String flight_date) throws ClassNotFoundException {
        List<Flights> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE flight_source=? AND destination=? AND flight_date=?";
        try (
            Connection con = DriverManager.getConnection(url, db_username, db_password);
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            ps.setString(1, source);
            ps.setString(2, destination);
            ps.setString(3, flight_date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Flights flight = new Flights();
                    flight.setFlight_source(rs.getString("flight_source"));
                    flight.setDestination(rs.getString("destination"));
                    flight.setFlight_number(rs.getString("flight_number"));
                    flight.setAirline_name(rs.getString("airline_name"));
                    flight.setFlight_date(rs.getString("flight_date"));
                    // If you have a flight_id column:
                    // flight.setFlight_id(rs.getInt("flight_id"));
                    flights.add(flight);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return flights;
    }
}

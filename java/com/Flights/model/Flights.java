package com.Flights.model;

public class Flights {
    private int flight_id; 
    private String flight_source;
    private String destination;
    private String flight_number;
    private String airline_name;
    private String flight_date;

    // Constructors
    public Flights() {}

    public Flights(int flight_id, String flight_source, String destination, String flight_number, String airline_name, String flight_date) {
        this.flight_id = flight_id;
        this.flight_source = flight_source;
        this.destination = destination;
        this.flight_number = flight_number;
        this.airline_name = airline_name;
        this.flight_date = flight_date;
    }

    // Getters and Setters
    public int getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(int flight_id) {
        this.flight_id = flight_id;
    }

    public String getFlight_source() {
        return flight_source;
    }

    public void setFlight_source(String flight_source) {
        this.flight_source = flight_source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFlight_number() {
        return flight_number;
    }

    public void setFlight_number(String flight_number) {
        this.flight_number = flight_number;
    }

    public String getAirline_name() {
        return airline_name;
    }

    public void setAirline_name(String airline_name) {
        this.airline_name = airline_name;
    }

    public String getFlight_date() {
        return flight_date;
    }

    public void setFlight_date(String flight_date) {
        this.flight_date = flight_date;
    }
}

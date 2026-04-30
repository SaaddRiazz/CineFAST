package com.example.cinefast;

import java.util.ArrayList;

public class Booking {
    public String movieName;
    public ArrayList<String> seats;
    public float totalPrice;
    public String timestamp;

    public Booking() {}

    public Booking(String movieName, ArrayList<String> seats, float totalPrice, String timestamp) {
        this.movieName = movieName;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }
}
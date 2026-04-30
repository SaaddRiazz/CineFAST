package com.example.cinefast;

import java.util.ArrayList;

public class Booking {
    public String bookingId;
    public String movieName;
    public int moviePoster;
    public ArrayList<String> seats;
    public ArrayList<Integer> selectedIndices;
    public float totalPrice;
    public String timestamp;
    public long unixTimestamp;

    public Booking() {}

    public Booking(String movieName, int moviePoster, ArrayList<String> seats,
                   ArrayList<Integer> selectedIndices, float totalPrice,
                   String timestamp, long unixTimestamp) {
        this.movieName = movieName;
        this.moviePoster = moviePoster;
        this.seats = seats;
        this.selectedIndices = selectedIndices;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
        this.unixTimestamp = unixTimestamp;
    }
}
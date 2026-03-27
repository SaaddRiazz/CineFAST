package com.example.cinefast;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    HashMap<String, String[]> seatBookingMap = new HashMap<>();
    ArrayList<String> selectedSeats = new ArrayList<>();
    ArrayList<SnacksCardView> selectedSnacks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), false);
        }
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment);
        if (addToBackStack) ft.addToBackStack(null);
        ft.commit();
    }

    public String[] getSeatStates(String movieTitle, int totalSeats) {
        if (!seatBookingMap.containsKey(movieTitle)) {
            String[] states = new String[totalSeats];
            Random random = new Random();
            for (int i = 0; i < totalSeats; i++) {
                states[i] = random.nextInt(100) < 20 ? "booked" : "available";
            }
            seatBookingMap.put(movieTitle, states);
        }
        return seatBookingMap.get(movieTitle);
    }

    public void selectSeat(String movieTitle, int index) {
        String[] states = seatBookingMap.get(movieTitle);
        if (states != null && index < states.length) {
            states[index] = "selected";
        }
    }

    public void deselectSeat(String movieTitle, int index) {
        String[] states = seatBookingMap.get(movieTitle);
        if (states != null && index < states.length) {
            states[index] = "available";
        }
    }

    public void finalizeBooking(String movieTitle) {
        String[] states = seatBookingMap.get(movieTitle);
        if (states != null) {
            for (int i = 0; i < states.length; i++) {
                if (states[i].equals("selected")) {
                    states[i] = "booked";
                }
            }
        }
    }

    public void clearSelectedSeats(String movieTitle) {
        String[] states = seatBookingMap.get(movieTitle);
        if (states != null) {
            for (int i = 0; i < states.length; i++) {
                if (states[i].equals("selected")) states[i] = "available";
            }
        }
    }

    public void saveLastBooking(String movieName, int seatCount, float totalPrice) {
        SharedPreferences prefs = getSharedPreferences("Last Booking Preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("last_movie_name", movieName);
        editor.putInt("last_seat_count", seatCount);
        editor.putFloat("last_total_price", totalPrice);
        editor.apply();
    }

    public void showLastBookingDialog() {
        SharedPreferences prefs = getSharedPreferences("Last Booking Preferences", MODE_PRIVATE);
        String movie = prefs.getString("last_movie_name", null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Last Booking");

        if (movie == null) {
            builder.setMessage("No previous booking found.");
        } else {
            int seats = prefs.getInt("last_seat_count", 0);
            float price = prefs.getFloat("last_total_price", 0.0f);

            String message = "Movie: " + movie + "\n" +
                    "Seats: " + seats + "\n" +
                    "Total Price: $" + String.format("%.2f", price);
            builder.setMessage(message);
        }

        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
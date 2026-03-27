package com.example.cinefast;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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

    public void clearSelectedSeats(String movieTitle) {
        String[] states = seatBookingMap.get(movieTitle);
        if (states != null) {
            for (int i = 0; i < states.length; i++) {
                if (states[i].equals("selected")) states[i] = "available";
            }
        }
    }
}
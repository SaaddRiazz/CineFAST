package com.example.cinefast;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    HashMap<String, boolean[]> seatBookingMap = new HashMap<>();

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

    public boolean[] getSeatStates(String movieTitle, int totalSeats) {
        if (!seatBookingMap.containsKey(movieTitle)) {
            boolean[] states = new boolean[totalSeats];
            Random random = new Random();
            for (int i = 0; i < totalSeats; i++) {
                states[i] = random.nextInt(100) < 20;
            }
            seatBookingMap.put(movieTitle, states);
        }
        return seatBookingMap.get(movieTitle);
    }
}
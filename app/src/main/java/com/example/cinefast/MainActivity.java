package com.example.cinefast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    ArrayList<String> selectedSeats = new ArrayList<>();
    ArrayList<SnacksCardView> selectedSnacks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), false);
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(new HomeFragment(), false);
            clearSelectionData();
        } else if (id == R.id.nav_bookings) {
            loadFragment(new BookingHistoryFragment(), true);
        } else if (id == R.id.nav_logout) {
            performLogout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clearSelectionData() {
        selectedSeats.clear();
        selectedSnacks.clear();
    }

    private void performLogout() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences sharedPreferences = getSharedPreferences("cinefast_session_pref_v3", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment);
        if (addToBackStack) ft.addToBackStack(null);
        ft.commit();
    }

    public void finalizeBooking(String movieTitle, List<Integer> selectedIndices) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("seats").child(movieTitle);
        for (Integer index : selectedIndices) {
            dbRef.child(String.valueOf(index)).setValue("booked");
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
            builder.setMessage("Movie: " + movie + "\nSeats: " + seats + "\nTotal: $" + String.format("%.2f", price));
        }
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
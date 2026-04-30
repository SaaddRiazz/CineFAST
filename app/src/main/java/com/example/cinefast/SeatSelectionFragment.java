package com.example.cinefast;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class SeatSelectionFragment extends Fragment {

    private String movieTitle;
    private int moviePoster;
    private Button bBack, bBookConfirm, bSnacks;
    private LinearLayout seatContainer;
    private DatabaseReference seatRef;
    private ArrayList<Integer> currentSelectedIndices = new ArrayList<>();

    public SeatSelectionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seat_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            movieTitle = getArguments().getString("movie_title_key");
            moviePoster = getArguments().getInt("movie_poster_key");
        }

        init(view);

        seatContainer.setOrientation(LinearLayout.VERTICAL);
        seatContainer.setGravity(Gravity.CENTER);

        seatRef = FirebaseDatabase.getInstance().getReference("seats").child(movieTitle);
        loadSeatsFromFirebase();
    }

    private void init(View view) {
        bBack = view.findViewById(R.id.bBack);
        bBookConfirm = view.findViewById(R.id.bBookConfirm);
        bSnacks = view.findViewById(R.id.bSnacks);
        seatContainer = view.findViewById(R.id.llSeatsCont);

        TextView tvMovieTitle = view.findViewById(R.id.tvMovieTitle);
        tvMovieTitle.setText(movieTitle);

        bSnacks.setOnClickListener(v -> {
            SnacksMenuFragment fragment = new SnacksMenuFragment();
            Bundle args = new Bundle();
            args.putString("movie_title_key", movieTitle);
            args.putInt("movie_poster_key", moviePoster);
            args.putStringArrayList("selected_seats_key", ((MainActivity) requireActivity()).selectedSeats);
            args.putIntegerArrayList("selected_indices_key", currentSelectedIndices);
            fragment.setArguments(args);
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        bBookConfirm.setOnClickListener(v -> {
            BookingFragment fragment = new BookingFragment();
            Bundle args = new Bundle();
            args.putString("movie_title_key", movieTitle);
            args.putInt("movie_poster_key", moviePoster);
            args.putStringArrayList("selected_seats_key", ((MainActivity) requireActivity()).selectedSeats);
            args.putIntegerArrayList("selected_indices_key", currentSelectedIndices);
            fragment.setArguments(args);
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        bBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void loadSeatsFromFirebase() {
        seatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded() || getContext() == null) {
                    return;
                }
                if (!snapshot.exists()) {
                    generateInitialSeatsInFirebase();
                } else {
                    ArrayList<String> states = new ArrayList<>();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        states.add(child.getValue(String.class));
                    }
                    renderSeatUI(states);
                }
                updateButtonStates();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void generateInitialSeatsInFirebase() {
        int totalSeats = 0;
        for (char row = 'H'; row >= 'A'; row--) {
            totalSeats += (row == 'A' || row == 'H') ? 6 : 8;
        }

        java.util.Map<String, Object> initialSeats = new java.util.HashMap<>();
        Random random = new Random();

        for (int i = 0; i < totalSeats; i++) {
            String state = (random.nextInt(100) < 20) ? "booked" : "available";
            initialSeats.put(String.valueOf(i), state);
        }

        seatRef.updateChildren(initialSeats);
    }

    private void renderSeatUI(ArrayList<String> states) {
        seatContainer.removeAllViews();
        float density = getResources().getDisplayMetrics().density;
        int seatSize = (int) (36 * density);
        int seatIndex = 0;

        for (char row = 'H'; row >= 'A'; row--) {
            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            rowLayout.setLayoutParams(rowParams);

            int seatCount = (row == 'A' || row == 'H') ? 6 : 8;

            for (int seatNum = 1; seatNum <= seatCount; seatNum++) {
                int margin = (seatNum == seatCount / 2) ? 100 : 1;
                SeatButton seat = new SeatButton(getContext());

                LinearLayout.LayoutParams seatParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                seatParams.setMarginEnd(margin);
                seat.setLayoutParams(seatParams);

                seat.setRow(String.valueOf(row));
                seat.setSeatNumber(seatNum);

                String seatLabel = "Row " + row + ", Seat " + seatNum;
                int finalSeatIndex = seatIndex;
                String status = states.get(seatIndex);

                if (currentSelectedIndices.contains(finalSeatIndex)) {
                    seat.setStatus("selected");
                    seat.setImageResource(R.drawable.your_seat);
                } else if (status.equals("booked")) {
                    seat.setStatus("booked");
                    seat.setImageResource(R.drawable.unavailable_seat);
                } else {
                    seat.setStatus("available");
                    seat.setImageResource(R.drawable.available_seat);
                }

                seat.setBackgroundColor(Color.TRANSPARENT);

                seat.setOnClickListener(v -> {
                    if (status.equals("available")) {
                        if (!currentSelectedIndices.contains(finalSeatIndex)) {
                            currentSelectedIndices.add(finalSeatIndex);
                            ((MainActivity) requireActivity()).selectedSeats.add(seatLabel);
                            seat.setImageResource(R.drawable.your_seat);
                        } else {
                            currentSelectedIndices.remove(Integer.valueOf(finalSeatIndex));
                            ((MainActivity) requireActivity()).selectedSeats.remove(seatLabel);
                            seat.setImageResource(R.drawable.available_seat);
                        }
                        updateButtonStates();
                    }
                });

                rowLayout.addView(seat);
                seatIndex++;
            }
            seatContainer.addView(rowLayout);
        }
    }

    private void updateButtonStates() {
        boolean hasSelection = !currentSelectedIndices.isEmpty();

        if (hasSelection) {
            bBookConfirm.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EE0000")));
            bBookConfirm.setTextColor(Color.parseColor("#FFFFFF"));
            bSnacks.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            bBookConfirm.setEnabled(true);
            bSnacks.setEnabled(true);
        } else {
            bBookConfirm.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#590909")));
            bBookConfirm.setTextColor(Color.parseColor("#767676"));
            bSnacks.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#474747")));
            bBookConfirm.setEnabled(false);
            bSnacks.setEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) requireActivity()).selectedSeats.clear();
    }
}
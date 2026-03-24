package com.example.cinefast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeatSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeatSelectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeatSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeatSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeatSelectionFragment newInstance(String param1, String param2) {
        SeatSelectionFragment fragment = new SeatSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seat_selection, container, false);
    }

    private final ArrayList<String> selectedSeats = new ArrayList<>();

    private Button bBack, bBookConfirm, bSnacks;

    private String movieTitle;
    private int moviePoster;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            movieTitle  = getArguments().getString("movie_title");
            moviePoster = getArguments().getInt("movie_poster");
        }

        init(view);
        generateSeats(view);
    }

    private void init(View view) {
        bBack = view.findViewById(R.id.bBack);
        bBookConfirm = view.findViewById(R.id.bBookConfirm);
        bSnacks = view.findViewById(R.id.bSnacks);

        bSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SnacksMenu.class);

                i.putExtra("movie_title_key", movieTitle);
                i.putExtra("movie_poster_key", moviePoster);
                i.putExtra("selected_seats_key", selectedSeats);

                startActivity(i);
            }
        });

        bBookConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Booking.class);

                i.putExtra("movie_title_key", movieTitle);
                i.putExtra("movie_poster_key", moviePoster);
                i.putExtra("selected_seats_key", selectedSeats);

                startActivity(i);
            }
        });

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void generateSeats(View view) {

        LinearLayout seatContainer = view.findViewById(R.id.llSeatsCont);
        seatContainer.setOrientation(LinearLayout.VERTICAL);
        seatContainer.setGravity(Gravity.CENTER);


        float density = getResources().getDisplayMetrics().density;
        int seatSize = (int) (36 * density);

        Random random = new Random();

        for (char row = 'H'; row >= 'A'; row--) {

            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams rowParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
            rowLayout.setLayoutParams(rowParams);

            int seatCount = (row == 'A' || row == 'H') ? 6 : 8;

            for (int seatNum = 1; seatNum <= seatCount; seatNum++) {
                int margin = 1;
                if(seatNum==seatCount/2)
                    margin = 100;
                SeatButton seat = new SeatButton(getContext());

                LinearLayout.LayoutParams seatParams =
                        new LinearLayout.LayoutParams(seatSize, seatSize);
                seatParams.setMarginEnd(margin);
                seat.setLayoutParams(seatParams);

                seat.setRow(String.valueOf(row));
                seat.setSeatNumber(seatNum);

                boolean isBooked = random.nextInt(100) < 20; // 20% probability

                if (isBooked) {
                    seat.setStatus("booked");
                    seat.setImageResource(R.drawable.unavailable_seat);
                }
                else {
                    seat.setStatus("available");
                    seat.setImageResource(R.drawable.available_seat);
                }

                seat.setBackgroundColor(Color.TRANSPARENT);

                seat.setOnClickListener(v -> {
                    if(Objects.equals(seat.getStatus(), "available")){
                        seat.setStatus("selected");
                        seat.setImageResource(R.drawable.your_seat);
                        selectedSeats.add("Row "+seat.getRow()+", Seat "+seat.getSeatNumber());
                    }
                    else if(Objects.equals(seat.getStatus(), "selected")){
                        seat.setStatus("available");
                        seat.setImageResource(R.drawable.available_seat);
                        selectedSeats.remove("Row "+seat.getRow()+", Seat "+seat.getSeatNumber());
                    }

                    if(!selectedSeats.isEmpty()){
                        bBookConfirm.setBackgroundTintList(
                                android.content.res.ColorStateList.valueOf(
                                        android.graphics.Color.parseColor("#EE0000")
                                )
                        );
                        bBookConfirm.setTextColor(android.graphics.Color.parseColor("#FFFFFF"));
                        bSnacks.setBackgroundTintList(
                                android.content.res.ColorStateList.valueOf(
                                        android.graphics.Color.parseColor("#FFFFFF")
                                )
                        );

                        bBookConfirm.setEnabled(true);
                        bSnacks.setEnabled(true);
                    }
                    else{
                        bBookConfirm.setBackgroundTintList(
                                android.content.res.ColorStateList.valueOf(
                                        android.graphics.Color.parseColor("#590909")
                                )
                        );
                        bBookConfirm.setTextColor(android.graphics.Color.parseColor("#767676"));
                        bSnacks.setBackgroundTintList(
                                android.content.res.ColorStateList.valueOf(
                                        android.graphics.Color.parseColor("#474747")
                                )
                        );

                        bBookConfirm.setEnabled(false);
                        bSnacks.setEnabled(false);
                    }
                });

                rowLayout.addView(seat);
            }

            seatContainer.addView(rowLayout);
        }
    }
}
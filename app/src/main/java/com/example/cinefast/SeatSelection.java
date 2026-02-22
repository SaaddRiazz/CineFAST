package com.example.cinefast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SeatSelection extends AppCompatActivity {

    private final ArrayList<String> selectedSeats = new ArrayList<>();
    TextView tvMovieTitle;
    Button bBack, bBookConfirm, bSnacks;

    String movieTitle;
    int moviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seat_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        generateSeats();

        Intent i = getIntent();
        movieTitle = i.getStringExtra("movie_title_key");
        moviePoster = i.getIntExtra("movie_poster_key", -1);

        tvMovieTitle.setText(movieTitle);
    }

    private void init(){
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        bBack=findViewById(R.id.bBack);
        bBookConfirm=findViewById(R.id.bBookConfirm);
        bSnacks=findViewById(R.id.bSnacks);

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SeatSelection.this, SnacksMenu.class);

                i.putExtra("movie_title_key", movieTitle);
                i.putExtra("movie_poster_key", moviePoster);
                i.putExtra("selected_seats_key", selectedSeats);

                startActivity(i);
            }
        });

        bBookConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SeatSelection.this, Booking.class);

                i.putExtra("movie_title_key", movieTitle);
                i.putExtra("movie_poster_key", moviePoster);
                i.putExtra("selected_seats_key", selectedSeats);

                startActivity(i);
            }
        });
    }

    private void generateSeats() {

        LinearLayout seatContainer = findViewById(R.id.llSeatsCont);
        seatContainer.setOrientation(LinearLayout.VERTICAL);
        seatContainer.setGravity(Gravity.CENTER);


        float density = getResources().getDisplayMetrics().density;
        int seatSize = (int) (36 * density);

        Random random = new Random();

        for (char row = 'H'; row >= 'A'; row--) {

            LinearLayout rowLayout = new LinearLayout(this);
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
                SeatButton seat = new SeatButton(this);

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
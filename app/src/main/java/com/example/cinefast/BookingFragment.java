package com.example.cinefast;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {

    LinearLayout llBookedSeats, llBoughtSnacks;
    ShapeableImageView sivMoviePoster;
    TextView tvMovieTitle, tvTotalPrice;
    Button bBack, bSendTicket;
    String bookingSummary, movieTitle;
    int moviePoster;
    ArrayList<String> selectedSeats, selectedSnacks;
    ArrayList<Integer> selectedIndices;
    float totalPrice = 0f;

    public BookingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            movieTitle = getArguments().getString("movie_title_key");
            moviePoster = getArguments().getInt("movie_poster_key");
            selectedSeats = getArguments().getStringArrayList("selected_seats_key");
            selectedSnacks = getArguments().getStringArrayList("selected_snacks_key");
            selectedIndices = getArguments().getIntegerArrayList("selected_indices_key");
        }

        init(view);
        tvMovieTitle.setText(movieTitle);
        sivMoviePoster.setImageResource(moviePoster);
        bookingSummary = movieTitle + " - Booking Summary";

        generateTicketDetails(selectedSeats);
        if (selectedSnacks != null) generateSnacksDetails(selectedSnacks, view);

        tvTotalPrice.setText(String.format("$%.2f", totalPrice));
        bookingSummary += "\n\nTOTAL                " + String.format("$%.2f", totalPrice);

        bSendTicket.setOnClickListener(v -> {
            MainActivity main = ((MainActivity) requireActivity());
            String userId = FirebaseAuth.getInstance().getUid();

            if (userId != null) {
                long nextDayTimeMillis = System.currentTimeMillis() + 24 * 60 * 60 * 1000L;
                String readableDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date(nextDayTimeMillis));

                Booking booking = new Booking(
                        movieTitle,
                        moviePoster,
                        selectedSeats,
                        selectedIndices,
                        totalPrice,
                        readableDate,
                        nextDayTimeMillis
                );

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                rootRef.child("bookings").child(userId).push().setValue(booking)
                        .addOnSuccessListener(aVoid -> {
                            main.finalizeBooking(movieTitle, selectedIndices);
                            main.saveLastBooking(movieTitle, selectedSeats.size(), totalPrice);
                            sendTicket(main);
                        });
            }
        });

        bBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void sendTicket(MainActivity main) {
        Intent intentMail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        intentMail.putExtra(Intent.EXTRA_SUBJECT, "CineFAST Ticket - " + movieTitle);
        intentMail.putExtra(Intent.EXTRA_TEXT, bookingSummary);

        Intent intentWA = new Intent(Intent.ACTION_VIEW);
        intentWA.setData(Uri.parse("https://wa.me/?text=" + Uri.encode(bookingSummary)));

        Intent chooser = Intent.createChooser(intentMail, "Send Ticket via");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{intentWA});
        startActivity(chooser);

        Toast.makeText(main, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
        main.clearSelectionData();
        main.getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        main.loadFragment(new HomeFragment(), false);
    }

    private void generateTicketDetails(ArrayList<String> seats) {
        bookingSummary += "\n\n***Tickets***\n";
        for (String seat : seats) {
            totalPrice += 9.99f;
            addDetailRow(llBookedSeats, seat, "$9.99");
            bookingSummary += "\n" + seat + "    $9.99";
        }
    }

    private void generateSnacksDetails(ArrayList<String> snacks, View view) {
        bookingSummary += "\n\n***Snacks***\n";
        llBoughtSnacks.setVisibility(View.VISIBLE);
        view.findViewById(R.id.tvSnacksText).setVisibility(View.VISIBLE);
        for (String snack : snacks) {
            String[] parts = snack.split("#");
            totalPrice += Float.parseFloat(parts[1]);
            addDetailRow(llBoughtSnacks, parts[0], "$" + parts[1]);
            bookingSummary += "\n" + parts[0] + "    $" + parts[1];
        }
    }

    private void addDetailRow(LinearLayout container, String infoTxt, String priceTxt) {
        LinearLayout row = new LinearLayout(getContext());
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        TextView tvInfo = new TextView(getContext());
        tvInfo.setText(infoTxt);
        tvInfo.setTextColor(Color.parseColor("#727886"));
        tvInfo.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.poppins));

        View spacer = new View(getContext());
        spacer.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1));

        TextView tvPrice = new TextView(getContext());
        tvPrice.setText(priceTxt);
        tvPrice.setTextColor(Color.parseColor("#727886"));
        tvPrice.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.poppins));

        row.addView(tvInfo);
        row.addView(spacer);
        row.addView(tvPrice);
        container.addView(row);
    }

    private void init(View view) {
        llBookedSeats = view.findViewById(R.id.llBookedSeats);
        llBoughtSnacks = view.findViewById(R.id.llBoughtSnacks);
        bBack = view.findViewById(R.id.bBack);
        bSendTicket = view.findViewById(R.id.bSendTicket);
        tvMovieTitle = view.findViewById(R.id.tvMovieTitle);
        sivMoviePoster = view.findViewById(R.id.sivMoviePoster);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
    }
}
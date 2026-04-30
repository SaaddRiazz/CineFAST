package com.example.cinefast;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context;
    private ArrayList<Booking> bookingList;
    private String userId;

    public BookingAdapter(Context context, ArrayList<Booking> bookingList, String userId) {
        this.context = context;
        this.bookingList = bookingList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvMovieTitle.setText(booking.movieName);
        holder.tvDateTime.setText(booking.timestamp);
        holder.tvTicketCount.setText(booking.seats.size() + " Tickets");
        holder.ivPoster.setImageResource(booking.moviePoster);

        holder.ivDelete.setOnClickListener(v -> {
            if (isFutureDate(booking.timestamp)) {
                showCancelDialog(booking, position);
            } else {
                Toast.makeText(context, "Cannot cancel past bookings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isFutureDate(String bookingDateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.getDefault());
            Date bookingDate = sdf.parse(bookingDateStr);
            return bookingDate != null && bookingDate.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private void showCancelDialog(Booking booking, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference movieSeatsRef = dbRef.child("seats").child(booking.movieName);
                    for (Integer index : booking.selectedIndices) {
                        movieSeatsRef.child(String.valueOf(index)).setValue("available");
                    }

                    dbRef.child("bookings").child(userId).child(booking.bookingId)
                            .removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "Booking Cancelled & Seats Released!", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitle, tvDateTime, tvTicketCount, ivDelete;
        ImageView ivPoster;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvTicketCount = itemView.findViewById(R.id.tvTicketCount);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
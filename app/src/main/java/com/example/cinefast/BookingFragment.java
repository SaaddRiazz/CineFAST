package com.example.cinefast;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookingFragment newInstance(String param1, String param2) {
        BookingFragment fragment = new BookingFragment();
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
        return inflater.inflate(R.layout.fragment_booking, container, false);
    }

    LinearLayout llBookedSeats, llBoughtSnacks;
    ShapeableImageView sivMoviePoster;
    TextView tvMovieTitle, tvTotalPrice;
    Button bBack, bSendTicket;
    String bookingSummary;

    float totalPrice=0f;

    String movieTitle;
    int moviePoster;
    ArrayList<String> selectedSeats, selectedSnacks;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            movieTitle = getArguments().getString("movie_title_key");
            moviePoster = getArguments().getInt("movie_poster_key");
            selectedSeats = getArguments().getStringArrayList("selected_seats_key");
            selectedSnacks = getArguments().getStringArrayList("selected_snacks_key");
        }

        init(view);

        tvMovieTitle.setText(movieTitle);
        sivMoviePoster.setImageResource(moviePoster);

        bookingSummary = movieTitle + " - Booking Summary";

        generateTicketDetails(selectedSeats);
        if (selectedSnacks != null)
            generateSnacksDetails(selectedSnacks, view);

        tvTotalPrice.setText(String.format("$%.2f", totalPrice));

        bookingSummary += "\n\n----------------------------------------------------------\n\n" +
                "TOTAL                " + String.format("$%.2f", totalPrice);

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        bSendTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = ((MainActivity) requireActivity());
                main.finalizeBooking(movieTitle);
                main.selectedSeats.clear();

                Intent intentMail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+
                        "?subject="+
                        "My CineFAST Ticket - "+tvMovieTitle.getText()+
                        "&body="+
                        bookingSummary));

                Intent intentWA = new Intent(Intent.ACTION_VIEW);
                intentWA.setData(Uri.parse("https://wa.me/" + "?text=" + bookingSummary));

                List<Intent> targetedIntents = new ArrayList<>();
                targetedIntents.add(intentMail);
                targetedIntents.add(intentWA);

                Intent chooser = Intent.createChooser(targetedIntents.remove(0), "Send Ticket via");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedIntents.toArray(new Parcelable[]{}));
                startActivity(chooser);

                Toast.makeText(main, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                main.getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                main.loadFragment(new HomeFragment(), false);
            }
        });
    }

    private void generateTicketDetails(ArrayList<String> seats){

        bookingSummary+="\n\n\t***Tickets***\n";
        for(String seat : seats) {
            LinearLayout ticket = new LinearLayout(getContext());
            ticket.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView info = new TextView(getContext());
            info.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            info.setText(seat);
            info.setTextColor(Color.parseColor("#727886"));
            info.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins));

            View separator = new View(getContext());
            separator.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1));

            TextView price = new TextView(getContext());
            price.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            price.setText("$9.99");
            totalPrice+=9.99f;
            price.setTextColor(Color.parseColor("#727886"));
            price.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins));

            ticket.addView(info);
            ticket.addView(separator);
            ticket.addView(price);

            llBookedSeats.addView(ticket);

            bookingSummary+="\n\t\t"+info.getText().toString()+"    "+price.getText().toString();
        }
    }

    private void generateSnacksDetails(ArrayList<String> snacks, View view){

        bookingSummary+="\n\n\t***Snacks***\n";

        for(String snack : snacks) {
            String snackInfo = snack.split("#")[0], snackPrice = snack.split("#")[1];

            LinearLayout item = new LinearLayout(getContext());
            item.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView info = new TextView(getContext());
            info.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            info.setText(snackInfo);
            info.setTextColor(Color.parseColor("#727886"));
            info.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins));

            View separator = new View(getContext());
            separator.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1));

            TextView price = new TextView(getContext());
            price.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            price.setText("$"+snackPrice);
            totalPrice+=Float.parseFloat(snackPrice);
            price.setTextColor(Color.parseColor("#727886"));
            price.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins));

            item.addView(info);
            item.addView(separator);
            item.addView(price);

            llBoughtSnacks.setVisibility(View.VISIBLE);
            view.findViewById(R.id.tvSnacksText).setVisibility(View.VISIBLE);
            llBoughtSnacks.addView(item);

            bookingSummary+="\n\t\t"+info.getText().toString()+"    "+price.getText().toString();
        }
    }

    private void init(View view){
        llBookedSeats=view.findViewById(R.id.llBookedSeats);
        llBoughtSnacks=view.findViewById(R.id.llBoughtSnacks);
        bBack=view.findViewById(R.id.bBack);
        bSendTicket=view.findViewById(R.id.bSendTicket);
        tvMovieTitle=view.findViewById(R.id.tvMovieTitle);
        sivMoviePoster=view.findViewById(R.id.sivMoviePoster);
        tvTotalPrice=view.findViewById(R.id.tvTotalPrice);
    }
}
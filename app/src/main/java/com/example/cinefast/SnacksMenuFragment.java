package com.example.cinefast;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

public class SnacksMenuFragment extends Fragment {

    private ArrayList<Snack> snacks;
    private String movieTitle;
    private int moviePoster;
    private ArrayList<String> selectedSeats;
    private ArrayList<Integer> selectedIndices;
    private Button bBookConfirm;

    public SnacksMenuFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_snacks_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            movieTitle = getArguments().getString("movie_title_key");
            moviePoster = getArguments().getInt("movie_poster_key");
            selectedSeats = getArguments().getStringArrayList("selected_seats_key");
            selectedIndices = getArguments().getIntegerArrayList("selected_indices_key");
        }

        SnackDatabaseHelper dbHelper = new SnackDatabaseHelper(requireContext());
        snacks = dbHelper.getAllSnacks(requireContext());

        ListView lvSnacks = view.findViewById(R.id.lvSnacks);
        SnackAdapter adapter = new SnackAdapter(requireContext(), snacks, ((MainActivity) requireActivity()).selectedSnacks);
        lvSnacks.setAdapter(adapter);

        bBookConfirm = view.findViewById(R.id.bBookConfirm);
        bBookConfirm.setOnClickListener(v -> {
            BookingFragment fragment = new BookingFragment();
            Bundle args = new Bundle();
            args.putString("movie_title_key", movieTitle);
            args.putInt("movie_poster_key", moviePoster);
            args.putStringArrayList("selected_seats_key", selectedSeats);
            args.putStringArrayList("selected_snacks_key", formatSnacks());

            args.putIntegerArrayList("selected_indices_key", selectedIndices);

            fragment.setArguments(args);
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });
    }

    private ArrayList<String> formatSnacks() {
        ArrayList<String> formatted = new ArrayList<>();
        for (SnacksCardView scv : ((MainActivity) requireActivity()).selectedSnacks) {
            String s = "x" + scv.getItemQuantity()
                    + " " + scv.getItemName()
                    + " (" + scv.getItemInfo()
                    + ")#" + String.format("%.2f", scv.getItemQuantity() * scv.getItemPrice());
            formatted.add(s);
        }
        return formatted;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) requireActivity()).selectedSnacks.clear();
    }
}
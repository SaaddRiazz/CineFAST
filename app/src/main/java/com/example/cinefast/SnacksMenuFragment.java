package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SnacksMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SnacksMenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SnacksMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SnacksMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SnacksMenuFragment newInstance(String param1, String param2) {
        SnacksMenuFragment fragment = new SnacksMenuFragment();
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
        return inflater.inflate(R.layout.fragment_snacks_menu, container, false);
    }

    ArrayList<Snack> snacks;
    private String movieTitle;
    private int moviePoster;
    private ArrayList<String> selectedSeats;
    Button bBookConfirm;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            movieTitle = getArguments().getString("movie_title_key");
            moviePoster = getArguments().getInt("movie_poster_key");
            selectedSeats = getArguments().getStringArrayList("selected_seats_key");
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
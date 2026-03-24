package com.example.cinefast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NowShowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NowShowingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NowShowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NowShowingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NowShowingFragment newInstance(String param1, String param2) {
        NowShowingFragment fragment = new NowShowingFragment();
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
        View view = inflater.inflate(R.layout.fragment_now_showing, container, false);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Dark Knight", "Action / 152 mins", R.drawable.the_dark_knight, "https://youtu.be/EXeTwQWrcwY", "now_showing"));
        movies.add(new Movie("Inception", "Sci-Fi / 148 mins", R.drawable.inception, "https://youtu.be/YoHD9XEInc0", "now_showing"));
        movies.add(new Movie("Interstellar", "Sci-Fi / 169 mins", R.drawable.interstellar, "https://youtu.be/zSWdZVtXT7E", "now_showing"));

        RecyclerView rv = view.findViewById(R.id.rvNowShowing);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        MovieAdapter movieAdapter = getMovieAdapter(movies);
        rv.setAdapter(movieAdapter);

        return view;
    }

    @NonNull
    private MovieAdapter getMovieAdapter(ArrayList<Movie> movies) {
        MovieAdapter movieAdapter = new MovieAdapter(movies);
        movieAdapter.setOnBookClickListener(movie -> {
            SeatSelectionFragment fragment = new SeatSelectionFragment();
            Bundle args = new Bundle();
            args.putString("movie_title_key", movie.getTitle());
            args.putInt("movie_poster_key", movie.getPosterId());
            fragment.setArguments(args);
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        movieAdapter.setOnTrailerClickListener(movie -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerLink()));
            startActivity(i);
        });
        return movieAdapter;
    }
}
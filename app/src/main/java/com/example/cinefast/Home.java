package com.example.cinefast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home extends AppCompatActivity {

    ArrayList<Movie> movies = new ArrayList<>();
    LinearLayout movieContainerToday, movieContainerTomorrow;
    RadioButton rbToday, rbTomorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        rbToday.toggle();

        rbToday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if(rbToday.isChecked()){
                    movieContainerToday.setVisibility(View.VISIBLE);
                    movieContainerTomorrow.setVisibility(View.GONE);
                }
            }
        });

        rbTomorrow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if(rbTomorrow.isChecked()){
                    movieContainerToday.setVisibility(View.GONE);
                    movieContainerTomorrow.setVisibility(View.VISIBLE);
                }
            }
        });

        storeMovies();
        displayMovies();
    }

    private void displayMovies(){
        for (Movie movie : movies){
            MovieCardView mcv = new MovieCardView(this);
            mcv.setMovieTitle(movie.getTitle());
            mcv.setMovieInfo(movie.getInfo());
            mcv.setMoviePoster(movie.getPosterId());

            mcv.setOnBookClickListener(new MovieCardView.setOnBookClickListener() {
                @Override
                public void onBookClick() {
                    Intent i = new Intent(Home.this, SeatSelection.class);
                    i.putExtra("movie_title_key", movie.getTitle());
                    i.putExtra("movie_poster_key", movie.getPosterId());
                    startActivity(i);
                }
            });

            mcv.setOnTrailerClickListener(new MovieCardView.setOnTrailerClickListener() {
                @Override
                public void onTrailerClick() {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerLink()));
                    startActivity(i);
                }
            });

            if(Objects.equals(movie.getDate(), "today"))
                movieContainerToday.addView(mcv);
            else if(Objects.equals(movie.getDate(), "tomorrow"))
                movieContainerTomorrow.addView(mcv);
        }
    }

    private void storeMovies(){
        movies.add(new Movie("The Dark Knight", "Action / 152 mins", R.drawable.the_dark_knight, "https://youtu.be/EXeTwQWrcwY?si=8GvXtrE8lPmvul2u", "today"));
        movies.add(new Movie("Inception", "Sci-Fi / 148 mins", R.drawable.inception, "https://youtu.be/YoHD9XEInc0?si=alp4rrx19LT02rK_", "today"));
        movies.add(new Movie("Interstellar", "Sci-Fi / 169 mins", R.drawable.interstellar, "https://youtu.be/zSWdZVtXT7E?si=ZfdrA1HK7A-iGkaf", "today"));
        movies.add(new Movie("The Shawshank Redemption", "Drama / 142 mins", R.drawable.the_shawshank_redemption, "https://youtu.be/PLl99DlL6b4?si=dP0f6w1bhP6BJFiR", "tomorrow"));
        movies.add(new Movie("The Godfather", "Drama / 175 mins", R.drawable.the_godfather, "https://youtu.be/UaVTIH8mujA?si=AUQOSlEepeIsSVSR", "tomorrow"));
    }

    private void init(){
        movieContainerToday = findViewById(R.id.llMovieContToday);
        movieContainerTomorrow = findViewById(R.id.llMovieContTomorrow);
        rbToday = findViewById(R.id.rbToday);
        rbTomorrow = findViewById(R.id.rbTomorrow);
    }
}
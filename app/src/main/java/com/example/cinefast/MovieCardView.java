package com.example.cinefast;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

public class MovieCardView extends LinearLayout {

    private ShapeableImageView sivPoster;
    private TextView tvTitle, tvInfo;
    private Button bBook, bTrailer;

    private String movieTitle;
    private String movieInfo;
    private int moviePoster;

    public interface setOnBookClickListener {
        void onBookClick();
    }
    private setOnBookClickListener bookListener;

    public void setOnBookClickListener(setOnBookClickListener listener) {
        this.bookListener = listener;
    }

    public interface setOnTrailerClickListener {
        void onTrailerClick();
    }
    private setOnTrailerClickListener trailerListener;

    public void setOnTrailerClickListener(setOnTrailerClickListener listener) {
        this.trailerListener = listener;
    }

    public MovieCardView(Context context) {
        super(context);
        init(context);
    }

    public MovieCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MovieCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_movie_card, this, true);

        sivPoster = findViewById(R.id.sivMoviePoster);
        tvTitle = findViewById(R.id.tvMovieTitle);
        tvInfo = findViewById(R.id.tvMovieInfo);
        bBook = findViewById(R.id.bBook);
        bTrailer = findViewById(R.id.bTrailer);

        bBook.setOnClickListener(v -> {
            bookListener.onBookClick();
        });

        bTrailer.setOnClickListener(v -> {
            trailerListener.onTrailerClick();
        });

    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieInfo() {
        return movieInfo;
    }

    public int getMoviePoster() {
        return moviePoster;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
        tvTitle.setText(movieTitle);
    }

    public void setMovieInfo(String movieInfo) {
        this.movieInfo = movieInfo;
        tvInfo.setText(movieInfo);
    }

    public void setMoviePoster(int moviePoster) {
        this.moviePoster = moviePoster;
        sivPoster.setImageResource(moviePoster);
    }
}

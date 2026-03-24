package com.example.cinefast;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    public interface setOnBookClickListener {
        void onBookClick(Movie movie);
    }
    private setOnBookClickListener bookListener;
    public void setOnBookClickListener(setOnBookClickListener listener) {
        this.bookListener = listener;
    }

    public interface setOnTrailerClickListener {
        void onTrailerClick(Movie movie);
    }
    private setOnTrailerClickListener trailerListener;
    public void setOnTrailerClickListener(setOnTrailerClickListener listener) {
        this.trailerListener = listener;
    }
    private ArrayList<Movie> movies;

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_movie_card, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvInfo.setText(movie.getInfo());
        holder.sivPoster.setImageResource(movie.getPosterId());

        holder.bBook.setOnClickListener(v -> {
            bookListener.onBookClick(movie);
        });

        holder.bTrailer.setOnClickListener(v -> {
            trailerListener.onTrailerClick(movie);
        });

        if ("coming_soon".equals(movie.getType())) {
            holder.bBook.setEnabled(false);
            holder.bBook.setText("Coming Soon");
            holder.bBook.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#590909")));
            holder.bBook.setTextColor(Color.parseColor("#767676"));
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView sivPoster;
        TextView tvTitle, tvInfo;
        Button bBook, bTrailer;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            sivPoster = itemView.findViewById(R.id.sivMoviePoster);
            tvTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvInfo = itemView.findViewById(R.id.tvMovieInfo);
            bBook = itemView.findViewById(R.id.bBook);
            bTrailer = itemView.findViewById(R.id.bTrailer);
        }
    }
}

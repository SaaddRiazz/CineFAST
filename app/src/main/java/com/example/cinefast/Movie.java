package com.example.cinefast;

import android.content.Context;

public class Movie {
    private String title, info, trailerLink, type, posterName;

    public Movie(String title, String info, String posterName, String trailerLink, String type) {
        this.title = title;
        this.info = info;
        this.posterName = posterName;
        this.trailerLink = trailerLink;
        this.type = type;
    }

    public String getTitle() { return title; }
    public String getInfo() { return info; }
    public String getTrailerLink() { return trailerLink; }
    public String getType() { return type; }
    public int getPosterId(Context context) { return context.getResources().getIdentifier(posterName, "drawable", context.getPackageName()); }
}

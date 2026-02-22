package com.example.cinefast;

import android.widget.ImageView;

public class Movie {
    private String title;
    private String info;
    private int posterId;
    private String trailerLink;
    private String date;

    public Movie(String title, String info, int posterId, String trailerLink, String date){
        this.title = title;
        this.info = info;
        this.posterId = posterId;
        this.trailerLink = trailerLink;
        this.date = date;
    }

    public String getDate() {
        return date;
    }
    public String getTrailerLink() {
        return trailerLink;
    }
    public String getTitle(){
        return this.title;
    }
    public String getInfo(){
        return this.info;
    }
    public int getPosterId(){
        return posterId;
    }
}

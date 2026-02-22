package com.example.cinefast;

public class Snack {
    private String name;
    private String info;
    private float price;
    private int posterId;

    public Snack(String name, String info, float price, int posterId){
        this.name = name;
        this.info = info;
        this.price = price;
        this.posterId = posterId;
    }

    public String getName(){
        return this.name;
    }
    public String getInfo(){
        return this.info;
    }
    public float getPrice(){
        return this.price;
    }
    public int getPosterId(){
        return this.posterId;
    }
}

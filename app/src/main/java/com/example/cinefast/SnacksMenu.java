package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class SnacksMenu extends AppCompatActivity {

    ArrayList<Snack> snacks = new ArrayList<>();
    ArrayList<SnacksCardView> selectedSnacks = new ArrayList<>();
    LinearLayout snacksContainer;
    Button bBookConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_snacks_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Intent i = getIntent();

        String movieTitle = i.getStringExtra("movie_title_key");
        int moviePoster = i.getIntExtra("movie_poster_key", -1);
        ArrayList<String> selectedSeats = i.getStringArrayListExtra("selected_seats_key");

        storeSnacks();
        displaySnacks();

        bBookConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SnacksMenu.this, Booking.class);

                i.putExtra("movie_title_key", movieTitle);
                i.putExtra("movie_poster_key", moviePoster);
                i.putExtra("selected_seats_key", selectedSeats);
                i.putExtra("selected_snacks_key", formatSelectedSnacksIntoString());

                startActivity(i);
            }
        });
    }

    private ArrayList<String> formatSelectedSnacksIntoString(){
        ArrayList<String> formattedSelectedSnacks = new ArrayList<>();

        for(SnacksCardView scv : selectedSnacks){
            String snackString = "x"+scv.getItemQuantity();
            snackString += " "+scv.getItemName();
            snackString += " ("+scv.getItemInfo();
            snackString += ")#"+String.format("%.2f", (scv.getItemQuantity()* scv.getItemPrice()));

            formattedSelectedSnacks.add(snackString);
        }

        return formattedSelectedSnacks;
    }

    private void displaySnacks(){
        for (Snack snack : snacks){
            SnacksCardView scv = new SnacksCardView(this);
            scv.setItemName(snack.getName());
            scv.setItemInfo(snack.getInfo());
            scv.setItemPrice(snack.getPrice());
            scv.setItemPoster(snack.getPosterId());

            scv.setOnAddItemClickListener(new SnacksCardView.OnAddItemClickListener() {
                @Override
                public void onAddItemClick() {
                    scv.setItemQuantity(scv.getItemQuantity()+1);
                    if(scv.getItemQuantity()==1){
                        selectedSnacks.add(scv);
                    }
                }
            });

            scv.setOnRemoveItemClickListener(new SnacksCardView.OnRemoveItemClickListener() {
                @Override
                public void onRemoveItemClick() {
                    scv.setItemQuantity(scv.getItemQuantity()-1);
                    if(scv.getItemQuantity()==0){
                        selectedSnacks.remove(scv);
                    }
                }
            });

            snacksContainer.addView(scv);
        }
    }

    private void storeSnacks(){
        snacks.add(new Snack("Popcorn", "Large / Buttered", 8.99f, R.drawable.popcorn));
        snacks.add(new Snack("Nachos", "With Cheese Dip", 7.99f, R.drawable.nachos));
        snacks.add(new Snack("Soft Drinks", "Large / Any Flavor", 5.99f, R.drawable.drinks));
        snacks.add(new Snack("Candy Mix", "Assorted Candies", 6.99f, R.drawable.candy));
    }

    private void init(){
        snacksContainer = findViewById(R.id.llSnacksCont);
        bBookConfirm=findViewById(R.id.bBookConfirm);
    }
}
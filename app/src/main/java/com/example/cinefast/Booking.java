package com.example.cinefast;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class Booking extends AppCompatActivity {

    LinearLayout llBookedSeats, llBoughtSnacks;
    ShapeableImageView sivMoviePoster;
    TextView tvMovieTitle, tvTotalPrice;
    Button bBack, bSendTicket;
    String bookingSummary;

    float totalPrice=0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bSendTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+
                        "?subject="+
                        "My CineFAST Ticket - "+tvMovieTitle.getText()+
                        "&body="+
                        bookingSummary));

                Intent intentWA = new Intent(Intent.ACTION_VIEW);
                intentWA.setData(Uri.parse("https://wa.me/" + "?text=" + bookingSummary));

                List<Intent> targetedIntents = new ArrayList<>();
                targetedIntents.add(intentMail);
                targetedIntents.add(intentWA);

                Intent chooser = Intent.createChooser(targetedIntents.remove(0), "Send Ticket via");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedIntents.toArray(new Parcelable[]{}));
                startActivity(chooser);
            }
        });

        Intent i = getIntent();

        tvMovieTitle.setText(i.getStringExtra("movie_title_key"));
        sivMoviePoster.setImageResource(i.getIntExtra("movie_poster_key", -1));

        bookingSummary=tvMovieTitle.getText().toString()+" - Booking Summary";

        ArrayList<String> seats = i.getStringArrayListExtra("selected_seats_key");
        ArrayList<String> snacks = i.getStringArrayListExtra("selected_snacks_key");
        generateTicketDetails(seats);
        if(snacks != null)
            generateSnacksDetails(snacks);
        tvTotalPrice.setText(String.format("$%.2f", totalPrice));

        bookingSummary+= "\n\n----------------------------------------------------------\n\n" +
                "TOTAL                " +
                String.format("$%.2f", totalPrice);
    }

    private void generateTicketDetails(ArrayList<String> seats){

        bookingSummary+="\n\n\t***Tickets***\n";
        for(String seat : seats) {
            LinearLayout ticket = new LinearLayout(this);
            ticket.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView info = new TextView(this);
            info.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            info.setText(seat);
            info.setTextColor(Color.parseColor("#727886"));
            info.setTypeface(ResourcesCompat.getFont(this, R.font.poppins));

            View separator = new View(this);
            separator.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1));

            TextView price = new TextView(this);
            price.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            price.setText("$9.99");
            totalPrice+=9.99f;
            price.setTextColor(Color.parseColor("#727886"));
            price.setTypeface(ResourcesCompat.getFont(this, R.font.poppins));

            ticket.addView(info);
            ticket.addView(separator);
            ticket.addView(price);

            llBookedSeats.addView(ticket);

            bookingSummary+="\n\t\t"+info.getText().toString()+"    "+price.getText().toString();
        }
    }

    private void generateSnacksDetails(ArrayList<String> snacks){

        bookingSummary+="\n\n\t***Snacks***\n";

        for(String snack : snacks) {
            String snackInfo = snack.split("#")[0], snackPrice = snack.split("#")[1];

            LinearLayout item = new LinearLayout(this);
            item.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView info = new TextView(this);
            info.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            info.setText(snackInfo);
            info.setTextColor(Color.parseColor("#727886"));
            info.setTypeface(ResourcesCompat.getFont(this, R.font.poppins));

            View separator = new View(this);
            separator.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1));

            TextView price = new TextView(this);
            price.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            price.setText("$"+snackPrice);
            totalPrice+=Float.parseFloat(snackPrice);
            price.setTextColor(Color.parseColor("#727886"));
            price.setTypeface(ResourcesCompat.getFont(this, R.font.poppins));

            item.addView(info);
            item.addView(separator);
            item.addView(price);

            llBoughtSnacks.setVisibility(View.VISIBLE);
            findViewById(R.id.tvSnacksText).setVisibility(View.VISIBLE);
            llBoughtSnacks.addView(item);

            bookingSummary+="\n\t\t"+info.getText().toString()+"    "+price.getText().toString();
        }
    }

    private void init(){
        llBookedSeats=findViewById(R.id.llBookedSeats);
        llBoughtSnacks=findViewById(R.id.llBoughtSnacks);
        bBack=findViewById(R.id.bBack);
        bSendTicket=findViewById(R.id.bSendTicket);
        tvMovieTitle=findViewById(R.id.tvMovieTitle);
        sivMoviePoster=findViewById(R.id.sivMoviePoster);
        tvTotalPrice=findViewById(R.id.tvTotalPrice);
    }
}
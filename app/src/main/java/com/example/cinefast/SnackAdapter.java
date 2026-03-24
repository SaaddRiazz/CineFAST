package com.example.cinefast;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SnackAdapter extends ArrayAdapter<Snack> {
    ArrayList<SnacksCardView> cardViews = new ArrayList<>();
    ArrayList<SnacksCardView> selectedSnacks;

    public SnackAdapter(Context context, ArrayList<Snack> snacks, ArrayList<SnacksCardView> selectedSnacks) {
        super(context, 0, snacks);
        this.selectedSnacks = selectedSnacks;

        for (Snack snack : snacks) {
            SnacksCardView scv = new SnacksCardView(context);
            scv.setItemName(snack.getName());
            scv.setItemInfo(snack.getInfo());
            scv.setItemPrice(snack.getPrice());
            scv.setItemPoster(snack.getPosterId());

            scv.setOnAddItemClickListener(() -> {
                scv.setItemQuantity(scv.getItemQuantity() + 1);
                if (scv.getItemQuantity() == 1) {
                    selectedSnacks.add(scv);
                }
            });

            scv.setOnRemoveItemClickListener(() -> {
                scv.setItemQuantity(scv.getItemQuantity() - 1);
                if (scv.getItemQuantity() == 0) {
                    selectedSnacks.remove(scv);
                }
            });

            cardViews.add(scv);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return cardViews.get(position);
    }
}

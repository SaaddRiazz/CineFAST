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

public class SnacksCardView extends LinearLayout {

    private ShapeableImageView sivPoster;
    private TextView tvName, tvInfo, tvPrice, tvItemQuantity;
    private Button bAddItem, bRemoveItem;

    private String itemName, itemInfo;
    private float itemPrice;
    private int itemQuantity = 0, itemPoster;

    public interface OnAddItemClickListener {
        void onAddItemClick();
    }
    public interface OnRemoveItemClickListener {
        void onRemoveItemClick();
    }
    private OnAddItemClickListener addListener;
    private OnRemoveItemClickListener remListener;
    public void setOnAddItemClickListener(OnAddItemClickListener listener) {
        this.addListener = listener;
    }
    public void setOnRemoveItemClickListener(OnRemoveItemClickListener listener) {
        this.remListener = listener;
    }

    public SnacksCardView(Context context) {
        super(context);
        init(context);
    }

    public SnacksCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SnacksCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_snacks_card, this, true);

        sivPoster = findViewById(R.id.sivItemPoster);
        tvName = findViewById(R.id.tvItemName);
        tvInfo = findViewById(R.id.tvItemInfo);
        tvPrice = findViewById(R.id.tvItemPrice);
        tvItemQuantity = findViewById(R.id.tvItemQuantity);
        bAddItem = findViewById(R.id.bAddItem);
        bRemoveItem = findViewById(R.id.bRemoveItem);

        bAddItem.setOnClickListener(v -> {
            if (addListener != null) {
                addListener.onAddItemClick();
            }

            if(itemQuantity==1) {
                bRemoveItem.setEnabled(true);
                bRemoveItem.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(
                                android.graphics.Color.parseColor("#EE0000")
                        )
                );
                bRemoveItem.setTextColor(android.graphics.Color.parseColor("#FFFFFF"));
            }
        });

        bRemoveItem.setOnClickListener(v -> {
            if (remListener != null) {
                remListener.onRemoveItemClick();
            }

            if(itemQuantity==0) {
                bRemoveItem.setEnabled(false);
                bRemoveItem.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(
                                android.graphics.Color.parseColor("#590909")
                        )
                );
                bRemoveItem.setTextColor(android.graphics.Color.parseColor("#767676"));
            }
        });
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
        tvName.setText(itemName);
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
        tvInfo.setText(itemInfo);
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
        tvPrice.setText("$"+itemPrice);
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
        tvItemQuantity.setText(""+itemQuantity);
    }

    public int getItemPoster() {
        return itemPoster;
    }

    public void setItemPoster(int itemPoster) {
        this.itemPoster = itemPoster;
        sivPoster.setImageResource(itemPoster);
    }
}

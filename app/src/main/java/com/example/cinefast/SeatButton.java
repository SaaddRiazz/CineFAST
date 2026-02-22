package com.example.cinefast;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageButton;

public class SeatButton extends AppCompatImageButton {

    private String status;
    private String row;
    private int seatNumber;

    public SeatButton(Context context) {
        super(context);
    }

    public SeatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setValues(context, attrs);
    }

    public SeatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setValues(context, attrs);
    }

    private void setValues(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.SeatButton);

        status = arr.getString(R.styleable.SeatButton_seatStatus);
        row = arr.getString(R.styleable.SeatButton_row);
        seatNumber = arr.getInt(R.styleable.SeatButton_seatNumber, 0);

        arr.recycle();
    }

    public String getStatus() {
        return status;
    }

    public String getRow() {
        return row;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}



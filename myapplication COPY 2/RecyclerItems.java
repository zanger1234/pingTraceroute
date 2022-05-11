package com.example.myapplication;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerItems {
    String textViewNumber;
    Bitmap imageViewStatusPing;
    String textViewIp;
    String textViewTime;

    public RecyclerItems(String textViewNumber, Bitmap imageViewStatusPing, String textViewIp, String textViewTime) {
        this.textViewNumber = textViewNumber;
        this.imageViewStatusPing = imageViewStatusPing;
        this.textViewIp = textViewIp;
        this.textViewTime = textViewTime;
    }
}

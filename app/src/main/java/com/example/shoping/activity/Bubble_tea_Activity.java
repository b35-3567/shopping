package com.example.shoping.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.shoping.R;

public class Bubble_tea_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_tea);
        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            int position = intent.getIntExtra("position", -1);
            if (position != -1) {

            }
        }

    }
}
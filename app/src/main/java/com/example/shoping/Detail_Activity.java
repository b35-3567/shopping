package com.example.shoping;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Detail_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);
        // Get the selected item from the intent
        String selectedItem = getIntent().getStringExtra("selectedItem");

        // Display the selected item in the detail activity
        TextView textView = findViewById(R.id.detailTextView);
        textView.setText(selectedItem);
    }
}
package com.example.shoping.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.shoping.R;
import androidx.fragment.app.Fragment;

public class ProductDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        // Nhận dữ liệu sản phẩm từ Intent
        String product = getIntent().getStringExtra("product");

        // Hiển thị phần màn hình chỉ một nửa ban đầu
        showHalfScreen();

        // Xử lý sự kiện khi phần màn hình chỉ một nửa được nhấn
        findViewById(R.id.halfScreenContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang hiển thị phần màn hình đầy đủ khi nhấn vào một nửa
                showFullScreen();
            }
        });


    }

    private void showHalfScreen() {
        findViewById(R.id.halfScreenContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.fullScreenContainer).setVisibility(View.GONE);
    }

    private void showFullScreen() {
        findViewById(R.id.halfScreenContainer).setVisibility(View.GONE);
        findViewById(R.id.fullScreenContainer).setVisibility(View.VISIBLE);
    }
}
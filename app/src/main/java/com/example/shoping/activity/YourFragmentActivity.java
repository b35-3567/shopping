package com.example.shoping.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoping.R;
import com.example.shoping.fragment.Fragment_ship;

public class YourFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_fragment);
        // Thêm Fragment vào FragmentContainer
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Fragment_ship())
                .commit();
    }
    @Override
    public void onBackPressed() {
        // Thực hiện các thao tác cần thiết để thêm sản phẩm vào giỏ hàng

        // Gửi kết quả trả về AcceptActivity
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);

        // Đóng YourFragmentActivity và trở về AcceptActivity
        finish();
    }
}
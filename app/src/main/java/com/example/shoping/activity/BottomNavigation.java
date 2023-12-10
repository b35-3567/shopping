package com.example.shoping.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import com.example.shoping.R;
import com.example.shoping.fragment.Fragment_cart;
import com.example.shoping.fragment.Fragment_home;
import com.example.shoping.fragment.Fragment_me;
import com.example.shoping.fragment.Fragment_sale;
import com.example.shoping.fragment.Fragment_ship;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavigation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_view);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        BottomNavigationView bottomNavigationView =findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Fragment fragment ;
                if(item.getItemId() == R.id.home){
                   fragment =new Fragment_home();
                }else if(item.getItemId() == R.id.sale){
                    fragment =new Fragment_sale();
               }else if(item.getItemId() == R.id.cart){
                   fragment =new Fragment_cart();
               }else if(item.getItemId() == R.id.delivery) {
                    fragment = new Fragment_ship();
                }else {
                    fragment = new Fragment_me();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout1,fragment)
                        .commit();
                // Đặt tiêu đề của Toolbar
                toolbar.setTitle(item.getTitle());
            }
        });


    }
}
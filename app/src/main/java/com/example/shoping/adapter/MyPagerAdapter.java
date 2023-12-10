package com.example.shoping.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyPagerAdapter extends FragmentStateAdapter {

    public MyPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Tạo và trả về Fragment tương ứng với vị trí
        return new MyFragment();
    }

    @Override
    public int getItemCount() {
        // Số lượng Fragment trong ViewPager
        return 3; // Sửa thành số lượng Fragment mong muốn
    }
}
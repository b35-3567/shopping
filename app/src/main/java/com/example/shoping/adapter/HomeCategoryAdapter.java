package com.example.shoping.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoping.R;
import com.example.shoping.activity.Bubble_tea_Activity;
import com.example.shoping.fragment.Bubble_teaFragment;
import com.example.shoping.fragment.MilkFragment;
import com.example.shoping.model.HomeCategoryModel;

import java.util.ArrayList;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<HomeCategoryModel>list;

    public HomeCategoryAdapter(Context context, ArrayList<HomeCategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.home_category_item,parent,false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull HomeCategoryAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.catImg);
        holder.name.setText(list.get(position).getName());

        // Thêm OnClickListener cho mỗi item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                switch (clickedPosition) {
                    case 0:
                        // Hiển thị Bubble_teaFragment khi nhấn vào item 0
                        Bubble_teaFragment fragment1 = new Bubble_teaFragment();
                        // Truyền dữ liệu nếu cần
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("position", clickedPosition);
                        fragment1.setArguments(bundle1);
                        // Thay thế fragment hiện tại bằng Bubble_teaFragment
                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout_Product, fragment1)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 1:
                        // Hiển thị MilkFragment khi nhấn vào item 1
                        MilkFragment fragment2 = new MilkFragment();
                        // Truyền dữ liệu nếu cần
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("position", clickedPosition);
                        fragment2.setArguments(bundle2);
                        // Thay thế fragment hiện tại bằng MilkFragment
                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout_Product, fragment2)
                                .addToBackStack(null)
                                .commit();
                        break;
                    // Thêm các case khác tương ứng với các fragment khác

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView catImg;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
          catImg=itemView.findViewById(R.id.home_cat_img);
          name=itemView.findViewById(R.id.home_cat_name);
        }
    }
}

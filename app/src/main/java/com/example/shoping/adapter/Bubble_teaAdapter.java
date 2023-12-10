package com.example.shoping.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoping.R;
import com.example.shoping.activity.DetailActivity;
import com.example.shoping.model.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Bubble_teaAdapter extends RecyclerView.Adapter<Bubble_teaAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ProductModel> list;

    public Bubble_teaAdapter(Context context, ArrayList<ProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Bubble_teaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.item_bubble_tea_fragment,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Bubble_teaAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.bubbletea_img);
        holder.bubbletea_name.setText(list.get(position).getName());
        // holder.description.setText(list.get(position).getDescription());
        holder.bubbletea_price.setText(Integer.toString(list.get(position).getPrice()));

        // Khai báo final cho position
        final int itemPosition = position;
        holder.bubbletea_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the product ID of the clicked item
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("detail",list.get(itemPosition));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bubbletea_img,bubbletea_plus;
        TextView bubbletea_price, bubbletea_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bubbletea_img=itemView.findViewById(R.id.bubbletea_img);
            bubbletea_name=itemView.findViewById(R.id.bubbletea_name);
            //description=itemView.findViewById(R.id.milk_des);
            bubbletea_price=itemView.findViewById(R.id.bubbletea_price);
            bubbletea_plus=itemView.findViewById(R.id.bubbletea_plus);
        }
    }
}

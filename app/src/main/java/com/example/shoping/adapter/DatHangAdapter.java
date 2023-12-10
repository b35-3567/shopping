package com.example.shoping.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoping.R;
import com.example.shoping.model.MyCartModel;

import java.util.ArrayList;

public class DatHangAdapter extends RecyclerView.Adapter<DatHangAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MyCartModel> cartList;

    // Constructor to pass the context and cartList
    public DatHangAdapter(Context context, ArrayList<MyCartModel> cartList) {
        this.context = context;
        this.cartList = cartList;
    }
    @NonNull
    @Override
    public DatHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.item_dat_hang,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DatHangAdapter.ViewHolder holder, int position) {
        MyCartModel currentItem = cartList.get(position);
        Glide.with(context).load(cartList.get(position).getImg_url()).into(holder.imageVieW);
        // Set data to views in the ViewHolder
        holder.productNameTextView.setText(currentItem.getProductName());
        holder.quantityTextView.setText("Số lượng: " + currentItem.getQuantity());
        holder.sizeTextView.setText("Size: " + currentItem.getProductSize());
        holder.priceTextView.setText("Giá: " + currentItem.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView priceTextView,sizeTextView,quantityTextView,productNameTextView;
        ImageView imageVieW;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            sizeTextView = itemView.findViewById(R.id.sizeTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            imageVieW = itemView.findViewById(R.id.imageVieW);
        }
    }
}

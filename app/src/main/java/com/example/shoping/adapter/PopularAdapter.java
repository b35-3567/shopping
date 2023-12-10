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
import com.example.shoping.model.Popular;


import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder>{
private Context context;
private ArrayList<Popular> list;

    public PopularAdapter(Context context, ArrayList<Popular> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.popular_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.img_url);
        holder.name.setText(list.get(position).getName());
        holder.description.setText(list.get(position).getDescription());
        holder.discount.setText(list.get(position).getDiscount());
        holder.rating.setText(list.get(position).getRating());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_url;
        TextView name, description,rating,discount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_url=itemView.findViewById(R.id.pop_img);
             name=itemView.findViewById(R.id.pop_name);
            description=itemView.findViewById(R.id.pop_des);
            rating=itemView.findViewById(R.id.pos_rate);
            discount=itemView.findViewById(R.id.pos_discount);
        }
    }
}

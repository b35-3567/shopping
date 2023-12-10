package com.example.shoping.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoping.R;
import com.example.shoping.activity.DetailActivity;
import com.example.shoping.model.ProductModel;

import java.util.ArrayList;

public class Milk_Adapter_Fragment extends RecyclerView.Adapter<Milk_Adapter_Fragment.ViewHolder> {
    private Context context;
    private ArrayList<ProductModel> list;

    public Milk_Adapter_Fragment(Context context, ArrayList<ProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Milk_Adapter_Fragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.item_milk_fragment,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Milk_Adapter_Fragment.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.img_url);
        holder.name.setText(list.get(position).getName());
       // holder.description.setText(list.get(position).getDescription());
        holder.price.setText(Integer.toString(list.get(position).getPrice()));

        // Khai báo final cho position
        final int itemPosition = position;
        holder.milk_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        ImageView img_url,milk_plus;
        TextView name, description,price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_url=itemView.findViewById(R.id.milk_img);
            name=itemView.findViewById(R.id.milk_name);
            //description=itemView.findViewById(R.id.milk_des);
            price=itemView.findViewById(R.id.milk_price);
            milk_plus=itemView.findViewById(R.id.milk_plus);
        }
    }
}

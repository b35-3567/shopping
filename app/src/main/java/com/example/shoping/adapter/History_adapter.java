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
import com.example.shoping.activity.Detail_Bill_Activity;
import com.example.shoping.model.OrderHistoryModel;

import java.util.ArrayList;

public class History_adapter extends RecyclerView.Adapter<History_adapter.ViewHolder> {
    private Context context;

    public History_adapter(Context context, ArrayList<OrderHistoryModel> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    private ArrayList<OrderHistoryModel> historyList;

    @NonNull
    @Override
    public History_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.item_history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull History_adapter.ViewHolder holder, int position) {
        OrderHistoryModel historyModel = historyList.get(position);

        holder.productId.setText("ID: " + historyModel.getId());  // Assuming getId() is the method to get the ID

        // Load image using Glide library
        Glide.with(context).load(historyModel.getProducts().get(0).getImgUrl()).into(holder.imageView4);

        // Set other TextViews accordingly
        holder.productPrice.setText("Price: " + historyModel.getProducts().get(0).getTotalPrice());
        holder.productDate.setText("Date: " + historyModel.getOrderDate());
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // Sử dụng context để tạo Intent và chuyển sang Detail_Bill_Activity
        Intent intent = new Intent(context, Detail_Bill_Activity.class);

        // Truyền ID hoặc thông tin cần thiết khác
        intent.putExtra("orderId", historyModel.getId());

        // Bắt đầu activity mới
        context.startActivity(intent);
    }
});

    }

    @Override
    public int getItemCount() {
        return  historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView4;
        TextView  productPrice,productDate,productId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             imageView4=itemView.findViewById(R.id.imageView4);
            productId=itemView.findViewById(R.id.productId);
            productPrice=itemView.findViewById(R.id.productPrice);
            productDate=itemView.findViewById(R.id.productDate);
        }
    }
}

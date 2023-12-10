package com.example.shoping.adapter;

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
import com.example.shoping.model.OrderHistoryModel;

import java.util.List;

public class Detai_bill_Adapter extends RecyclerView.Adapter<Detai_bill_Adapter.ViewHolder> {
    private Context context;
    private List<OrderHistoryModel.ProductItem> productList;

    public Detai_bill_Adapter(Context context, List<OrderHistoryModel.ProductItem> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public Detai_bill_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_detail_bill, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Detai_bill_Adapter.ViewHolder holder, int position) {
        OrderHistoryModel.ProductItem productItem = productList.get(position);

        // Thiết lập dữ liệu cho các thành phần UI trong ViewHolder
        holder.quantityTextView.setText("Số lượng: " + productItem.getQuantity());
        holder.productNameTextView.setText("Tên sản phẩm: " + productItem.getProductName());
        holder.sizeTextView.setText("Size: " + productItem.getProductSize());
        holder.priceTextView.setText("Giá: " + productItem.getTotalPrice());

        // Load hình ảnh sử dụng Glide hoặc thư viện tương tự
        Glide.with(context).load(productItem.getImgUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateData(List<OrderHistoryModel.ProductItem> newProductList) {
        productList.clear();
        productList.addAll(newProductList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView quantityTextView, productNameTextView, sizeTextView, priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageVieW);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            sizeTextView = itemView.findViewById(R.id.sizeTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }
    }
}

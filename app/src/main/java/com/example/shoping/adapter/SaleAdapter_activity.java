package com.example.shoping.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoping.R;
import com.example.shoping.model.VoucherModel;

import java.util.ArrayList;

public class SaleAdapter_activity extends RecyclerView.Adapter<SaleAdapter_activity.ViewHolder> {
    private Context context;
    private ArrayList<VoucherModel> list;

    public ArrayList<VoucherModel> getVoucherList() {
        return list;
    }
    public SaleAdapter_activity(Context context, ArrayList<VoucherModel> list ) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public SaleAdapter_activity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_voucher_readio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_sale()).into(holder.img_sale);
        holder.name_sale.setText(list.get(position).getName_sale());
        holder.value.setText("Giá trị:" + list.get(position).getValue());
        holder.expirationDate.setText("HSD:" + list.get(position).getExpirationDate());

       VoucherModel voucherItem = list.get(position);
        holder.checkBox.setChecked(voucherItem.isSelected());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                voucherItem.setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_sale;
        TextView name_sale, value, expirationDate;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_sale = itemView.findViewById(R.id.img_sale);
            name_sale = itemView.findViewById(R.id.name_sale);
            value = itemView.findViewById(R.id.value);
            expirationDate = itemView.findViewById(R.id.expirationDate);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}

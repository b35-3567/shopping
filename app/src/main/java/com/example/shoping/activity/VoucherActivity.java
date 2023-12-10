package com.example.shoping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoping.R;
import com.example.shoping.adapter.SaleAdapter_activity;
import com.example.shoping.model.VoucherModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class VoucherActivity extends AppCompatActivity {
    RecyclerView voucher_rec;
    ArrayList<VoucherModel> voucherModelArrayList;
    SaleAdapter_activity saleAdapterActivity;
    FirebaseFirestore db;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        voucher_rec=findViewById(R.id.voucher_rec);
        voucher_rec.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        voucherModelArrayList=new ArrayList<>();
        btnBack = findViewById(R.id.btnBack);
        saleAdapterActivity = new SaleAdapter_activity(this, voucherModelArrayList);
        voucher_rec.setAdapter(saleAdapterActivity);
        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Truy vấn dữ liệu từ Firestore
        loadVoucherData();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trả về danh sách voucher được chọn cho ActivityA
                Intent resultIntent = new Intent();
                resultIntent.putStringArrayListExtra("selectedVouchers", getSelectedVouchers(saleAdapterActivity));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
    private void loadVoucherData() {
        db.collection("Voucher")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Lấy dữ liệu từ Firestore
                            String hsd = document.getString("hsd");
                            String img_sale = document.getString("img_sale");
                            String name_sale = document.getString("name_sale");
                            long value = document.getLong("value");

                            // Tạo đối tượng VoucherModel từ dữ liệu
                            VoucherModel voucherModel = new VoucherModel(img_sale, name_sale, value, hsd);

                            // Thêm vào danh sách
                            voucherModelArrayList.add(voucherModel);
                        }

                        // Cập nhật RecyclerView sau khi có dữ liệu
                        saleAdapterActivity.notifyDataSetChanged();
                    } else {
                        // Xử lý khi truy vấn không thành công
                    }
                });


    }
        private ArrayList<String> getSelectedVouchers(SaleAdapter_activity adapter) {
            ArrayList<String> selectedVouchers = new ArrayList<>();
            for (VoucherModel voucherItem : adapter.getVoucherList()) {
                if (voucherItem.isSelected()) {
                    selectedVouchers.add(voucherItem.getName_sale());
                }
            }
            return selectedVouchers;
        }

    }

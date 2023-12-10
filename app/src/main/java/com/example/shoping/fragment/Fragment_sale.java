package com.example.shoping.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoping.R;
import com.example.shoping.adapter.SaleAdapter;
import com.example.shoping.model.VoucherModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class Fragment_sale extends Fragment {
    RecyclerView voucher_rec;
    ArrayList<VoucherModel> voucherModelArrayList;
    SaleAdapter saleAdapter;
    FirebaseFirestore db;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_sale, container, false);

       voucher_rec=view.findViewById(R.id.voucher_rec);
       voucher_rec.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        voucherModelArrayList=new ArrayList<>();
        saleAdapter=new SaleAdapter(getContext(),voucherModelArrayList);
        voucher_rec.setAdapter(saleAdapter);
        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Truy vấn dữ liệu từ Firestore
        loadVoucherData();

        return view;
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
                        saleAdapter.notifyDataSetChanged();
                    } else {
                        // Xử lý khi truy vấn không thành công
                    }
                });
    }
}
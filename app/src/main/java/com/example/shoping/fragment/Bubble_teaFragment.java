package com.example.shoping.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoping.DbHelper.FirestoreHelper;
import com.example.shoping.R;
import com.example.shoping.adapter.Bubble_teaAdapter;
import com.example.shoping.adapter.Milk_Adapter_Fragment;
import com.example.shoping.model.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Bubble_teaFragment extends Fragment {

    ArrayList<ProductModel> productModelArrayList;
    Bubble_teaAdapter bubbleTeaAdapter;
    RecyclerView bubble_rec;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

            View view=    inflater.inflate(R.layout.fragment_bubble_tea, container, false);
        // Lấy dữ liệu từ Bundle (nếu có)

        Bundle bundle = getArguments();
        if (bundle != null) {
            int position = bundle.getInt("position", -1);
            // Thực hiện các xử lý tương ứng với vị trí được chọn từ RecyclerView
        }

        // Initialize FirebaseFirestore instance
        db = FirebaseFirestore.getInstance();
       bubble_rec=view.findViewById(R.id.bubble_rec);
        bubble_rec.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        productModelArrayList=new ArrayList<>();
        bubbleTeaAdapter =new Bubble_teaAdapter(getContext(),productModelArrayList);
        bubble_rec.setAdapter(bubbleTeaAdapter);
        // Lấy dữ liệu từ Firestore với điều kiện type == "milk"
        db.collection("Products")
                .whereEqualTo("type", "bubble_tea")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Convert dữ liệu từ Firestore thành đối tượng ProductModel
                                ProductModel product = document.toObject(ProductModel.class);

                                // Set the document ID
                                product.setDocumentId(document.getId());

                                // Thêm đối tượng vào danh sách
                                productModelArrayList.add(product);

                            }

                            // Cập nhật dữ liệu trong adapter khi đã lấy được dữ liệu
                            bubbleTeaAdapter.notifyDataSetChanged();
                        } else {
                            // Xử lý lỗi nếu có
                            Log.e("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });


        return view;
    }
}
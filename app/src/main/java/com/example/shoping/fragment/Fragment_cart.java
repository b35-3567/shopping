package com.example.shoping.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoping.R;
import com.example.shoping.activity.AcceptActivity;
import com.example.shoping.adapter.CartAdapter;
import com.example.shoping.model.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Fragment_cart extends Fragment {
RecyclerView cart_rec;
Button btn_dathang;
CartAdapter cartAdapter;
ArrayList<MyCartModel> list;
    FirebaseFirestore db;;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart, container, false);
        btn_dathang=view.findViewById(R.id.dat_hang);

        btn_dathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AcceptActivity.class));
                             }
        });
        cart_rec=view.findViewById(R.id.cart_rec);
        cart_rec.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        list=new ArrayList<>();
        cartAdapter=new CartAdapter(getContext(),list);
        cart_rec.setAdapter(cartAdapter);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        // Thay đổi đoạn code trong Fragment_cart
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list.clear(); // Clear the list before adding new items
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                                list.add(cartModel);
                            }
                            cartAdapter.notifyDataSetChanged(); // Notify the adapter about the change
                        }
                    }
                });



        return view;
    }

    }

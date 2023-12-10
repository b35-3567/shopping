package com.example.shoping.fragment;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoping.R;
import com.example.shoping.adapter.HomeCategoryAdapter;
import com.example.shoping.model.HomeCategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Fragment_ship extends Fragment {
    FirebaseFirestore db;

    //HOme category
    ArrayList<HomeCategoryModel>categoryModelArrayList;
    HomeCategoryAdapter homeCategoryAdapter;
    RecyclerView exp_rec;

    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_ship,container,false);
        // Tham chiếu đến ProgressBar
        progressBar = view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
        exp_rec=view.findViewById(R.id.explore_rec);
        exp_rec.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        categoryModelArrayList=new ArrayList<>();
        homeCategoryAdapter=new HomeCategoryAdapter(getContext(),categoryModelArrayList);
        exp_rec.setAdapter(homeCategoryAdapter);
        // Tải dữ liệu HomeCategory
        progressBar.setVisibility(View.VISIBLE);
        // Initialize FirebaseFirestore instance
        db = FirebaseFirestore.getInstance();
        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE); // Ẩn ProgressBar sau khi tải xong
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomeCategoryModel homeCategoryModel=document.toObject(HomeCategoryModel.class);
                                categoryModelArrayList.add(homeCategoryModel);
                                homeCategoryAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Toast.makeText(getActivity(), "ERROR!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return view;
    }
}
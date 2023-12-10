package com.example.shoping.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.shoping.LoginActivity;
import com.example.shoping.R;
import com.example.shoping.activity.ChangePassActivity;
import com.example.shoping.activity.HistoryActivity;
import com.google.firebase.auth.FirebaseAuth;


public class Fragment_me extends Fragment {
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_me, container, false);
        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        Button btn_lichsu=view.findViewById(R.id.btn_lichsu);
        Button btn_doimk=view.findViewById(R.id.btn_doimk);
        Button  btn_logout=view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        btn_doimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangePassActivity.class));
            }
        });
        btn_lichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HistoryActivity.class));
            }
        });
      return view;
    }
    private void signOut() {
        mAuth.signOut();

        // Sau khi đăng xuất, chuyển người dùng đến màn hình đăng nhập hoặc màn hình khác tùy thuộc vào thiết kế của ứng dụng
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // Kết thúc và đóng Activity chứa Fragment
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
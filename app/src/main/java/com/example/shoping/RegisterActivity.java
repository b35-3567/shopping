package com.example.shoping;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.shoping.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
TextView signIn;
 EditText name, email1, phone1, password,diachi;
Button signUp;
 FirebaseAuth auth;
 FirebaseDatabase database;
 ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressBar=findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
// Initialize Firebase
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        signIn=findViewById(R.id.sign_in);
        name=findViewById(R.id.name);
        email1=findViewById(R.id.email);
        password=findViewById(R.id.pass);
        phone1=findViewById(R.id.phone);
       diachi=findViewById(R.id.Diachi);
        signUp=findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 creatUser();
                 progressBar.setVisibility(View.VISIBLE);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    private void creatUser() {
        String username=name.getText().toString();
        String email=email1.getText().toString();
        String pass=password.getText().toString();
        String phone=phone1.getText().toString();
        String location=diachi.getText().toString();
        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "họ tên trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "số điện thoại  trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "mật khẩu trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.length()<6){
            Toast.makeText(this, "mật khẩu  phải 6 ký tự trở lên", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(location)){
            Toast.makeText(this, "địa chỉ trống", Toast.LENGTH_SHORT).show();
            return;
        }
        //create user
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Người dùng được tạo thành công

                            if (auth != null) {
                                String uid = auth.getUid(); // Lấy UID của người dùng mới
                                // Lưu thông tin người dùng vào Firebase Realtime Database
                                User user = new User(username, email, phone, pass, location);
                                database.getReference().child("Users").child(uid).setValue(user);
                                // Hiển thị thông báo đăng ký thành công hoặc điều chỉnh giao diện của bạn ở đây
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                // FirebaseUser is null, xử lý lỗi ở đây nếu cần
                            }
                        } else {
                            // Đăng ký thất bại, xử lý lỗi ở đây nếu cần
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


}
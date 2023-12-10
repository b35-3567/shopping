package com.example.shoping.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassActivity extends AppCompatActivity {

    private EditText oldPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private Button changePasswordButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        // Khởi tạo FirebaseAuth và FirebaseDatabase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        // Lấy người dùng hiện tại
        currentUser = mAuth.getCurrentUser();

        // Ánh xạ các thành phần UI
        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        // Thiết lập sự kiện click cho nút thay đổi mật khẩu
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String oldPassword = oldPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Kiểm tra xác nhận mật khẩu
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(ChangePassActivity.this, "Xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thay đổi mật khẩu trong Firebase Authentication
        currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Mật khẩu đã được thay đổi thành công

                    // Thay đổi mật khẩu trong Realtime Database
                    updatePasswordInDatabase(currentUser.getUid(), newPassword);

                    Toast.makeText(ChangePassActivity.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    finish(); // Kết thúc hoạt động sau khi thay đổi mật khẩu
                } else {
                    // Đã xảy ra lỗi trong quá trình thay đổi mật khẩu
                    Toast.makeText(ChangePassActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePasswordInDatabase(String userId, String newPassword) {
        // Thay đổi mật khẩu trong Realtime Database
        DatabaseReference userRef = mDatabase.getReference("Users").child(userId);
        userRef.child("pass").setValue(newPassword);
    }
}

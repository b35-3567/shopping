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

 import com.example.shoping.activity.BottomNavigation;
 import com.google.android.gms.tasks.OnCompleteListener;
 import com.google.android.gms.tasks.Task;
 import com.google.firebase.auth.AuthResult;
 import com.google.firebase.auth.FirebaseAuth;

 public class LoginActivity extends AppCompatActivity {
Button sign_in;
EditText edtPass,edtEmail;
TextView txtSign_up;
FirebaseAuth auth;
     ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar=findViewById(R.id.progress_bar1);
        progressBar.setVisibility(View.GONE);
        auth=FirebaseAuth.getInstance();
edtPass=findViewById(R.id.pass);
edtEmail=findViewById(R.id.email);
sign_in=findViewById(R.id.sign_in);
txtSign_up=findViewById(R.id.sign_up);
txtSign_up.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
});
sign_in.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
loginUser();
        progressBar.setVisibility(View.VISIBLE);
    }
});
    }

     private void loginUser() {
         String  email=edtEmail.getText().toString();
         String pass=edtPass.getText().toString();

         if(TextUtils.isEmpty(email)){
             Toast.makeText(this, "email trống!", Toast.LENGTH_SHORT).show();
             return;
         }
         if(TextUtils.isEmpty(pass)){
             Toast.makeText(this, "mật khẩu trống!", Toast.LENGTH_SHORT).show();
             return;
         }
         if (pass.length()<6){
             Toast.makeText(this, "mật khẩu  phải 6 ký tự trở lên", Toast.LENGTH_SHORT).show();
         }
auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "đăng nhập thnahf công", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, BottomNavigation.class));
        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "đăng nhập thât bại", Toast.LENGTH_SHORT).show();
        }
    }
});
     }
 }
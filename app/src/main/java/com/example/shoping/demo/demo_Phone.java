package com.example.shoping.demo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoping.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class demo_Phone extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText phoneNumber, verificationCode;
    private Button sendCodeButton, verifyButton;
    private String mVerificationId;  // Variable to store verification ID
    private PhoneAuthProvider.ForceResendingToken mResendToken; // Variable to store resending token

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_phone);

        mAuth = FirebaseAuth.getInstance();
        phoneNumber = findViewById(R.id.phoneNumber);
        verificationCode = findViewById(R.id.verificationCode);
        sendCodeButton = findViewById(R.id.sendCodeButton);
        verifyButton = findViewById(R.id.verifyButton);

        sendCodeButton.setOnClickListener(v -> sendVerificationCode());
        verifyButton.setOnClickListener(v -> verifySignInCode());
    }

    private void sendVerificationCode() {
        String phone = phoneNumber.getText().toString().trim();
        if (phone.isEmpty()) {
            phoneNumber.setError("Phone number is required");
            phoneNumber.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,           // Timeout duration
                TimeUnit.SECONDS,
                this,         // Activity (for callback binding)
                mCallbacks);  // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(demo_Phone.this, "Invalid request", Toast.LENGTH_LONG).show();
            } else {
               // Toast.makeText(demo_Phone.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Verification", "Verification failed: " + e.getMessage());
            }


        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId, token);
            mVerificationId = verificationId;
            mResendToken = token;
        }
    };

    private void verifySignInCode() {
        String code = verificationCode.getText().toString().trim();
        if (code.isEmpty()) {
            verificationCode.setError("Verification code is required");
            verificationCode.requestFocus();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(demo_Phone.this, "Login Successful", Toast.LENGTH_LONG).show();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(demo_Phone.this, "Incorrect Verification Code", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

package com.example.quranmentor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quranmentor.Fragments.ProfileImage_fragment;
import com.example.quranmentor.Fragments.profileFragment;
import com.example.quranmentor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OTP_Activity extends AppCompatActivity {
    TextView emailshowing;
    private ProgressBar progressBar;
    private AppCompatButton verify_button;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_acitivty);

        emailshowing = findViewById(R.id.verficatioveri_NUM);
        verify_button = findViewById(R.id.button_verify);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        handler = new Handler();

        String email = getIntent().getStringExtra("email");
        emailshowing.setText(email);

        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailVerification();
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkEmailVerificationStatus();
                handler.postDelayed(this, 3000); // Check email verification status every 3 seconds
            }
        }, 3000);
    }

    private void checkEmailVerificationStatus() {
        user.reload().addOnCompleteListener(task -> {
            if (user.isEmailVerified()) {
                Toast.makeText(OTP_Activity.this, "Email verified successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkEmailVerification() {
        user.reload().addOnCompleteListener(task -> {
            if (user.isEmailVerified()) {
                handler.removeCallbacksAndMessages(null); // Stop the handler
                navigateToNextFragment();
            } else {
                Toast.makeText(OTP_Activity.this, "Please verify your email to proceed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToNextFragment() {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userRole = preferences.getString("spinnerOption", "");

        Fragment fragment;
        if ("As a Student".equalsIgnoreCase(userRole)) {
            fragment = new ProfileImage_fragment(); // Replace with your student fragment
        } else if ("As a Teacher".equalsIgnoreCase(userRole)) {
            fragment = new profileFragment(); // Replace with your teacher fragment
        } else {
            Toast.makeText(OTP_Activity.this, "User role is not defined", Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

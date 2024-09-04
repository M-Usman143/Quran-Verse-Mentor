package com.example.quranmentor.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quranmentor.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassward extends AppCompatActivity {
    private TextInputLayout forgotEmail, forgotPassword, confirmForgotPassword;
    private Button continueBtn, updatePasswordBtn;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_passward_1);

        // Initialize Firebase Database Reference
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Initialize views
        forgotEmail = findViewById(R.id.forgotemail);
        continueBtn = findViewById(R.id.continueBtn);

        // Set OnClickListener for Continue button
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forgotEmail.getEditText().getText().toString().trim();

                // Check if email is empty
                if (email.isEmpty()) {
                    Toast.makeText(ForgotPassward.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else {
                    // Show the second layout
                    setContentView(R.layout.forgot_passward_2); // Switch to the second layout
                    initializeSecondLayout(email); // Pass the email to the next step
                }
            }
        });
    }

    // Initialize views and listeners for the second layout
    private void initializeSecondLayout(String email) {
        forgotPassword = findViewById(R.id.newPassward);
        confirmForgotPassword = findViewById(R.id.confirmPassward);
        updatePasswordBtn = findViewById(R.id.resetPasswordBtn);

        // Set OnClickListener for Update Password button
        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = forgotPassword.getEditText().getText().toString().trim();
                String confirmNewPassword = confirmForgotPassword.getEditText().getText().toString().trim();

                // Check if passwords match
                if (!newPassword.equals(confirmNewPassword)) {
                    Toast.makeText(ForgotPassward.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Find user by email and update the password in the database
                usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                userSnapshot.getRef().child("passward").setValue(newPassword)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(ForgotPassward.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                            // Optionally, you can switch back to the main layout or perform other actions
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(ForgotPassward.this, "Failed to update password: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            }

                            for (DataSnapshot userSnapshots : dataSnapshot.getChildren()) {
                                userSnapshots.getRef().child("conpassward").setValue(newPassword)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(ForgotPassward.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                            // Optionally, you can switch back to the main layout or perform other actions
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(ForgotPassward.this, "Failed to update password: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            }


                        } else {
                            Toast.makeText(ForgotPassward.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ForgotPassward.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}


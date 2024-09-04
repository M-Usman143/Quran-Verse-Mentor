package com.example.quranmentor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quranmentor.R;
import com.example.quranmentor.models.SessionManager;
import com.example.quranmentor.models.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class Sign_up extends AppCompatActivity {
    private TextInputLayout username, email, phoneNo, passward, conpassward;
    private Button signup_btn, alreadyAccBTn;
    private DatabaseReference reference;
    private Spinner spinneroption;
    private ProgressBar progressBar;
    private boolean progressBarShown = false;
    private FirebaseAuth mAuth;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        FunT();
    }
    private void FunT(){

        username = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        phoneNo = findViewById(R.id.phoneNo);
        passward = findViewById(R.id.passward);
        spinneroption = findViewById(R.id.spinner_user);
        conpassward = findViewById(R.id.confirmpassward);
        signup_btn = findViewById(R.id.signup_btn);
        alreadyAccBTn = findViewById(R.id.backTo_loginbtn);
        progressBar = findViewById(R.id.progress_bar);
        sessionManager = new SessionManager(this);

        // After initializing the progressBar
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(android.R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
        setUpSpinner();

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
        alreadyAccBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show progress bar only if it hasn't been shown before
                if (!progressBarShown) {
                    progressBarShown = true;
                    progressBar.setVisibility(View.VISIBLE);
                    toggleViewsVisibility(View.INVISIBLE);
                    // Delay for 5 seconds before moving to the login activity
                    new Handler().postDelayed(() -> {
                        // Hide the progress bar
                        progressBarShown = false;
                        progressBar.setVisibility(View.INVISIBLE);
                        // Move to the login activity
                        Intent intent = new Intent(Sign_up.this, Login.class);
                        startActivity(intent);
                        finish();
                    }, 3000);
                }
            }
        });
    }
    private void toggleViewsVisibility(int visibility) {
        signup_btn.setVisibility(visibility);
        alreadyAccBTn.setVisibility(visibility);}
    private void setUpSpinner() {
        List<String> options = new ArrayList<>();
        options.add("Select the option");
        options.add("As a Student");
        options.add("As a Teacher");
        // Add more options as needed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneroption.setAdapter(adapter);
    }
//    private void signUp() {
//        String name = username.getEditText().getText().toString().trim();
//        String emails = email.getEditText().getText().toString().trim();
//        String password = passward.getEditText().getText().toString().trim();
//        String numbers = "+92"+phoneNo.getEditText().getText().toString().trim();
//        String confirmpassward = conpassward.getEditText().getText().toString().trim();
//        String selectedOption = spinneroption.getSelectedItem().toString();
//
//
//        if (!validateName() || !validateEmail() || !validatephonenumber() || !validateconfirmpassward()
//                || !validatePassword() || selectedOption.equals("Select the option")) {
//            return;}
//               saveSpinnerOption(selectedOption);
//        // Show progress bar
//        progressBar.setVisibility(View.VISIBLE);
//        toggleViewsVisibility(View.INVISIBLE);
//        mAuth.createUserWithEmailAndPassword(emails, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        if(user.isEmailVerified()){
//
//                        }
//                   else{
//                       user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                           @Override
//                           public void onSuccess(Void unused) {
//                               Toast.makeText(Sign_up.this, "Verify Your Email", Toast.LENGTH_SHORT).show();
//                           }
//                       }).addOnFailureListener(new OnFailureListener() {
//                           @Override
//                           public void onFailure(@NonNull Exception e) {
//                               Toast.makeText(Sign_up.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                           }
//                       });
//                        }
//                        if (user != null) {
//                            String uid = user.getUid();
//                            user newUser = new user(name, emails, numbers, password, confirmpassward, selectedOption , uid);
//                            reference.child(uid).setValue(newUser);
//
//                            Toast.makeText(Sign_up.this, "Signup successful. Data stored in Firebase", Toast.LENGTH_SHORT).show();
//                            sessionManager.saveSessionToken("user_session_token"); // Replace "user_session_token" with the actual session token
//                            // User signed up successfully, retrieve device token and store in database
//                            FirebaseMessaging.getInstance().getToken()
//                                    .addOnCompleteListener(new OnCompleteListener<String>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<String> task) {
//                                            if (task.isSuccessful() && task.getResult() != null) {
//                                                String deviceToken = task.getResult();
//                                                // Store the device token in the database under the user's data
//                                                storeDeviceTokenInDatabase(deviceToken, user.getUid());
//                                                sendOTP(numbers);
//                                            } else {
//                                            }
//                                        }
//                                    });
//                            // Delay for 5 seconds before moving to the next activity
//                            new Handler().postDelayed(() -> {
//                                // Hide the progress bar
//                                progressBar.setVisibility(View.INVISIBLE);
//                                }, 3000);
//                        } else {
//                            // If user is null, display an error message
//                            Toast.makeText(Sign_up.this, "User is null.", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        // If sign up fails, hide progress bar and display a message to the user.
//                        progressBar.setVisibility(View.INVISIBLE);
//                        toggleViewsVisibility(View.VISIBLE);
//
//                        Toast.makeText(Sign_up.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
private void signUp() {
    String name = username.getEditText().getText().toString().trim();
    String emails = email.getEditText().getText().toString().trim();
    String password = passward.getEditText().getText().toString().trim();
    String numbers = "+92" + phoneNo.getEditText().getText().toString().trim();
    String confirmpassward = conpassward.getEditText().getText().toString().trim();
    String selectedOption = spinneroption.getSelectedItem().toString();

    if (!validateName() || !validateEmail() || !validatephonenumber() || !validateconfirmpassward()
            || !validatePassword() || selectedOption.equals("Select the option")) {
        return;
    }
    saveSpinnerOption(selectedOption);
    // Show progress bar
    progressBar.setVisibility(View.VISIBLE);
    toggleViewsVisibility(View.INVISIBLE);
    mAuth.createUserWithEmailAndPassword(emails, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String uid = user.getUid();
                        user newUser = new user(name, emails, numbers, password, confirmpassward, selectedOption, uid);
                        reference.child(uid).setValue(newUser);

                        Toast.makeText(Sign_up.this, "Signup successful. Data stored in Firebase", Toast.LENGTH_SHORT).show();
                        sessionManager.saveSessionToken("user_session_token"); // Replace "user_session_token" with the actual session token

                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if (task.isSuccessful() && task.getResult() != null) {
                                            String deviceToken = task.getResult();
                                            storeDeviceTokenInDatabase(deviceToken, user.getUid());
                                        } else {
                                        }
                                    }
                                });

                        if (!user.isEmailVerified()) {
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Sign_up.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                                    sendOTP(emails);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Sign_up.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If email is already verified, move to next activity
                        }
                    } else {
                        Toast.makeText(Sign_up.this, "User is null.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // If sign up fails, hide progress bar and display a message to the user.
                    progressBar.setVisibility(View.INVISIBLE);
                    toggleViewsVisibility(View.VISIBLE);
                    Toast.makeText(Sign_up.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
}


    private void storeDeviceTokenInDatabase(String deviceToken, String userId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.child(userId).child("deviceToken").setValue(deviceToken)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Device token stored successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure to store device token
                    }
                });
    }
    private void saveSpinnerOption(String spinnerOption) {
        // Save spinner option in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("spinnerOption", spinnerOption);
        editor.apply();}
    private boolean validateName() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;}
    }
    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        char[] requiredChars = {'g', 'm', 'a', 'i', 'l', '.', 'c', 'o', 'm'};
        boolean containsAllRequiredChars = true;

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            // Check if all required characters are present in the email
            for (char c : requiredChars) {
                if (val.indexOf(c) == -1) {
                    containsAllRequiredChars = false;
                    break;
                }
            }

            if (!containsAllRequiredChars) {
                email.setError("Invalid Email");
                return false;
            } else {
                email.setError(null);
                return true;
            }
        }
    }
    private Boolean validatephonenumber() {
        String val = phoneNo.getEditText().getText().toString();
        if (val.isEmpty()) {
            phoneNo.setError("Field cannot be empty");
            return false;
        } else if (val.length() < 10) {
            phoneNo.setError("Please Enter valid number");
            return false;
        } else {
            phoneNo.setError(null);
            return true;}
    }
    private boolean validatePassword() {
        String val = passward.getEditText().getText().toString().trim();
        String passwordPattern = "^(?=.*[\\d@#$%^&+=a-zA-Z]).{4,}$";
        if (val.isEmpty()) {
            passward.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordPattern)) {
            passward.setError("Password must contain at least one digit, one symbol, one uppercase letter, and one lowercase letter");
            return false;
        } else {
            passward.setError(null);
            return true;}
    }
    private Boolean validateconfirmpassward() {
        String password = passward.getEditText().getText().toString().trim();
        String val = conpassward.getEditText().getText().toString();
        String passwordVal = "^(?=.*[\\d@#$%^&+=a-zA-Z]).{4,}$";
        if (val.isEmpty()) {
            conpassward.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            conpassward.setError("Password must contain at least one digit, one symbol, one uppercase letter, and one lowercase letter");
            return false;
        } else if (!val.equals(password)) {
            conpassward.setError("Passwords do not match");
            return false;
        } else {
            conpassward.setError(null);
            return true;}
    }
    private void sendOTP(String emails) {
        // Move to email verification activity
        Intent intent = new Intent(Sign_up.this, OTP_Activity.class);
        intent.putExtra("email", emails);
        startActivity(intent);
        finish();
    }
}

//package com.example.quranmentor.activities;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.example.quranmentor.R;
//import com.example.quranmentor.models.SessionManager;
//import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class Login extends AppCompatActivity {
//    private TextInputLayout usernameTextInputLayout, passwordTextInputLayout;
//    private Button signInButton, forgotPasswordButton, newUserAccountButton;
//    private ProgressBar progressBar;
//    private boolean progressBarShown = false;
//    private FirebaseAuth firebaseAuth;
//    private SessionManager sessionManager;
//    private DatabaseReference mDatabase;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login);
//
//        sessionManager = new SessionManager(this);
//
//        usernameTextInputLayout = findViewById(R.id.email);
//        passwordTextInputLayout = findViewById(R.id.passward);
//        signInButton = findViewById(R.id.loginbtn);
//        forgotPasswordButton = findViewById(R.id.forget_btn);
//        newUserAccountButton = findViewById(R.id.newuseraccount_btn);
//        progressBar = findViewById(R.id.progress_bar);
//
//        // Initialize Firebase Authentication
//        firebaseAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Login.this, ForgotPassward.class);
//                startActivity(intent);
//            }
//        });
//
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = usernameTextInputLayout.getEditText().getText().toString().trim();
//                String password = passwordTextInputLayout.getEditText().getText().toString().trim();
//
//                if (isValidCredentials(username, password)) {
//                    toggleViewsVisibility(View.INVISIBLE);
//                    progressBar.setVisibility(View.VISIBLE);
//
//                    signInWithFirebase(username, password);
//                } else {
//                    showToast("Please fill in both fields.");
//                }
//            }
//        });
//
//        newUserAccountButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
//                toggleViewsVisibility(View.INVISIBLE);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setVisibility(View.INVISIBLE);
//                        Intent intent = new Intent(Login.this, Sign_up.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }, 3000); // Delay for 3 seconds
//            }
//        });
//
//        // Check if a session exists
//        String sessionToken = sessionManager.getSessionToken();
//        if (sessionToken != null) {
//            // Session exists, redirect to appropriate dashboard
//            //redirectDashbaord();
//        }
//
//    }
//
//    private void signInWithFirebase(String email, String password) {
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = firebaseAuth.getCurrentUser();
//                        navigateToDashboard();
//                        showToast("Login Successful: " + user.getEmail());
//
//                        // Save session token upon successful login
//                        sessionManager.saveSessionToken("user_session_token");
//                        sessionManager.setLogin(true);
//
//                    } else {
//                        showToast("Login Failed: " + task.getException().getMessage());
//                    }
//
//                    toggleViewsVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.INVISIBLE);
//                });
//    }
//
//    private void navigateToDashboard() {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (currentUser != null) {
//            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
//
//            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        String userRole = dataSnapshot.child("spinneroption").getValue(String.class);
//
//                        if (userRole.equals("As a Student")) {
//                            startActivity(new Intent(Login.this, student_dashboard.class));
//                        } else if (userRole.equals("As a Teacher")) {
//                            startActivity(new Intent(Login.this, teacher_dashboard.class));
//                        } else {
//                            // Handle other roles if needed
//                        }
//
//                        finish(); // Close LoginActivity to prevent going back to it
//                    } else {
//                        navigateToDashboardAdmin();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Handle database error
//                }
//            });
//        }
//    }
//
//    private void navigateToDashboardAdmin() {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (currentUser != null) {
//            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Admin");
//
//            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        String userRole = dataSnapshot.child("role").getValue(String.class);
//
//                        if (userRole.equals("As a Admin")) {
//                            startActivity(new Intent(Login.this, ScheduleClass.class));
//                        } else {
//                            // Handle other roles or unexpected data
//                        }
//
//                        finish(); // Close LoginActivity to prevent going back to it
//                    } else {
//                        // Handle case where user data doesn't exist
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Handle database error
//                }
//            });
//        }
//    }
//
//    private boolean isValidCredentials(String username, String password) {
//        return !username.isEmpty() && !password.isEmpty();
//    }
//
//    private void showToast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//
//    private void toggleViewsVisibility(int visibility) {
//        signInButton.setVisibility(visibility);
//        newUserAccountButton.setVisibility(visibility);
//    }
//}


package com.example.quranmentor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quranmentor.R;
import com.example.quranmentor.models.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private TextInputLayout usernameTextInputLayout, passwordTextInputLayout;
    private Button signInButton, forgotPasswordButton, newUserAccountButton;
    private ProgressBar progressBar;
    private boolean progressBarShown = false;
    private FirebaseAuth firebaseAuth;
    private SessionManager sessionManager;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sessionManager = new SessionManager(this);

        usernameTextInputLayout = findViewById(R.id.email);
        passwordTextInputLayout = findViewById(R.id.passward);
        signInButton = findViewById(R.id.loginbtn);
        forgotPasswordButton = findViewById(R.id.forget_btn);
        newUserAccountButton = findViewById(R.id.newuseraccount_btn);
        progressBar = findViewById(R.id.progress_bar);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassward.class);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTextInputLayout.getEditText().getText().toString().trim();
                String password = passwordTextInputLayout.getEditText().getText().toString().trim();

                if (isValidCredentials(username, password)) {
                    toggleViewsVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    signInWithFirebase(username, password);
                } else {
                    showToast("Please fill in both fields.");
                }
            }
        });

        newUserAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                toggleViewsVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(Login.this, Sign_up.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000); // Delay for 3 seconds
            }
        });

        // Check if a session exists
        String sessionToken = sessionManager.getSessionToken();
        if (sessionToken != null) {
            // Session exists, redirect to appropriate dashboard
          //  redirectDashbaord();
        }
    }

    private void signInWithFirebase(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        navigateToDashboard();
                        showToast("Login Successful: " + user.getEmail());

                        // Save session token upon successful login
                        sessionManager.saveSessionToken("user_session_token");
                        sessionManager.setLogin(true);

                    } else {
                        showToast("Login Failed: " + task.getException().getMessage());
                    }

                    toggleViewsVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }

    private void navigateToDashboard() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userRole = dataSnapshot.child("spinneroption").getValue(String.class);

                        if (userRole.equals("As a Student")) {
                            startActivity(new Intent(Login.this, student_dashboard.class));
                        } else if (userRole.equals("As a Teacher")) {
                            startActivity(new Intent(Login.this, teacher_dashboard.class));
                        } else {
                            // Handle other roles if needed
                        }

                        finish(); // Close LoginActivity to prevent going back to it
                    } else {
                        navigateToDashboardAdmin();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
    }

    private void navigateToDashboardAdmin() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Admin");

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userRole = dataSnapshot.child("role").getValue(String.class);

                        if (userRole.equals("As a Admin")) {
                            startActivity(new Intent(Login.this, ScheduleClass.class));
                        } else {
                            // Handle other roles or unexpected data
                        }

                        finish(); // Close LoginActivity to prevent going back to it
                    } else {
                        // Handle case where user data doesn't exist
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
    }

    private boolean isValidCredentials(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void toggleViewsVisibility(int visibility) {
        signInButton.setVisibility(visibility);
        newUserAccountButton.setVisibility(visibility);
    }
}

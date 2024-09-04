package com.example.quranmentor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quranmentor.R;
import com.example.quranmentor.models.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIMEOUT = 3000; // 3 seconds timeout
    private ImageView imageViewLogo;
    private TextView  textViewSlogan;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
       // sessionManager = new SessionManager(this);

        imageViewLogo = findViewById(R.id.imageViewLogo);
        textViewSlogan = findViewById(R.id.textViewSlogan);

        // Animation for logo (from top to center)
        Animation animLogo = AnimationUtils.loadAnimation(this, R.anim.anim_logo);
        imageViewLogo.startAnimation(animLogo);

        // Animation for slogan (from bottom to top)
        Animation animSlogan = AnimationUtils.loadAnimation(this, R.anim.anim_slogan);
        textViewSlogan.startAnimation(animSlogan);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (sessionManager.isUserLoggedIn()) {
//                    // User is logged in, navigate to the respective dashboard
//                   // navigateToDashboard();
//                } else {
//
//                }

                // User is not logged in, navigate to the login activity
                Intent mainIntent = new Intent(Splash.this, Login.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
    private void navigateToDashboard() {
        // Implement the logic to navigate to the appropriate dashboard based on user role
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userRole = dataSnapshot.child("spinneroption").getValue(String.class);

                    if (userRole.equals("As a Student")) {
                        startActivity(new Intent(Splash.this, student_dashboard.class));
                    } else if (userRole.equals("As a Teacher")) {
                        startActivity(new Intent(Splash.this, teacher_dashboard.class));
                    } else {
                        // Handle other roles if needed
                    }

                    finish(); // Close SplashActivity to prevent going back to it
                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}

}






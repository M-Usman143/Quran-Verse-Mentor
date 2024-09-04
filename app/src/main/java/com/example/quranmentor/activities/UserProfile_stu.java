package com.example.quranmentor.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quranmentor.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile_stu extends AppCompatActivity {
    private CircleImageView profileImage;
    private TextView phone ,role , password ,userEmail ,userName;
    private Button editProfileButton, saveProfileButton;
    private RelativeLayout firstLayout;
    private LinearLayout secondLayout, thirdLayout;
    private TextInputLayout EDfullname, EDphoneNo, EDpassward;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_stu);
       inits();}
    private void inits() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }

        // Initialize Firebase Database Reference
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

        // Initialize views
        firstLayout = findViewById(R.id.firstLayout);
        secondLayout = findViewById(R.id.secondLayout);
        thirdLayout = findViewById(R.id.thirdLayout);
        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phones);
        role = findViewById(R.id.role);
        editProfileButton = findViewById(R.id.editProfileButton);
        saveProfileButton = findViewById(R.id.editsavebutton);
        EDfullname = findViewById(R.id.EDfullname);
        EDphoneNo = findViewById(R.id.EDphoneNo);
        EDpassward = findViewById(R.id.EDpassward);

// Retrieve user info from Firebase
        retrieveUserInfo();

        // Set OnClickListener for Edit Profile button
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondLayout.setVisibility(View.GONE);
                editProfileButton.setVisibility(View.GONE);
                thirdLayout.setVisibility(View.VISIBLE);
                saveProfileButton.setVisibility(View.VISIBLE);
                populateEditableFields();
            }
        });

        // Set OnClickListener for Save Profile button
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

    }
    private void populateEditableFields() {
        EDfullname.getEditText().setText(userName.getText().toString());
        EDphoneNo.getEditText().setText(phone.getText().toString());
        EDpassward.getEditText().setText(password.getText().toString());

    }
    private void retrieveUserInfo() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("fullname").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String passwordText = dataSnapshot.child("passward").getValue(String.class);
                    String phoneText = dataSnapshot.child("number").getValue(String.class);
                    String roleText = dataSnapshot.child("spinneroption").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);

                    userName.setText(name);
                    userEmail.setText(email);
                    password.setText(passwordText);
                    phone.setText(phoneText);
                    role.setText(roleText);

                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Picasso.get().load(profileImageUrl).placeholder(R.drawable.men).into(profileImage);
                    } else {
                        profileImage.setImageResource(R.drawable.men);
                    }
                } else {
                    Toast.makeText(UserProfile_stu.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfile_stu.this, "Error retrieving user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveUserProfile() {
        String updatedFullName = EDfullname.getEditText().getText().toString();
        String updatedPhone = EDphoneNo.getEditText().getText().toString();
        String updatedPassword = EDpassward.getEditText().getText().toString();

        // Update in Firebase database
        usersRef.child("fullname").setValue(updatedFullName);
        usersRef.child("number").setValue(updatedPhone);
        usersRef.child("passward").setValue(updatedPassword);


        // Update UI after saving changes
        userName.setText(updatedFullName);
        phone.setText(updatedPhone);
        password.setText(updatedPassword);

        // Switch visibility of layouts and buttons
        firstLayout.setVisibility(View.VISIBLE);
        secondLayout.setVisibility(View.VISIBLE);
        thirdLayout.setVisibility(View.GONE);
        editProfileButton.setVisibility(View.VISIBLE);
        saveProfileButton.setVisibility(View.GONE);

        Toast.makeText(UserProfile_stu.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }
}
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

public class UserProfile_Teacher extends AppCompatActivity {
    private CircleImageView profileImage;
    private TextView userName, userEmail, TVpassword, TVphones, TVrole, TVcity, TVcountry, TVexperience, gender, TVqualification, TVrate, TVspecilization;
    private Button editProfileButton, saveProfileButton;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private String currentUserId;
    private RelativeLayout firstLayout;
    private LinearLayout secondLayout, thirdLayout;
    private TextInputLayout EDfullname, EDphoneNo, EDpassward, EDrates, EDexperience, EDgender, EDspecilization, EDQualification, EDcity, EDcountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_teacher);
        init();
    }
    private void init(){

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
            return;
        }

        // Initialize Firebase Database Reference

        // Initialize views
        firstLayout = findViewById(R.id.firstLayout_teacher);
        secondLayout = findViewById(R.id.secondLayout_teacher);
        thirdLayout = findViewById(R.id.thirdLayout_teacher);
        profileImage = findViewById(R.id.profileImage_teacher);
        userName = findViewById(R.id.userName_teacher);
        userEmail = findViewById(R.id.userEmail_teacher);
        TVpassword = findViewById(R.id.TVpassword_teacher);
        TVphones = findViewById(R.id.TVphones_teacher);
        TVrole = findViewById(R.id.TVrole_teacher);
        TVcity = findViewById(R.id.TVcity_teacher);
        TVcountry = findViewById(R.id.TVcountry_teacher);
        TVexperience = findViewById(R.id.TVexperience_teacher);
        gender = findViewById(R.id.gender_teacher);
        TVqualification = findViewById(R.id.TVqualification_teacher);
        TVrate = findViewById(R.id.TVrate_teacher);
        TVspecilization = findViewById(R.id.TVspecilization_teacher);
        editProfileButton = findViewById(R.id.editProfileButton_teacher);
        saveProfileButton = findViewById(R.id.editsavebutton_teacher);
        EDfullname = findViewById(R.id.EDfullname_teacher);
        EDphoneNo = findViewById(R.id.EDphoneNo_teacher);
        EDpassward = findViewById(R.id.EDpassward_teacher);
        EDrates = findViewById(R.id.EDrates_teacher);
        EDexperience = findViewById(R.id.EDexperience_teacher);
        EDgender = findViewById(R.id.EDgender_teacher);
        EDspecilization = findViewById(R.id.EDspecilization_teacher);
        EDQualification = findViewById(R.id.EDQualification_teacher);
        EDcity = findViewById(R.id.EDcity_teacher);
        EDcountry = findViewById(R.id.EDcountry_teacher);

        // Retrieve user info from Firebase
        retrieveUserInfo();
        // Retrieve teacher info from Firebase
        retrieveTeacherInfo();

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
        EDphoneNo.getEditText().setText(TVphones.getText().toString());
        EDpassward.getEditText().setText(TVpassword.getText().toString());
        EDrates.getEditText().setText(TVrate.getText().toString());
        EDgender.getEditText().setText(gender.getText().toString());
        EDspecilization.getEditText().setText(TVspecilization.getText().toString());
        EDexperience.getEditText().setText(TVexperience.getText().toString());
        EDQualification.getEditText().setText(TVqualification.getText().toString());
        EDcity.getEditText().setText(TVcity.getText().toString());
        EDcountry.getEditText().setText(TVcountry.getText().toString());
    }
    private void retrieveUserInfo() {
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
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
                    TVpassword.setText(passwordText);
                    TVphones.setText(phoneText);
                    TVrole.setText(roleText);

                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Picasso.get().load(profileImageUrl).placeholder(R.drawable.men).into(profileImage);
                    } else {
                        profileImage.setImageResource(R.drawable.men);
                    }
                } else {
                    Toast.makeText(UserProfile_Teacher.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfile_Teacher.this, "Error retrieving user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void retrieveTeacherInfo() {
        DatabaseReference teachersRef = FirebaseDatabase.getInstance().getReference().child("teachers_info").child(currentUserId);
        teachersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String cityText = dataSnapshot.child("city").getValue(String.class);
                    String countryText = dataSnapshot.child("country").getValue(String.class);
                    String experienceText = dataSnapshot.child("experience").getValue(String.class);
                    String genderText = dataSnapshot.child("gender").getValue(String.class);
                    String qualificationText = dataSnapshot.child("qualification").getValue(String.class);
                    String rateText = dataSnapshot.child("rates").getValue(String.class);
                    String specializationText = dataSnapshot.child("specilization").getValue(String.class);

                    TVcity.setText(cityText);
                    TVcountry.setText(countryText);
                    TVexperience.setText(experienceText);
                    gender.setText(genderText);
                    TVqualification.setText(qualificationText);
                    TVrate.setText(rateText);
                    TVspecilization.setText(specializationText);
//
//                    EDcity.getEditText().setText(cityText);
//                    EDcountry.getEditText().setText(countryText);
//                    EDexperience.getEditText().setText(experienceText);
//                    EDgender.getEditText().setText(genderText);
//                    EDQualification.getEditText().setText(qualificationText);
//                    EDrates.getEditText().setText(rateText);
//                    EDspecilization.getEditText().setText(specializationText);
                } else {
                    Toast.makeText(UserProfile_Teacher.this, "Teacher info not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfile_Teacher.this, "Error retrieving teacher info: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveUserProfile() {
        String updatedFullName = EDfullname.getEditText().getText().toString();
        String updatedPhone = EDphoneNo.getEditText().getText().toString();
        String updatedPassword = EDpassward.getEditText().getText().toString();
        String updatedRate = EDrates.getEditText().getText().toString();
        String updatedGender = EDgender.getEditText().getText().toString();
        String updatedSpecilization = EDspecilization.getEditText().getText().toString();
        String updatedExperience = EDexperience.getEditText().getText().toString();
        String updatedQualification = EDQualification.getEditText().getText().toString();
        String updatedCity = EDcity.getEditText().getText().toString();
        String updatedCountry = EDcountry.getEditText().getText().toString();

        // Update in Firebase database
        usersRef.child("fullname").setValue(updatedFullName);
        usersRef.child("number").setValue(updatedPhone);
        usersRef.child("passward").setValue(updatedPassword);

        DatabaseReference teachersRef = FirebaseDatabase.getInstance().getReference().child("teachers_info").child(currentUserId);
        teachersRef.child("city").setValue(updatedCity);
        teachersRef.child("country").setValue(updatedCountry);
        teachersRef.child("experience").setValue(updatedExperience);
        teachersRef.child("gender").setValue(updatedGender);
        teachersRef.child("qualification").setValue(updatedQualification);
        teachersRef.child("rates").setValue(updatedRate);
        teachersRef.child("specilization").setValue(updatedSpecilization);

        // Update UI after saving changes
        userName.setText(updatedFullName);
        TVphones.setText(updatedPhone);
        TVpassword.setText(updatedPassword);
        TVrate.setText(updatedRate);
        gender.setText(updatedGender);
        TVspecilization.setText(updatedSpecilization);
        TVexperience.setText(updatedExperience);
        TVqualification.setText(updatedQualification);
        TVcity.setText(updatedCity);
        TVcountry.setText(updatedCountry);

        // Switch visibility of layouts and buttons
        firstLayout.setVisibility(View.VISIBLE);
        secondLayout.setVisibility(View.VISIBLE);
        thirdLayout.setVisibility(View.GONE);
        editProfileButton.setVisibility(View.VISIBLE);
        saveProfileButton.setVisibility(View.GONE);

        Toast.makeText(UserProfile_Teacher.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }
}

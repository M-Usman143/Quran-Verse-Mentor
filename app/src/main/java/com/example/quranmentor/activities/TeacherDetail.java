package com.example.quranmentor.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quranmentor.Adapters.feedbackAdapter;
import com.example.quranmentor.R;
import com.example.quranmentor.models.Give_Feedback;
import com.example.quranmentor.models.TeacherProfile;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherDetail extends AppCompatActivity {
    ImageView teacher_profilepic;
    TextView teacher_name, header_experience_textView, count_review , teacher_qualification_text, teacher_fees_text, teacher_specilization_text, teacher_description_text;
    AppCompatButton messageButton;
    private FlexboxLayout flexboxLayout;
    private List<TeacherProfile> teacherProfileList = new ArrayList<>();
    private RecyclerView recyclerViewFeedback;
    private feedbackAdapter feedbackAdapter;
    private List<Give_Feedback> feedbackList;
    private String teacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.teacher_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        messageButton = findViewById(R.id.chatbutton);
        count_review = findViewById(R.id.count);


      funT();
    }
    private void funT(){

        // Retrieve teacher ID from intent extras
        teacherId = getIntent().getStringExtra("teacherID");
        String username = getIntent().getStringExtra("fullname");
        String profileImage = getIntent().getStringExtra("profileImageUrl");
        if (teacherId != null && username != null && profileImage != null) {
            retrieveTeacherInfoFromFirebase(teacherId, username, profileImage);
        } else {
            Log.e("TeacherDetail", "Teacher ID, username, or profile image is null");
        }
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTeacherDetail(teacherId,username,profileImage);
            }
        });
        recyclerSetup();
    }

    private void recyclerSetup() {
        recyclerViewFeedback= findViewById(R.id.recycler_view_feedback);
        feedbackList = new ArrayList<>();
        feedbackAdapter = new feedbackAdapter(this, feedbackList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewFeedback.setLayoutManager(layoutManager);
        recyclerViewFeedback.setAdapter(feedbackAdapter);
        feedbackRetrieve(teacherId, feedbackAdapter);

    }
    public void feedbackRetrieve(String teacherId, feedbackAdapter adapter) {
        DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference().child("feedback").child(teacherId);
        feedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot feedbackSnapshot : dataSnapshot.getChildren()) {
                    int totalReviews = (int) dataSnapshot.getChildrenCount(); // Calculate total number of reviews

                    count_review.setText("("+totalReviews+")"); // Upda
                    addFeedbackFromSnapshot(feedbackSnapshot, adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to retrieve feedback: " + databaseError.getMessage());
            }
        });
    }
    private void addFeedbackFromSnapshot(DataSnapshot feedbackSnapshot, feedbackAdapter adapter) {
        String feedbackId = feedbackSnapshot.child("feedbackId").getValue(String.class);
        String studentName = feedbackSnapshot.child("studentName").getValue(String.class);
        float rating = feedbackSnapshot.child("rating").getValue(Float.class);
        String comment = feedbackSnapshot.child("comment").getValue(String.class);
        long timestamp = feedbackSnapshot.child("timestamp").getValue(Long.class);

        Give_Feedback feedback = new Give_Feedback();
        feedback.setFeedbackID(feedbackId);
        feedback.setStudentName(studentName);
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setTimestamp(timestamp);
        adapter.addFeedback(feedback);
    }
    private void navigateToTeacherDetail(String teacherId, String teacherUsername, String teacherProfileImage) {
        Intent intent = new Intent(TeacherDetail.this, chatsActivity.class);
        intent.putExtra("teacherID", teacherId);
        intent.putExtra("teacherUsername", teacherUsername);
        intent.putExtra("teacherProfileImage", teacherProfileImage);
        startActivity(intent);
    }

    public void retrieveTeacherInfoFromFirebase(String teacherId, String username, String profileImage) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("teachers_info").child(teacherId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String rate = dataSnapshot.child("rates").getValue(String.class);
                    String experience = dataSnapshot.child("experience").getValue(String.class);
                    String qualification = dataSnapshot.child("qualification").getValue(String.class);
                    String specilization = dataSnapshot.child("specilization").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);

                    if (rate != null && experience != null && qualification != null && specilization != null && description != null) {
                        TeacherProfile teacherProfile = new TeacherProfile();
                        teacherProfile.setTeacherID(teacherId);
                        teacherProfile.setName(username);
                        teacherProfile.setPrice(rate);
                        teacherProfile.setDescription(description);
                        teacherProfile.setExperience(experience);
                        teacherProfile.setQualification(qualification);
                        teacherProfile.setSpecilization(specilization);
                        teacherProfile.setProfileImageUri(profileImage);

                        updateProfileUI(teacherProfile);
                    } else {
                        Log.e("Firebase", "Some information is missing for user: " + teacherId);

                    }
                } else {
                    Log.e("Firebase", "No teacher profile found for ID: " + teacherId);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to retrieve teacher info: " + databaseError.getMessage());

            }
        });
    }
    private void updateProfileUI(TeacherProfile teacherProfile) {

        teacher_profilepic = findViewById(R.id.tutor_image);
        teacher_name = findViewById(R.id.title_name);
        header_experience_textView = findViewById(R.id.header_experience);
        teacher_qualification_text = findViewById(R.id.tell_me_qualification);
        teacher_fees_text = findViewById(R.id.tell_me_fees);
        teacher_description_text = findViewById(R.id.tell_mebriefing);
        messageButton = findViewById(R.id.chatbutton);
        flexboxLayout = findViewById(R.id.flexboxLayout);


        // Set the name, gender, and rates
        teacher_name.setText(teacherProfile.getName());
        String expertisees = teacherProfile.getExperience()+ " Years Experience";
        header_experience_textView.setText(expertisees);
        String ratess = teacherProfile.getPrice()+ " PKR/Month";
        teacher_fees_text.setText(ratess);
        teacher_qualification_text.setText(teacherProfile.getQualification());
        teacher_description_text.setText(teacherProfile.getDescription());
        Glide.with(this)
                .load(teacherProfile.getProfileImageUri())
                .into(teacher_profilepic);


        String specilization = teacherProfile.getSpecilization();
        if (specilization != null && !specilization.isEmpty()) {
            String[] specilizations = specilization.split(",");
            for (String tag : specilizations) {
                TextView textView = new TextView(this);
                textView.setText(tag.trim());
                textView.setBackgroundResource(R.drawable.tag_backgorund); // Set background drawable for tag
                textView.setPadding(16, 8, 16, 8); // Add padding to the tag
                textView.setTextColor(Color.BLACK); // Set text color for tag
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Set text size for tag
                FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(8, 8, 8, 8); // Add margins between tags
                textView.setLayoutParams(layoutParams);
                flexboxLayout.addView(textView);}
        }
// Hide shimmer and show main content

        }


}
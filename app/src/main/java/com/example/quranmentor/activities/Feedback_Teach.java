package com.example.quranmentor.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranmentor.Adapters.feedbackAdapter;
import com.example.quranmentor.R;
import com.example.quranmentor.models.Give_Feedback;
import com.example.quranmentor.models.TeacherProfile;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Feedback_Teach extends AppCompatActivity {
    TextView count_review;
    private RecyclerView recyclerViewFeedback;
    private feedbackAdapter feedbackAdapter;
    private List<Give_Feedback> feedbackList;
    private String currentTeacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_teach);

        // Get the current user's ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentTeacherId = currentUser.getUid();
        } else {
            // Handle the case where the user is not logged in
            Log.e("Firebase", "User not logged in");
            return;
        }

        recyclerSetup();
    }

    private void recyclerSetup() {
        recyclerViewFeedback = findViewById(R.id.recycler_view_feedback);
        feedbackList = new ArrayList<>();
        feedbackAdapter = new feedbackAdapter(this, feedbackList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewFeedback.setLayoutManager(layoutManager);
        recyclerViewFeedback.setAdapter(feedbackAdapter);
        feedbackRetrieve(currentTeacherId, feedbackAdapter);
    }

    public void feedbackRetrieve(String teacherId, feedbackAdapter adapter) {
        DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference().child("feedback").child(teacherId);
        feedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int totalReviews = (int) dataSnapshot.getChildrenCount(); // Calculate total number of reviews
//                count_review.setText("(" + totalReviews + ")"); // Update review count

                for (DataSnapshot feedbackSnapshot : dataSnapshot.getChildren()) {
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
}

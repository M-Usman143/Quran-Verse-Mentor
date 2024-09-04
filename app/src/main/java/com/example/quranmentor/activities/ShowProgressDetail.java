package com.example.quranmentor.activities;
import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quranmentor.R;
import com.example.quranmentor.models.ProgressReport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ShowProgressDetail extends AppCompatActivity {
    // Declare variables for the UI elements
    private TextView studentNameTextView, pronouncationpercentage, fluencypercentage, memorizationpercentage, tajweedpercentage, overallpercentage , essentialduas;
    private ProgressBar pronunciationProgressBar, fluencyProgressBar, memorizationProgressBar, tajweedProgressBar, overallProgressBar , essentailduasProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_progress_detail);
        initialIDs();
    }
    private void initialIDs(){
        // Initialize the UI elements
        studentNameTextView = findViewById(R.id.student_Name);
        pronunciationProgressBar = findViewById(R.id.progressBarPronunciation);
        fluencyProgressBar = findViewById(R.id.progressBarFluency);
        memorizationProgressBar = findViewById(R.id.progressBarMemorization);
        tajweedProgressBar = findViewById(R.id.progressBarTajweed);
        essentailduasProgressBar = findViewById(R.id.progressBaressentailduass);
        overallProgressBar = findViewById(R.id.progressBaroverall);

        pronouncationpercentage = findViewById(R.id.pronouncation_percentage);
        fluencypercentage = findViewById(R.id.fluency_percentage);
        memorizationpercentage = findViewById(R.id.memorization_percentage);
        tajweedpercentage = findViewById(R.id.tajweed_percentage);
        overallpercentage = findViewById(R.id.overallresult);
        essentialduas = findViewById(R.id.essentialduas);

        String studentName = getIntent().getStringExtra("studentName");
        studentNameTextView.setText(studentName);
        retrieveAndDisplayProgressReports();
    }
    private void retrieveAndDisplayProgressReports() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            DatabaseReference progressReportRef = FirebaseDatabase.getInstance().getReference()
                    .child("progress_reports").child(currentUserId);

            // Query to retrieve progress reports for the current user
            Query query = progressReportRef.orderByChild("studentID").equalTo(currentUserId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ProgressReport progressReport = snapshot.getValue(ProgressReport.class);
                            if (progressReport != null) {
                                updateProgressUI(progressReport);
                            }
                        }
                    } else {
                        Log.d(TAG, "No progress reports found for current user");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Error fetching progress reports: " + databaseError.getMessage());
                }
            });
        }
    }
    private void updateProgressUI(ProgressReport progressReport) {
        int pronunciationProgress = progressReport.getPronunciation();
        int fluencyProgress = progressReport.getFluency();
        int memorizationProgress = progressReport.getMemorization();
        int tajweedProgress = progressReport.getTajweed();
       int essentialProgress = progressReport.getEssentialDuas();
        int overallProgress = (pronunciationProgress + fluencyProgress + memorizationProgress + tajweedProgress + essentialProgress) / 5;

        pronunciationProgressBar.setProgress(pronunciationProgress);
        fluencyProgressBar.setProgress(fluencyProgress);
        memorizationProgressBar.setProgress(memorizationProgress);
        tajweedProgressBar.setProgress(tajweedProgress);
        essentailduasProgressBar.setProgress(essentialProgress);
        overallProgressBar.setProgress(overallProgress);

        pronouncationpercentage.setText(pronunciationProgress + "%complete\n" + getProgressMessage(pronunciationProgress));
        fluencypercentage.setText(fluencyProgress + "%complete\n" + getProgressMessage(fluencyProgress));
        memorizationpercentage.setText(memorizationProgress + "%complete\n" + getProgressMessage(memorizationProgress));
        tajweedpercentage.setText(tajweedProgress + "%complete\n" + getProgressMessage(tajweedProgress));
        essentialduas.setText(essentialProgress + "%complete\n" + getProgressMessage(essentialProgress));
        overallpercentage.setText(overallProgress + "%Complete");
    }
    private String getProgressMessage(int percentage) {
        if (percentage >= 90) {
            return "You are doing well";
        } else if (percentage < 90 && percentage >=70) {
            return "Keep it up";
        } else if (percentage < 70&&percentage >= 50) {
            return "You are making progress";
        } else {
            return "You need to do more work on it";
        }
    }
}


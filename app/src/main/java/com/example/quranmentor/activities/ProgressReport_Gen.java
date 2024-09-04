
package com.example.quranmentor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quranmentor.R;
import com.example.quranmentor.models.ProgressReport;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProgressReport_Gen extends AppCompatActivity {

    // Declare UI elements
    private SeekBar seekBarPronunciation, seekBarFluency, seekBarMemorization, seekBarTajweed, seekBarEssentialDuas;
    private TextView studentuserame;
    private TextView tvPronunciationPercentage, tvFluencyPercentage, tvMemorizationPercentage, tvTajweedPercentage, tvEssentialDuasPercentage;
    private FloatingActionButton btnSubmitReport;

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private String studentID;
    private String studentName;
    private String teacherName;
    private String teacherID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_report_gen);
        initialIDs();
    }

    private void initialIDs() {
        // Initialize variables
        studentuserame = findViewById(R.id.tvStudentInfo);
        tvPronunciationPercentage = findViewById(R.id.tvPronunciationPercentage);
        tvFluencyPercentage = findViewById(R.id.tvFluencyPercentage);
        tvMemorizationPercentage = findViewById(R.id.tvMemorizationPercentage);
        tvTajweedPercentage = findViewById(R.id.tvTajweedPercentage);
        tvEssentialDuasPercentage = findViewById(R.id.tvEssentialDuasPercentage);

        seekBarPronunciation = findViewById(R.id.seekBarPronunciation);
        seekBarFluency = findViewById(R.id.seekBarFluency);
        seekBarMemorization = findViewById(R.id.seekBarMemorization);
        seekBarTajweed = findViewById(R.id.seekBarTajweed);
        seekBarEssentialDuas = findViewById(R.id.seekBarEssentialDuas);

        btnSubmitReport = findViewById(R.id.btnSubmit);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("progress_reports");

        // Get student ID or Name from Intent
        Intent intent = getIntent();
        if (intent != null) {
            studentID = intent.getStringExtra("studentID");
            studentName = intent.getStringExtra("studentName");
            String name = "Student Name: " + studentName;
            studentuserame.setText(name);
        }

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            teacherID = currentUser.getUid();
            fetchTeacherName();
        }

        // Setup listeners
        setupSeekBarListeners();
        setupSubmitButton();
    }

    private void fetchTeacherName() {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(teacherID);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    teacherName = snapshot.child("fullname").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    private void setupSeekBarListeners() {
        seekBarPronunciation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvPronunciationPercentage.setText("Progress: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something when touch starts
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do something when touch stops
            }
        });

        seekBarFluency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvFluencyPercentage.setText("Progress: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something when touch starts
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do something when touch stops
            }
        });

        seekBarMemorization.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvMemorizationPercentage.setText("Progress: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something when touch starts
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do something when touch stops
            }
        });

        seekBarTajweed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTajweedPercentage.setText("Progress: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something when touch starts
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do something when touch stops
            }
        });

        seekBarEssentialDuas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvEssentialDuasPercentage.setText("Progress: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something when touch starts
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do something when touch stops
            }
        });
    }

    private void setupSubmitButton() {
        btnSubmitReport.setOnClickListener(v -> {
            // Collect data from SeekBars
            int pronunciation = seekBarPronunciation.getProgress();
            int fluency = seekBarFluency.getProgress();
            int memorization = seekBarMemorization.getProgress();
            int tajweed = seekBarTajweed.getProgress();
            int essentialDuas = seekBarEssentialDuas.getProgress();

            // Ensure student ID is not null
            if (studentID != null && !studentID.isEmpty()) {
                // Create a unique ID for each report
                String reportId = databaseReference.child(studentID).push().getKey();

                // Get the current date and time
                String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                // Create a report object
                ProgressReport report = new ProgressReport(pronunciation, fluency, memorization, tajweed, essentialDuas, studentName,
                        teacherName, studentID, teacherID, currentDate , reportId);

                // Save the report to Firebase Realtime Database
                if (reportId != null) {
                    databaseReference.child(studentID).child(reportId).setValue(report).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Display success message
                            Toast.makeText(ProgressReport_Gen.this, "Report submitted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Display failure message
                            Toast.makeText(ProgressReport_Gen.this, "Failed to submit report", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(ProgressReport_Gen.this, "Student ID is null or empty", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

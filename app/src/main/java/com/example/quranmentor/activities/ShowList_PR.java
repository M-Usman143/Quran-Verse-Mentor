package com.example.quranmentor.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quranmentor.Adapters.StudentAdapterPR;
import com.example.quranmentor.R;
import com.example.quranmentor.models.scheduleClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowList_PR extends AppCompatActivity {
    private RecyclerView recyclerViewStudents;
    private StudentAdapterPR studentAdapter;
    private List<scheduleClass> studentList;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_pr);

        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
        studentList = new ArrayList<>();

        studentAdapter = new StudentAdapterPR(studentList, this);
        recyclerViewStudents.setAdapter(studentAdapter);
        fetchStudentsScheduledWithTeacher();

    }
    private void fetchStudentsScheduledWithTeacher() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String teacherId = currentUser.getUid(); // Get current user's ID (teacher ID)
            // Reference to the schedules node in Firebase
            DatabaseReference schedulesRef = FirebaseDatabase.getInstance().getReference().child("schedules").child(teacherId);
            // Query to fetch schedules where the teacher ID matches the current user's ID
            schedulesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    studentList.clear(); // Clear previous data

                    for (DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()) {
                        // Retrieve student ID and name from schedule
                        String studentID = scheduleSnapshot.child("studentID").getValue(String.class);
                        String studentName = scheduleSnapshot.child("studentName").getValue(String.class);

                        if (studentID != null && studentName != null) {
                            // Create a scheduleClass object and set its properties
                            scheduleClass schedule = new scheduleClass();
                            schedule.setStudentID(studentID);
                            schedule.setStudentName(studentName);

                            // Add schedule to list
                            studentList.add(schedule);
                        }
                    }

                    // Notify adapter of data changes
                    studentAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Toast.makeText(ShowList_PR.this, "Failed to retrieve schedules: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}


package com.example.quranmentor.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranmentor.Adapters.AdapterupcomingClass;
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

public class ScheduleClass extends AppCompatActivity {

    FirebaseUser currentUser;
    private List<scheduleClass> upcomingClassesList = new ArrayList<>();

    private RecyclerView scheduleClass_recView;
    private AdapterupcomingClass mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_class);
        recyclerSetup();

    }
    //-------------------------------------Scheduler Adapter Setups---------------------------------------------
    private void recyclerSetup() {
        scheduleClass_recView = findViewById(R.id.recycler_view_teachers);
        scheduleClass_recView.setLayoutManager(new LinearLayoutManager(this));
        AdapterupcomingClass adapter = new AdapterupcomingClass(upcomingClassesList , ScheduleClass.this);
        scheduleClass_recView.setAdapter(adapter);
        scheduleretrieve(adapter);
    }
    public void scheduleretrieve(AdapterupcomingClass adapter) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid(); // Get the current user's ID

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userRole = dataSnapshot.child("spinneroption").getValue(String.class); // Get the current user's role

                        DatabaseReference schedulesRef = FirebaseDatabase.getInstance().getReference().child("schedules");
                        schedulesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                                    String teacherId = teacherSnapshot.getKey(); // Get teacher ID

                                    if (userRole.equals("As a Teacher") && teacherId.equals(userId)) {
                                        // If the user is a teacher and the teacher ID matches the current user's ID
                                        for (DataSnapshot scheduleSnapshot : teacherSnapshot.getChildren()) {
                                            // Add only the schedules of the current teacher
                                            addScheduleFromSnapshot(scheduleSnapshot, adapter);
                                        }
                                    } else if (userRole.equals("As a Student")) {
                                        // If the user is a student
                                        for (DataSnapshot scheduleSnapshot : teacherSnapshot.getChildren()) {
                                            String studentId = scheduleSnapshot.child("studentID").getValue(String.class); // Get student ID
                                            if (studentId != null && studentId.equals(userId)) {
                                                addScheduleFromSnapshot(scheduleSnapshot, adapter);
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("Firebase", "Failed to retrieve schedules: " + databaseError.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Failed to retrieve user data: " + databaseError.getMessage());
                }
            });
        }
    }
    private void addScheduleFromSnapshot(DataSnapshot scheduleSnapshot, AdapterupcomingClass adapter) {
        String scheduleId = scheduleSnapshot.getKey(); // Get schedule ID
        String date = scheduleSnapshot.child("date").getValue(String.class);
        String time = scheduleSnapshot.child("time").getValue(String.class);
        String title = scheduleSnapshot.child("title").getValue(String.class);
        String studentName = scheduleSnapshot.child("studentName").getValue(String.class);
        String tutorName = scheduleSnapshot.child("tutorName").getValue(String.class);
        String studentId = scheduleSnapshot.child("studentID").getValue(String.class); // Get student ID
        String teacherId = scheduleSnapshot.child("teacherID").getValue(String.class); // Get teacher ID

        scheduleClass schedule = new scheduleClass();
        schedule.setScheduleID(scheduleId);
        schedule.setTeacherID(teacherId);
        schedule.setStudentID(studentId);
        schedule.setStudentName(studentName);
        schedule.setTutorName(tutorName);
        schedule.setTime(time);
        schedule.setDate(date);
        schedule.setTitle(title);
        adapter.addSchedule(schedule);
    }

//    public void scheduleretrieve() {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//
//            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
//            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        String userRole = dataSnapshot.child("spinneroption").getValue(String.class);
//                        Log.d("Scheduleretrieve", "User role: " + userRole);
//
//                        DatabaseReference schedulesRef = FirebaseDatabase.getInstance().getReference().child("schedules");
//                        schedulesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                Log.d("Scheduleretrieve", "Schedules data received");
//                                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
//                                    String teacherId = teacherSnapshot.getKey();
//                                    Log.d("Scheduleretrieve", "Processing teacher ID: " + teacherId);
//
//                                    if (userRole.equals("As a Teacher") && teacherId.equals(userId)) {
//                                        for (DataSnapshot scheduleSnapshot : teacherSnapshot.getChildren()) {
//                                            Log.d("Scheduleretrieve", "Adding schedule for teacher ID: " + teacherId);
//                                            addScheduleFromSnapshot(scheduleSnapshot, mAdapter);
//                                        }
//                                    } else if (userRole.equals("As a Student")) {
//                                        for (DataSnapshot scheduleSnapshot : teacherSnapshot.getChildren()) {
//                                            String studentId = scheduleSnapshot.child("studentID").getValue(String.class);
//                                            Log.d("Scheduleretrieve", "Found student ID: " + studentId);
//                                            if (studentId != null && studentId.equals(userId)) {
//                                                Log.d("Scheduleretrieve", "Adding schedule for student ID: " + studentId);
//                                                addScheduleFromSnapshot(scheduleSnapshot, mAdapter);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                Log.e("Firebase", "Failed to retrieve schedules: " + databaseError.getMessage());
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Log.e("Firebase", "Failed to retrieve user data: " + databaseError.getMessage());
//                }
//            });
//        }
//    }
//
//    private void addScheduleFromSnapshot(DataSnapshot scheduleSnapshot, AdapterupcomingClass adapter) {
//        String scheduleId = scheduleSnapshot.getKey();
//        String date = scheduleSnapshot.child("date").getValue(String.class);
//        String time = scheduleSnapshot.child("time").getValue(String.class);
//        String title = scheduleSnapshot.child("title").getValue(String.class);
//        String studentName = scheduleSnapshot.child("studentName").getValue(String.class);
//        String tutorName = scheduleSnapshot.child("tutorName").getValue(String.class);
//        String studentId = scheduleSnapshot.child("studentID").getValue(String.class);
//        String teacherId = scheduleSnapshot.child("teacherID").getValue(String.class);
//
//        if (studentId == null) {
//            Log.e("AddSchedule", "Student ID is null for schedule: " + scheduleId);
//        } else {
//            Log.d("AddSchedule", "Retrieved Student ID: " + studentId + " for schedule: " + scheduleId);
//        }
//
//        scheduleClass schedule = new scheduleClass();
//        schedule.setScheduleID(scheduleId);
//        schedule.setTeacherID(teacherId);
//        schedule.setStudentID(studentId);
//        schedule.setStudentName(studentName);
//        schedule.setTutorName(tutorName);
//        schedule.setTime(time);
//        schedule.setDate(date);
//        schedule.setTitle(title);
//        adapter.addSchedule(schedule);
//    }

}
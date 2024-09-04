package com.example.quranmentor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quranmentor.Adapters.AdapterupcomingClass;
import com.example.quranmentor.Fragments.BottomSheetFragment;
import com.example.quranmentor.R;
import com.example.quranmentor.models.TeacherProfile;
import com.example.quranmentor.models.scheduleClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class teacher_dashboard extends AppCompatActivity {
    private RecyclerView scheduleClass_recView;
    private AdapterupcomingClass mAdapter;
    private DatabaseReference feedbackRequestRef;
    CircleImageView profilepic;
    ImageView movetoschedulACT;
    private SearchView searchView;
    FirebaseUser currentUser;
    private List<scheduleClass> upcomingClassesList = new ArrayList<>();
    AdapterupcomingClass adapter;
    ImageButton Tajweedbutton , Quranbutton , Qiblabutton , Prayerbutton;
    TextView usernames;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_dashboard);
        initialize();
        movetoschedulACT = findViewById(R.id.movetoscheduleAct);

        searchView = findViewById(R.id.searchView);
        // Expand the SearchView programmatically
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterLists(newText);
                return false;
            }
        });



        movetoschedulACT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teacher_dashboard.this , ScheduleClass.class);
                startActivity(intent);
            }
        });
    }

    private void filterLists(String newText) {
        List<scheduleClass> filterlist = new ArrayList<>();
        if (!newText.isEmpty()) {
            for (scheduleClass scheduleClass : upcomingClassesList) {
                if (scheduleClass.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                        scheduleClass.getStudentName().toLowerCase().contains(newText.toLowerCase()) ||
                        scheduleClass.getTutorName().toLowerCase().contains(newText.toLowerCase())) {
                    filterlist.add(scheduleClass);
                }
            }

         {
                adapter.setFiltertedList(filterlist);
                scrollToFirstMatchedItem(filterlist);
            }
        } else {
            // Reset to original list if search query is empty
            adapter.setFiltertedList(upcomingClassesList);
        }
    }

    private void scrollToFirstMatchedItem(List<scheduleClass> filterlist) {
        if (!filterlist.isEmpty()) {
            scheduleClass firstMatchedItem = filterlist.get(0);
            int position = upcomingClassesList.indexOf(firstMatchedItem);
            if (position != -1) {
                scheduleClass_recView.getLayoutManager().scrollToPosition(position);
            }
        }
    }


    private void initialize(){
        profilepic = findViewById(R.id.userprofilepic);
        usernames = findViewById(R.id.usernameTextView);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        feedbackRequestRef = FirebaseDatabase.getInstance().getReference("feedbackRequests"); // Add this line

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });
        if (currentUser != null) {
            userInfo(currentUser.getUid());
        } else {}
        recyclerSetup();
        buttons();}
    private void buttons(){
        Quranbutton = findViewById(R.id.imgbtn1);
        Prayerbutton = findViewById(R.id.imgbtn2);
        Qiblabutton = findViewById(R.id.imgbtn3);
        Tajweedbutton = findViewById(R.id.imgbtn4);
        Quranbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(teacher_dashboard.this , QuranActivity.class);
                startActivity(intent);
            }
        });
        Prayerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(teacher_dashboard.this , PrayerActivity.class);
                startActivity(intent);
            }
        });
        Qiblabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(teacher_dashboard.this , QiblaActivity.class);
                startActivity(intent);
            }
        });
        Tajweedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(teacher_dashboard.this , TajweedActivity.class);
                startActivity(intent);
            }
        });
    }
    //-------------------------------------Scheduler Adapter Setups---------------------------------------------
    private void recyclerSetup() {
        scheduleClass_recView = findViewById(R.id.recycler_view_teachers);
        scheduleClass_recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new AdapterupcomingClass(upcomingClassesList , teacher_dashboard.this);
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
                                                // If the student ID matches the current user's ID
                                                // Add only the schedules for the current student
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
        String studentID = scheduleSnapshot.child("studentID").getValue(String.class);

        String teacherId = scheduleSnapshot.getRef().getParent().getKey(); // Get teacher ID

        scheduleClass schedule = new scheduleClass();
        schedule.setScheduleID(scheduleId);
        schedule.setTeacherID(teacherId);
        schedule.setStudentID(studentID); // Set studentID
        schedule.setStudentName(studentName);
        schedule.setTutorName(tutorName);
        schedule.setTime(time);
        schedule.setDate(date);
        schedule.setTitle(title);
        adapter.addSchedule(schedule);
    }
    //-------------------------Dialog Box Setup---------------------------------------
    public void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_layout_for_teacher, null);
        builder.setView(dialogView);

        TextView textViewSignOut = dialogView.findViewById(R.id.textViewSignOut);
        TextView textViewManagedProfile = dialogView.findViewById(R.id.textView_managedProfile);
        TextView textViewChats = dialogView.findViewById(R.id.textViewChats);
        TextView textViewScheduledClasses = dialogView.findViewById(R.id.textViewPaymentHistory);
        TextView textViewprogressReport = dialogView.findViewById(R.id.textViewprogressreport);
        TextView textViewUsername = dialogView.findViewById(R.id.usernameTextView);
        CircleImageView imageViewProfile = dialogView.findViewById(R.id.userprofilepic);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get profile information
                    String username = dataSnapshot.child("fullname").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);

                    // Set username text
                    textViewUsername.setText(username);

                    // Load profile image using Glide or any other image loading library
                    Picasso.get().load(profileImageUrl).into(imageViewProfile);
                } else {
                    // Handle case where user data doesn't exist
                    Log.e("ProfileInfo", "User data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("ProfileInfo", "Database error: " + databaseError.getMessage());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        textViewSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish(); // Close current activity
            dialog.dismiss(); // Dismiss dialog
        });

        textViewManagedProfile.setOnClickListener(v -> {
            // Handle payment history option click
            // Example: Open payment history activity
            Intent intent = new Intent(this, UserProfile_Teacher.class);
            startActivity(intent);
            dialog.dismiss(); // Dismiss dialog
        });

        textViewChats.setOnClickListener(v -> {
            // Handle chats option click
            Intent intent = new Intent(this, ChatsList.class);
            startActivity(intent);
            dialog.dismiss();
        });

        textViewScheduledClasses.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScheduleClass.class);
            startActivity(intent);
            dialog.dismiss(); // Dismiss dialog
        });

        textViewprogressReport.setOnClickListener(v -> {

            Intent intent = new Intent(this, ShowList_PR.class);
            startActivity(intent);
            //sendFeedbackRequest();
            dialog.dismiss();
        });
    }
    //-----------------image and username getter------------------------------------
    private void userInfo(String uid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get profile information
                    String username = dataSnapshot.child("fullname").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                    // For demonstration purposes, let's log the retrieved information
                    Log.d("ProfileInfo", "Username: " + username);
                    Log.d("ProfileInfo", "Profile Image URL: " + profileImageUrl);
                    usernames.setText(username);
                    Picasso.get().load(profileImageUrl).into(profilepic);


                } else {
                    // Handle case where user data doesn't exist
                    Log.e("ProfileInfo", "User data does not exist");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("ProfileInfo", "Database error: " + databaseError.getMessage());
            }
        });

    }

}

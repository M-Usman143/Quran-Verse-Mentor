package com.example.quranmentor.activities;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quranmentor.Adapters.AdapterupcomingClass;
import com.example.quranmentor.Adapters.ProgressAdapter;
import com.example.quranmentor.Adapters.selectteacherAdapterClass;
import com.example.quranmentor.R;
import com.example.quranmentor.models.ProgressReport;
import com.example.quranmentor.models.scheduleClass;
import com.example.quranmentor.models.TeacherProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class student_dashboard extends AppCompatActivity {

    CircleImageView profilepic;
    TextView usernames;
    ImageButton imageButton1;
    ImageView movetoschedulACT;
    private ProgressBar progressBar;
    FirebaseAuth mAuth;
    private DatabaseReference userRefrence;
    FirebaseUser currentUser;
    private RecyclerView findingTeacher_recView, scheduleClass_recView, progress_recView;
    ProgressAdapter progressAdapter;
    selectteacherAdapterClass findingTeacher;
    AdapterupcomingClass upcomingClass;
    private List<ProgressReport> studentProgressList = new ArrayList<>();
    private List<scheduleClass> upcomingClassesList = new ArrayList<>();
    private List<TeacherProfile> teacherProfileList = new ArrayList<>();
    private SearchView searchView;
    ImageButton Tajweedbutton, Quranbutton, Qiblabutton, Prayerbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);
        initialize();}

    private void initialize() {
        imageButton1 = findViewById(R.id.imgbtn1);
        profilepic = findViewById(R.id.userprofilepic);
        usernames = findViewById(R.id.usernameTextView);
        progressBar = findViewById(R.id.progressBar);
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
                filterListss(newText);
                return false;
            }
        });


        movetoschedulACT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(student_dashboard.this , ScheduleClass.class);
                startActivity(intent);
            }
        });

        profilepic.setOnClickListener(v -> showOptionsDialog());
        buttons();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            user_Info(currentUser.getUid());
        }

        recyclerSetup();
    }

    private void filterListss(String newText) {
    List<TeacherProfile> filterlist = new ArrayList<>();
   if (!newText.isEmpty()){
       for(TeacherProfile teacherProfiles:teacherProfileList){
           if (teacherProfiles.getName().toLowerCase().contains(newText.toLowerCase())){
               filterlist.add(teacherProfiles);
           }
           findingTeacher.setFiltertedList(filterlist);
               scrollToFirstMatchedItems(filterlist);}
   } else {
       // Reset to original list if search query is empty
       findingTeacher.setFiltertedList(teacherProfileList);}
    }
    private void scrollToFirstMatchedItems(List<TeacherProfile> filterlist) {
        if (!filterlist.isEmpty()) {
            TeacherProfile firstMatchedItem = filterlist.get(0);
            int position = upcomingClassesList.indexOf(firstMatchedItem);
            if (position != -1) {
                scheduleClass_recView.getLayoutManager().scrollToPosition(position);
            }
        }
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
                upcomingClass.setFiltertedList(filterlist);
                scrollToFirstMatchedItem(filterlist);
        } else {
            // Reset to original list if search query is empty
            upcomingClass.setFiltertedList(upcomingClassesList);
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
    //----------------currentuser_info------------------------------------
    private void user_Info(String uid) {
        userRefrence = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        userRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("fullname").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);

                    usernames.setText(username);

                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(student_dashboard.this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.man)
                                .error(R.drawable.men)
                                .into(profilepic);
                    }
                } else {
                    Log.e("ProfileInfo", "User data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ProfileInfo", "Database error: " + databaseError.getMessage());
            }
        });
    }
    //---------------------Adapters setups-------------------------------------------
    private void recyclerSetup() {
        //---------------------------Finding Teacher Adapter----------------------------
        findingTeacher_recView = findViewById(R.id.selectionteacherRecyclerview);
        findingTeacher_recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        findingTeacher = new selectteacherAdapterClass(teacherProfileList, student_dashboard.this);
        findingTeacher_recView.setAdapter(findingTeacher);
        retrieveData(findingTeacher);
    //----------------------------------------Schedule Adapter---------------------
        scheduleClass_recView = findViewById(R.id.scheduleClassRecView);
        scheduleClass_recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        upcomingClass = new AdapterupcomingClass(upcomingClassesList, this);
        scheduleClass_recView.setAdapter(upcomingClass);
        scheduleretrieve(upcomingClass);
    //----------------------------------------Progress Adapter---------------------
        progress_recView = findViewById(R.id.progressChartRecView);
        progress_recView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL, false));
        progressAdapter = new ProgressAdapter(this, studentProgressList);
        progress_recView.setAdapter(progressAdapter);
        retrieveAndDisplayProgressReports();
    }
//-------------------selection of teacher------------------------------------------
    public void retrieveData(selectteacherAdapterClass adapter) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            toggleLoading(true);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("teachers_info");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String userId = snapshot.getKey(); // Get teacher ID
                            String gender = snapshot.child("gender").getValue(String.class);
                            String rate = snapshot.child("rates").getValue(String.class);
                            String city = snapshot.child("city").getValue(String.class);
                            String country = snapshot.child("country").getValue(String.class);

                            DatabaseReference userRoleRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("spinneroption");
                            userRoleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot roleSnapshot) {
                                    if (roleSnapshot.exists()) {
                                        String role = roleSnapshot.getValue(String.class);
                                        if (role.equals("As a Teacher")) {
                                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                                            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                                    if (userSnapshot.exists()) {
                                                        String username = userSnapshot.child("fullname").getValue(String.class);
                                                        String profileImageUrl = userSnapshot.child("profileImageUrl").getValue(String.class);

                                                        TeacherProfile teacherProfile = new TeacherProfile();
                                                        teacherProfile.setTeacherID(userId);
                                                        teacherProfile.setName(username);
                                                        teacherProfile.setGender(gender);
                                                        teacherProfile.setPrice(rate);
                                                        teacherProfile.setCity(city);
                                                        teacherProfile.setCountry(country);
                                                        teacherProfile.setProfileImageUri(profileImageUrl);

                                                        // Check if the teacher ID is already selected by the current student
                                                        if (!isTeacherSelectedByStudent(userId)) {
                                                            adapter.addTeacherProfile(teacherProfile);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Log.e("Firebase", "Failed to retrieve user data: " + databaseError.getMessage());
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("Firebase", "Failed to retrieve user role: " + databaseError.getMessage());
                                }
                            });
                        }
                    } else {
                        Log.e("Firebase", "No teacher profiles found");
                    }
                    new Handler().postDelayed(() -> toggleLoading(false), 5000);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Failed to retrieve teacher info: " + databaseError.getMessage());
                    toggleLoading(false);
                }
            });
        }
    }
//-------------selection of teacher------------------------------------
    private boolean isTeacherSelectedByStudent(String teacherId) {
        for (scheduleClass schedule : upcomingClassesList) {
            if (schedule.getTeacherID().equals(teacherId)) {
                return true; // Teacher is already selected by the student
            }
        }
        return false;
    }
    //-------------------------Dialog Box Setup---------------------------------------
    public void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_layout_for_studnet, null);
        builder.setView(dialogView);

        TextView textViewSignOut = dialogView.findViewById(R.id.textViewSignOut);
        TextView textViewPaymentHistory = dialogView.findViewById(R.id.textViewPaymentHistory);
        TextView textViewChats = dialogView.findViewById(R.id.textViewChats);
        TextView textViewScheduledClasses = dialogView.findViewById(R.id.textViewScheduledClasses);
        TextView textViewFeedback = dialogView.findViewById(R.id.textViewManagedProfile);
        TextView textViewUsername = dialogView.findViewById(R.id.usernameTextView);
        CircleImageView imageViewProfile = dialogView.findViewById(R.id.userprofilepic);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("fullname").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);

                    textViewUsername.setText(username);

                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Picasso.get().load(profileImageUrl).into(imageViewProfile);
                    }
                } else {
                    Log.e("ProfileInfo", "User data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ProfileInfo", "Database error: " + databaseError.getMessage());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        textViewSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(student_dashboard.this, Login.class);
            startActivity(intent);
            finish(); // Close current activity
            dialog.dismiss(); // Dismiss dialog
        });

        textViewPaymentHistory.setOnClickListener(v -> {
            Intent intent = new Intent(student_dashboard.this, PaymentGateway.class);
            startActivity(intent);
            dialog.dismiss(); // Dismiss dialog

        });

        textViewChats.setOnClickListener(v -> {
            Intent intent = new Intent(student_dashboard.this, ChatsList.class);
            startActivity(intent);
            dialog.dismiss();
        });

        textViewScheduledClasses.setOnClickListener(v -> {
            Intent intent = new Intent(student_dashboard.this, ScheduleClass.class);
            startActivity(intent);
            dialog.dismiss(); // Dismiss dialog
        });

        textViewFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(student_dashboard.this , UserProfile_stu.class);
            startActivity(intent);
            dialog.dismiss();
        });
    }
   //-------------------------------------Scheduler Adapter---------------------------------------------
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
                                upcomingClassesList.clear(); // Clear previous data

                                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                                    String teacherId = teacherSnapshot.getKey(); // Get teacher ID

                                    if (userRole.equals("As a Teacher") && teacherId.equals(userId)) {
                                        // If the user is a teacher and the teacher ID matches the current user's ID
                                        for (DataSnapshot scheduleSnapshot : teacherSnapshot.getChildren()) {
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
                                adapter.notifyDataSetChanged(); // Notify adapter of data change
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
        String sID = scheduleSnapshot.getKey();
        scheduleClass schedule = new scheduleClass();
        schedule.setScheduleID(scheduleId);
        schedule.setTeacherID(teacherId);
        schedule.setStudentID(studentID);
        schedule.setStudentName(studentName);
        schedule.setTutorName(tutorName);
        schedule.setTime(time);
        schedule.setDate(date);
        schedule.setTitle(title);
        adapter.addSchedule(schedule);
    }
    //-----------------------------progressions------------------------------
    private void toggleLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            findingTeacher_recView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            findingTeacher_recView.setVisibility(View.VISIBLE);
        }
    }
  //-----------------------------Buttons----------------------------
    private void buttons() {
        Quranbutton = findViewById(R.id.imgbtn1);
        Prayerbutton = findViewById(R.id.imgbtn2);
        Qiblabutton = findViewById(R.id.imgbtn3);
        Tajweedbutton = findViewById(R.id.imgbtn4);

        Quranbutton.setOnClickListener(view -> {
            Intent intent = new Intent(student_dashboard.this, QuranActivity.class);
            startActivity(intent);
        });

        Prayerbutton.setOnClickListener(view -> {
            Intent intent = new Intent(student_dashboard.this, PrayerActivity.class);
            startActivity(intent);
        });

        Qiblabutton.setOnClickListener(view -> {
            Intent intent = new Intent(student_dashboard.this, QiblaActivity.class);
            startActivity(intent);
        });

        Tajweedbutton.setOnClickListener(view -> {
            Intent intent = new Intent(student_dashboard.this, TajweedActivity.class);
            startActivity(intent);
        });
    }
   //----------------------------progressReport----------------------------------
    private void retrieveAndDisplayProgressReports() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            DatabaseReference progressReportRef = FirebaseDatabase.getInstance().getReference()
                    .child("progress_reports").child(currentUserId);

            Query query = progressReportRef.orderByChild("studentID").equalTo(currentUserId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        studentProgressList.clear(); // Clear existing list
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ProgressReport progressReport = snapshot.getValue(ProgressReport.class);
                            if (progressReport != null) {
                                studentProgressList.add(progressReport);
                            }
                        }
                        progressAdapter.notifyDataSetChanged(); // Notify adapter of data change
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
}









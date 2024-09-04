package com.example.quranmentor.Fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.quranmentor.R;
import com.example.quranmentor.models.scheduleClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText titleEditText ,descriptionEditText ;
    private Button saveButton;
    private DatabaseReference schedulesRef;
    private String teacherID;
    private String tutorname;
    private String studentID;
    private String studentUsername;
    private static final String CHANNEL_ID = "schedule_channel";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        schedulesRef = FirebaseDatabase.getInstance().getReference().child("schedules");
        // Initialize views
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        titleEditText = view.findViewById(R.id.title_edit_text);
        descriptionEditText = view.findViewById(R.id.description_edit_text);
        saveButton = view.findViewById(R.id.save_button);
        passingData();
        createNotificationChannel();
        return view;
    }
    private void passingData(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tutorname = dataSnapshot.child("fullname").getValue(String.class);
                } else {
                    // Handle case where user data doesn't exist
                    Log.e("CurrentUser", "User data does not exist");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("CurrentUser", "Database error: " + databaseError.getMessage());
            }
        });

        // Get student's ID and name from arguments
        Bundle args = getArguments();
        if (args != null) {
            studentID = args.getString("studentID");
            studentUsername = args.getString("studentName");
            tutorname = args.getString("teacherName");}
        if(studentID!=null){
            setupSchedule();}
    }

    private void setupSchedule(){
        saveButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get selected date from datePicker
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; // Month starts from 0
            int year = datePicker.getYear();
            String date = day + "-" + month + "-" + year;

            // Get selected time from timePicker
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
           // String time = hour + ":" + minute;

            // Convert 24-hour format to 12-hour format with AM/PM
            String time;
            String amPm;
            if (hour >= 12) {
                amPm = "PM";
                if (hour > 12) {
                    hour -= 12;
                }
            } else {
                amPm = "AM";
            }
            if (hour == 0) {
                hour = 12;
            }
            String formattedHour = String.format(Locale.getDefault(), "%02d", hour);
            String formattedMinute = String.format(Locale.getDefault(), "%02d", minute);
            time = formattedHour + ":" + formattedMinute + " " + amPm;

            // Get title and description from EditTexts
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            // Validate input
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(getContext(), "Please enter title and description", Toast.LENGTH_SHORT).show();
                return;
            }
            // Get current user's ID (teacherID)
            String teacherId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Save schedule to database
            saveScheduleToDatabase(teacherId, date, time, title, description);
        }
    });

}
    private void saveScheduleToDatabase(String teacherId, String date, String time, String title, String description) {
        DatabaseReference teacherSchedulesRef = schedulesRef.child(teacherId);

        String scheduleId = teacherSchedulesRef.push().getKey();

        scheduleClass schedule = new scheduleClass(studentID,studentUsername,title,description,date,time
                ,tutorname,teacherId , scheduleId);
        schedule.setStudentID(studentID);
        schedule.setStudentName(studentUsername);
        teacherSchedulesRef.child(scheduleId).setValue(schedule)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showNotification();
                            Toast.makeText(getContext(), "Class Scheduled successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to save schedule", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.experience) // Add your notification icon here
                .setContentTitle("Class Scheduled")
                .setContentText("Your class has been scheduled successfully.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ScheduleChannel";
            String description = "Channel for scheduling notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
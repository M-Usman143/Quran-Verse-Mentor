package com.example.quranmentor.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;

import com.example.quranmentor.R;
import com.example.quranmentor.models.Give_Feedback;
import com.example.quranmentor.models.TeacherProfile;
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

public class Feedback extends AppCompatActivity {
    private TextInputLayout commentEditText;
    private RatingBar feedback_ratingBar;
    private AppCompatButton submitButton , skipButton;
    private DatabaseReference feedbackRef;
    private FirebaseAuth mAuth;
    private String teacherId,teacherName, studentName , studentId;
    private CircleImageView userprofile;
    private TextView username;
    private CheckBox checkBox1 , checkBox2 , checkBox3;
    private static final String CHANNEL_ID = "feedback_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        commentEditText = findViewById(R.id.comment);
        feedback_ratingBar = findViewById(R.id.teacherRate);
        submitButton = findViewById(R.id.feedback_submitBTN);
        userprofile = findViewById(R.id.userprofilepic);
        username = findViewById(R.id.usernameTextView);

        feedbackRef = FirebaseDatabase.getInstance().getReference().child("feedback");
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        teacherId = intent.getStringExtra("teacherId");
        studentId = intent.getStringExtra("studentId");
        teacherName = intent.getStringExtra("teacherName");
        studentName = intent.getStringExtra("studentName");
        username.setText(teacherName);

        createNotificationChannel();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            submitFeedback();
            }
        });
        TeacherProfile teacherProfile = getTeacherProfileById(teacherId);
        if (teacherProfile != null) {
            String profileImageUrl = teacherProfile.getProfileImageUri();
            Picasso.get().load(profileImageUrl).into(userprofile);
        } else {}
        chkbox();
    }
    private void chkbox(){
        skipButton = findViewById(R.id.skipbtn);
       skipButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Feedback.this , student_dashboard.class);
        startActivity(intent);
                finish();
    }
});
        checkBox1 = findViewById(R.id.chkbox1);
        checkBox2 = findViewById(R.id.chkbox2);
        checkBox3 = findViewById(R.id.chkbox3);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                }
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox1.setChecked(false);
                    checkBox3.setChecked(false);
                }
            }
        });

        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                }
            }
        });

    }
    private TeacherProfile getTeacherProfileById(String teacherId) {
// Assuming teacherId contains the ID of the teacher whose profile image you want to retrieve
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(teacherId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                    // Now you have the profile image URL, you can load it into an ImageView using Picasso or Glide
                    // For example, using Picasso:
                    Picasso.get().load(profileImageUrl).into(userprofile);
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
return null;
    }

    private void submitFeedback() {
        float rating = feedback_ratingBar.getRating();
        String comment = commentEditText.getEditText().getText().toString().trim();
        long timestamp = System.currentTimeMillis();


        if (!comment.isEmpty()) {
            String feedbackId = feedbackRef.child(teacherId).push().getKey(); // Generate unique feedback ID under teacherID
            FirebaseUser currentUser = mAuth.getCurrentUser();
          //  String studentId = currentUser.getUid();

            Give_Feedback feedback = new Give_Feedback(teacherId,teacherName,studentId,studentName,rating,comment,feedbackId,timestamp);
            feedbackRef.child(teacherId).child(feedbackId).setValue(feedback); // Store feedback under teacherID
            Toast.makeText(Feedback.this, "ThankYou For Your Feedback", Toast.LENGTH_SHORT).show();
            showNotification();

        } else {
            // Show error message if comment is empty
            Toast.makeText(Feedback.this, "Please provide a comment", Toast.LENGTH_SHORT).show();
        }
    }


    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.experience) // Add your feedback icon here
                .setContentTitle("Feedback")
                .setContentText("Your feedback is submitted. Thank you for it.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "FeedbackChannel";
            String description = "Channel for feedback notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
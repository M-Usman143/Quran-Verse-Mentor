package com.example.quranmentor.Adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranmentor.Fragments.Feedback_receiving_bottom_sheet;
import com.example.quranmentor.R;
import com.example.quranmentor.activities.Feedback;
import com.example.quranmentor.activities.Voice_call;
import com.example.quranmentor.models.TeacherProfile;
import com.example.quranmentor.models.scheduleClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallType;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;
import java.util.List;

public class AdapterupcomingClass extends RecyclerView.Adapter<AdapterupcomingClass.ViewHolder> {
    private List<scheduleClass> upcomingClassesList;
    private Context context;
    public AdapterupcomingClass(List<scheduleClass> upcomingClassesList, Context context) {
        this.upcomingClassesList = upcomingClassesList;
        this.context = context;
    }
    public void addSchedule(scheduleClass schedule) {
        upcomingClassesList.add(schedule);
        notifyDataSetChanged();
    }
    public void setFiltertedList(List<scheduleClass> filterList){
        this.upcomingClassesList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        scheduleClass upcomingClass = upcomingClassesList.get(position);
        holder.bind(upcomingClass);

    }
    @Override
    public int getItemCount() {
        return upcomingClassesList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTime, textViewDate;
        private TextView textViewSubject;
        private AppCompatButton joinclassBTN;
        private TextView textViewstudent, textViewtutor;
        private String currentUserId, targetUserId;
        FirebaseAuth mAuth;
        Context activityContext;
        private DatabaseReference usersRef;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewSubject = itemView.findViewById(R.id.textViewSubject);
            textViewtutor = itemView.findViewById(R.id.textViewTutor);
            textViewstudent = itemView.findViewById(R.id.textViewstudent);
            joinclassBTN = itemView.findViewById(R.id.joinclassBTN);
            mAuth = FirebaseAuth.getInstance();
            usersRef = FirebaseDatabase.getInstance().getReference("users");

        }
        public void bind(scheduleClass upcomingClass) {
            String DateSchedule = "Date: " + upcomingClass.getDate();
            textViewDate.setText(DateSchedule);
            String timeSchedule = "Today: " + upcomingClass.getTime();
            textViewTime.setText(timeSchedule);
            textViewSubject.setText(upcomingClass.getTitle());
            String student = "Student Name: " + upcomingClass.getStudentName();
            textViewstudent.setText(student);
            String teacher = "Instructor: " + upcomingClass.getTutorName();
            textViewtutor.setText(teacher);

            // Initialize Firebase Authentication
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                currentUserId = currentUser.getUid();
                proceeding(currentUserId);
                if (currentUserId.equals(upcomingClass.getTeacherID())) {
                   // Toast.makeText(context, "Welcome to Your Dashbaord", Toast.LENGTH_SHORT).show();
                    targetUserId = upcomingClass.getStudentID();
                } else {
                   // Toast.makeText(context, "Welcome to Your Dashbaord", Toast.LENGTH_SHORT).show();
                    targetUserId = upcomingClass.getTeacherID();
                }
                // Request necessary permissions
              //  requestPermissions();
            } else {
                Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show();
            }



            itemView.setOnClickListener(view -> {
                if (currentUser != null) {
                    usersRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String role = snapshot.child("spinneroption").getValue(String.class);
                            if ("As a Student".equals(role)) {
                                Feedback_receiving_bottom_sheet feedbackBottomSheet = new Feedback_receiving_bottom_sheet();
                                Bundle bundle = new Bundle();
                                bundle.putString("teacherId", upcomingClass.getTeacherID());
                                bundle.putString("studentId", upcomingClass.getStudentID());
                                bundle.putString("teacherName", upcomingClass.getTutorName());
                                bundle.putString("studentName", upcomingClass.getStudentName());
                                feedbackBottomSheet.setArguments(bundle);
                                feedbackBottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "FeedbackBottomSheet");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "Failed to retrieve user role", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show();
                }
            });

            joinclassBTN.setOnClickListener(view -> {
                String teacherId = upcomingClass.getTeacherID();
                String studentId = upcomingClass.getStudentID();
                String scheduleID = upcomingClass.getScheduleID();
                String studentName = upcomingClass.getStudentName();
                String teacherName = upcomingClass.getTutorName();

                Intent intent = new Intent(context, Voice_call.class);
                intent.putExtra("teacherId", teacherId);
                intent.putExtra("studentId", studentId);
                intent.putExtra("scheduleId", scheduleID);
                intent.putExtra("teacherName", teacherName);
                intent.putExtra("studentName", studentName);
                intent.putExtra("currentUserId", currentUserId);
                intent.putExtra("targetUserId", targetUserId);

                context.startActivity(intent);
            });

        }
        private void proceeding(String userID) {
            Application application = (Application) context.getApplicationContext();
            long appID = 2062246637; // yourAppID
            String appSign = "fe45976d7687b47a0b10a86ca6184a39c64cf3413ad8311b0e0705df2af162c8  "; // yourAppSign
            String userName = userID; // yourUserName
            ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
            ZegoUIKitPrebuiltCallService.init(application, appID, appSign, userID, userName, callInvitationConfig);
        }
        private void requestPermissions() {
            PermissionX.init((FragmentActivity) context)
                    .permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
                    .onExplainRequestReason(new ExplainReasonCallback() {
                        @Override
                        public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
                            String message = "We need your consent for the following permissions in order to use the offline call function properly";
                            scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny");
                        }
                    })
                    .request(new RequestCallback() {
                        @Override
                        public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                            if (!allGranted) {
                                Toast.makeText(context, "Permissions denied: " + deniedList, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}















































//package com.example.quranmentor.activities;
//
//import android.Manifest;
//import android.app.Application;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.fragment.app.FragmentActivity;
//
//import com.example.quranmentor.R;
//import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.permissionx.guolindev.PermissionX;
//import com.permissionx.guolindev.callback.ExplainReasonCallback;
//import com.permissionx.guolindev.callback.RequestCallback;
//import com.permissionx.guolindev.request.ExplainScope;
//import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
//import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallFragment;
//import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService;
//import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
//import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
//
//import java.util.List;
//
//public class Call_Activity extends AppCompatActivity {
//    TextInputLayout usernameET;
//    AppCompatButton appCompatButton;
//    private String username;
//    private String teacherId, studentId;
//    private String teacherName, studentName;
//    private String currentUserId, targetUserId;
//    private String scheduleId;
//    FirebaseAuth mAuth;
//    Context activityContext;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_call);
//        funT();
//    }
//    private void funT(){
//        activityContext = this;
//        usernameET = findViewById(R.id.usernameET);
//        appCompatButton = findViewById(R.id.userIDBTN);
//        mAuth = FirebaseAuth.getInstance();
//        // Get the teacher ID and student ID from the intent
//        teacherId = getIntent().getStringExtra("teacherId");
//        studentId = getIntent().getStringExtra("studentId");
//        scheduleId = getIntent().getStringExtra("scheduleId");
//        studentName = getIntent().getStringExtra("studentName");
//        teacherName = getIntent().getStringExtra("teacherName");
//        // Get the current user ID
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            currentUserId = currentUser.getUid();
//            proceeding(currentUserId);
//            if(currentUserId.equals(teacherId)){
//                Toast.makeText(activityContext, "teacher Identified", Toast.LENGTH_SHORT).show();
//                targetUserId=studentId;
//            }
//            else {
//                Toast.makeText(activityContext, "student Identified", Toast.LENGTH_SHORT).show();
//                targetUserId=teacherId;
//            }
//        } else {
//            // Handle the case where the current user is not authenticated
//            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//        // Request necessary permissions
//        PermissionX.init((FragmentActivity) activityContext).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
//                .onExplainRequestReason(new ExplainReasonCallback() {
//                    @Override
//                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
//                        String message = "We need your consent for the following permissions in order to use the offline call function properly";
//                        scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny");
//                    }
//                }).request(new RequestCallback() {
//                    @Override
//                    public void onResult(boolean allGranted, @NonNull List<String> grantedList,
//                                         @NonNull List<String> deniedList) {
//                        if (!allGranted) {
//                            Toast.makeText(activityContext, "Permissions denied: " + deniedList, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//        appCompatButton.setOnClickListener(view -> {
//            username = usernameET.getEditText().getText().toString();
//            if (!username.isEmpty()) {
//                Intent intent = new Intent(Call_Activity.this, Voice_call.class);
//                intent.putExtra("currentUserId", currentUserId);
//                intent.putExtra("targetUserId", targetUserId);
//                intent.putExtra("scheduleId", scheduleId);
//                intent.putExtra("studentId", studentId);
//                intent.putExtra("teacherId", teacherId);
//                intent.putExtra("username", username);
//                intent.putExtra("studentName", studentName);
//                intent.putExtra("teacherName", teacherName);
//                startActivity(intent);
//            } else {
//                Toast.makeText(Call_Activity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    private void proceeding(String userID) {
//        Application application = getApplication(); // Android's application context
//        long appID = 785842282;   // yourAppID
//        String appSign = "7a06dc1dcbb3309761f16ccef62439d9e0d168d4e9098d47a78d0da67d034418";  // yourAppSign
//        String userName = userID;   // yourUserName
//        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
//        ZegoUIKitPrebuiltCallService.init(getApplication(), appID, appSign, userID, userName, callInvitationConfig);}
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ZegoUIKitPrebuiltCallInvitationService.unInit();
//    }
//
//}




















//   private void fetchStudentIdFromDatabase(String teacherId, String currentUserId) {
//        DatabaseReference schedulesRef = FirebaseDatabase.getInstance().getReference().child("schedules").child(teacherId);
//        schedulesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()) {
//                    String studentIdFromDb = scheduleSnapshot.child("studentID").getValue(String.class);
//                    if (studentIdFromDb != null) {
//                        studentId = studentIdFromDb;
//                        // Determine the target user ID based on the current user ID
//                        if (currentUserId.equals(teacherId)) {
//                            Toast.makeText(activityContext, "Teacher Identified", Toast.LENGTH_SHORT).show();
//                            targetUserId = studentId;
//                        } else {
//                            Toast.makeText(activityContext, "Student Identified", Toast.LENGTH_SHORT).show();
//                            targetUserId = teacherId;
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("Firebase", "Failed to retrieve student ID: " + databaseError.getMessage());
//            }
//        });
//    }

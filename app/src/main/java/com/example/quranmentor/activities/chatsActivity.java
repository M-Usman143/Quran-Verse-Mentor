package com.example.quranmentor.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranmentor.Fragments.BottomSheetFragment;
import com.example.quranmentor.R;
import com.example.quranmentor.models.ChatingSystem;
import com.example.quranmentor.Adapters.messageAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatsActivity extends AppCompatActivity {
    private CircleImageView imageViewProfile;
    private TextView textViewUsername;
    private TextView textViewTypingIndicator;
    private LinearLayout message_input_layout;
    private RelativeLayout headerChatLayouts;
    private LinearLayout message_send_button_layout;
    private RecyclerView recyclerView;
    private messageAdapter messageAdapter;
    private List<ChatingSystem> messageList;
    private DatabaseReference messageRef;
    private String senderId;
    private String receiverID;
    private AppCompatEditText messageInput;
    private ImageButton buttonSend;
    private ImageView movingImg;
    private ValueEventListener valueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_teachers);

        imageViewProfile = findViewById(R.id.image_view_profile);
        textViewUsername = findViewById(R.id.text_view_username);
        textViewTypingIndicator = findViewById(R.id.text_view_typing_indicator);
        headerChatLayouts = findViewById(R.id.header_chat_layouts);
        message_send_button_layout = findViewById(R.id.message_send_button_layout);
        message_input_layout = findViewById(R.id.message_input_layout);
        movingImg = findViewById(R.id.schedulemovingImg);
        messageInput = findViewById(R.id.edit_text_message);
        recyclerView = findViewById(R.id.recycler_view_messages);
        buttonSend = findViewById(R.id.message_send_btn);
        messageList = new ArrayList<>();
        chkuser();
        headerData();
        addKeyboardVisibilityListener();

    }
    private void chkuser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("spinneroption");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userRole = dataSnapshot.getValue(String.class);
                        if (userRole != null && userRole.equals("As a Teacher")) {
                            movingImg.setVisibility(View.VISIBLE);
                            movingImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showScheduleDialog();
                                }
                            });
                        } else {
                            movingImg.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(chatsActivity.this, "User role data does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(chatsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void showScheduleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to schedule class with this student?")
                .setPositiveButton("Yes", (dialog, id) -> openBottomSheet())
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void openBottomSheet() {
        if (receiverID != null && !TextUtils.isEmpty(senderId) && !TextUtils.isEmpty(textViewUsername.getText().toString())) {
            String studentId = receiverID; // Assuming userId represents the student ID
            String studentName = textViewUsername.getText().toString(); // Assuming textViewUsername displays the student's name
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            Bundle args = new Bundle();
            args.putString("studentID", studentId);
            args.putString("studentName", studentName);
            bottomSheetFragment.setArguments(args);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        } else {
            Toast.makeText(this, "Failed to open bottom sheet. Missing user information.", Toast.LENGTH_SHORT).show();
        }
    }
    private void headerData() {
        senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = getIntent();
        if (intent != null) {
            String teacherUsername = intent.getStringExtra("teacherUsername");
            String teacherProfileImage = intent.getStringExtra("teacherProfileImage");
            receiverID = intent.getStringExtra("teacherID");
            if (receiverID != null) {
                // Retrieve messages for the selected teacher
                retrieveMessagesForTeacher();
                // Set click listener for send button
                buttonSend.setOnClickListener(v -> sendMessage());
            }
            Picasso.get().load(teacherProfileImage).into(imageViewProfile);
            textViewUsername.setText(teacherUsername);
        } else {
            Log.e("TeacherID", "Intent is null");
        }

    }
    private void retrieveMessagesForTeacher() {
        String currentUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("chats")
                .child(currentUserID).child(receiverID);
        messageList.clear();

        if (valueEventListener != null) {
            messagesRef.removeEventListener(valueEventListener);
        }

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatingSystem message = snapshot.getValue(ChatingSystem.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                setUpRecyclerView();
                recyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.e("Firebase", "Failed to retrieve chats: " + databaseError.getMessage());
            }
        };
        messagesRef.addValueEventListener(valueEventListener);
    }
    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new messageAdapter(messageList, this, senderId);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
    }
    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!TextUtils.isEmpty(messageText)) {
            String senderId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            long timestamp = System.currentTimeMillis();

            DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("chats").child(receiverID).child(senderId);
            DatabaseReference messagesRef1 = FirebaseDatabase.getInstance().getReference("chats").child(senderId).child(receiverID);
            String messageId = messagesRef.push().getKey();

            ChatingSystem chatingSystem = new ChatingSystem(messageText, messageId, timestamp, senderId, receiverID , false);

            if (messageId != null) {
                messagesRef.child(messageId).setValue(chatingSystem)
                        .addOnSuccessListener(aVoid -> {
                            // Message sent successfully
                            Log.d("SendMessage", "Message sent successfully");
                            // Clear message input field
                            messageInput.setText("");
                        })
                        .addOnFailureListener(e -> {
                            // Error occurred while sending message
                            Log.e("SendMessage", "Error sending message: " + e.getMessage());
                            // Show error message to the user, if needed
                            Toast.makeText(chatsActivity.this, "Failed to send message. Please try again.", Toast.LENGTH_SHORT).show();
                        });
                // Save the message under the receiver's node
                messagesRef1.child(messageId).setValue(chatingSystem)
                        .addOnSuccessListener(aVoid -> {
                            // Message sent successfully
                            Log.d("SendMessage", "Message sent successfully");
                        })
                        .addOnFailureListener(e -> {
                            // Error occurred while sending message
                            Log.e("SendMessage", "Error sending message: " + e.getMessage());
                            // Handle error, if needed
                        });

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove ValueEventListener when activity is destroyed to avoid memory leaks
        if (valueEventListener != null && messageRef != null) {
            messageRef.removeEventListener(valueEventListener);
        }
    }
    private void addKeyboardVisibilityListener() {
        View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getHeight();
                int keypadHeight = screenHeight - r.bottom;
                int distanceFromKeyboard = 120;
                keypadHeight -= distanceFromKeyboard;
                keypadHeight = Math.max(0, keypadHeight);

                boolean isKeyboardOpen = keypadHeight > screenHeight * 0.20;

                if (isKeyboardOpen) {
                    int paddingBottom = keypadHeight; // Adjust padding to the height of the keyboard
                    message_input_layout.setPadding(0, 0, 0, paddingBottom);
                    message_send_button_layout.setPadding(0, 0, 0, paddingBottom);
                } else {
                    message_input_layout.setPadding(0, 0, 0, 0);
                    message_send_button_layout.setPadding(0, 0, 0, 0);
                }
            }
        });
    }
}















//    private void retrieveMessagesForTeacher(String teacherId) {
//        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("chats").child(teacherId).child(receiverID);
//        messageList.clear();
//
//        if (valueEventListener != null) {
//            messagesRef.removeEventListener(valueEventListener);
//        }
//
//        valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                messageList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    ChatingSystem message = snapshot.getValue(ChatingSystem.class);
//                    if (message != null) {
//                        messageList.add(message);}
//                }
//                setUpRecyclerView();
//                recyclerView.scrollToPosition(messageList.size() - 1);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle error
//                Log.e("Firebase", "Failed to retrieve chats: " + databaseError.getMessage());
//            }
//        };
//        messagesRef.addValueEventListener(valueEventListener);
//    }


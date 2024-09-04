package com.example.quranmentor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranmentor.R;
import com.example.quranmentor.activities.chatsActivity;
import com.example.quranmentor.models.ChatingSystem;
import com.example.quranmentor.models.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.MessageViewHolder> {
    private ArrayList<user> userProfileList;
    private Context context;
    private Map<String, Integer> unreadMessageCountMap = new HashMap<>();
    private ChildEventListener messageListener;

    public ProfileListAdapter(ArrayList<user> userProfileList, Context context) {
        this.userProfileList = userProfileList;
        this.context = context;
        fetchUnreadMessageCounts();
        setupMessageListener();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_chat_view, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        user userProfile = userProfileList.get(position);
        holder.nameTextView.setText(userProfile.getFullname());
        Picasso.get().load(userProfile.getProfileImageUrl()).placeholder(R.drawable.men).into(holder.profileImageView);

        // Construct the correct database reference path
        String currentUserId = getCurrentUserId();
        String chatPath = "chats/" + currentUserId + "/" + userProfile.getUserID();

        // Retrieve the last message for the current user
        DatabaseReference lastMessageRef = FirebaseDatabase.getInstance().getReference().child(chatPath);
        lastMessageRef.orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ChatingSystem lastMessage = dataSnapshot.getValue(ChatingSystem.class);
                        if (lastMessage != null) {
                            holder.lastmessage.setText(lastMessage.getMessage());
                            holder.timestampTextView.setText(formatTimeStamp(lastMessage.getTimestamp()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving last message: " + error.getMessage());
            }
        });

        // Check if there are unread messages and update UI accordingly
        if (unreadMessageCountMap.containsKey(userProfile.getUserID())) {
            int unreadCount = unreadMessageCountMap.get(userProfile.getUserID());
            if (unreadCount > 0 && !holder.isProfileClicked) {
                holder.messageNumber.setText(String.valueOf(unreadCount));
                holder.messageNumber.setVisibility(View.VISIBLE);
                holder.relativeLayout.setVisibility(View.VISIBLE);
            } else {
                holder.messageNumber.setVisibility(View.GONE);
                holder.relativeLayout.setVisibility(View.GONE);
            }
        } else {
            holder.messageNumber.setVisibility(View.GONE);
            holder.relativeLayout.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.isProfileClicked = true; // Mark profile as clicked

                String teacherId = userProfile.getUserID();
                Intent intent = new Intent(context, chatsActivity.class);
                intent.putExtra("teacherID", teacherId);
                intent.putExtra("teacherUsername", userProfile.getFullname());
                intent.putExtra("teacherProfileImage", userProfile.getProfileImageUrl());
                context.startActivity(intent);
                holder.messageNumber.setVisibility(View.GONE);
                markMessagesAsRead(teacherId); // Mark messages as read when profile is clicked
            }
        });
    }

    private void fetchUnreadMessageCounts() {
        String currentUserId = getCurrentUserId();
        if (currentUserId == null) return;

        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference().child("chats").child(currentUserId);
        chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                unreadMessageCountMap.clear();
                for (DataSnapshot userChatSnapshot : snapshot.getChildren()) {
                    String userId = userChatSnapshot.getKey();
                    if (userId != null && !userId.equals(currentUserId)) {
                        int unreadCount = 0;
                        for (DataSnapshot messageSnapshot : userChatSnapshot.getChildren()) {
                            ChatingSystem message = messageSnapshot.getValue(ChatingSystem.class);
                            if (message != null && !message.isRead() && message.getReceiverID().equals(currentUserId)) {
                                unreadCount++;
                            }
                        }
                        unreadMessageCountMap.put(userId, unreadCount);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching unread message counts: " + error.getMessage());
            }
        });
    }

    private void setupMessageListener() {
        String currentUserId = getCurrentUserId();
        if (currentUserId == null) return;

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("chats").child(currentUserId);
        messageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                fetchUnreadMessageCounts(); // Update unread message counts when a new message is added
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                fetchUnreadMessageCounts(); // Update unread message counts when a message is changed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                fetchUnreadMessageCounts(); // Update unread message counts when a message is removed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Not needed in this context
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error listening for message changes: " + error.getMessage());
            }
        };

        rootRef.addChildEventListener(messageListener);
    }

    private void markMessagesAsRead(String userId) {
        String currentUserId = getCurrentUserId();
        if (currentUserId == null) return;

        DatabaseReference userChatRef = FirebaseDatabase.getInstance().getReference().child("chats").child(currentUserId).child(userId);
        userChatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    ChatingSystem message = messageSnapshot.getValue(ChatingSystem.class);
                    if (message != null && message.getReceiverID().equals(currentUserId) && !message.isRead()) {
                        messageSnapshot.getRef().child("read").setValue(true);
                    }
                }
                // Update the unread message count map
                unreadMessageCountMap.put(userId, 0);
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error marking messages as read: " + error.getMessage());
            }
        });
    }

    private String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
    }

    private String formatTimeStamp(long lastMessageTimestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE HH:mm", Locale.getDefault());
        return sdf.format(new Date(lastMessageTimestamp));
    }

    @Override
    public int getItemCount() {
        return userProfileList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImageView;
        TextView nameTextView;
        TextView timestampTextView, messageNumber;
        TextView lastmessage;
        boolean isProfileClicked = false; // Flag to track if profile is clicked/opened
        RelativeLayout relativeLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);


                        profileImageView = itemView.findViewById(R.id.image_view_user_avatar);
            nameTextView = itemView.findViewById(R.id.usernameTextView);
            timestampTextView = itemView.findViewById(R.id.text_view_timestamp);
            lastmessage = itemView.findViewById(R.id.last_message_content);
            messageNumber = itemView.findViewById(R.id.messagesNumber);
            relativeLayout = itemView.findViewById(R.id.chatnumber_bg);


        }
    }
}

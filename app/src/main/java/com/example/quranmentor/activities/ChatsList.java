//package com.example.quranmentor.activities;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.quranmentor.Adapters.ProfileListAdapter;
//import com.example.quranmentor.R;
//import com.example.quranmentor.models.user;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ChatsList extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private ProfileListAdapter adapter;
//    private ArrayList<user> userProfileList;
//    private DatabaseReference usersRef;
//    private String currentUserId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.chat_lists);
//
//        recyclerView = findViewById(R.id.recycler_view_messages_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        userProfileList = new ArrayList<>();
//        adapter = new ProfileListAdapter(userProfileList, this);
//        recyclerView.setAdapter(adapter);
//
//        // Get current user ID
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            currentUserId = currentUser.getUid();
//            usersRef = FirebaseDatabase.getInstance().getReference().child("users");
//            fetchChats();
//        } else {
//            Log.e("ChatsList", "Current user is null");
//            // Handle the case where current user is null (not authenticated)
//        }
//    }
//
//    private void fetchChats() {
//        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference()
//                .child("chats").child(currentUserId);
//        chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot receiverSnapshot : dataSnapshot.getChildren()) {
//                        String receiverId = receiverSnapshot.getKey(); // This is the receiverId
//
//                        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference()
//                                .child("chats").child(currentUserId).child(receiverId);
//                        messagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.exists()) {
//                                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
//                                        String messageId = messageSnapshot.getKey();
//                                        String senderId = messageSnapshot.child("userID").getValue(String.class);
//                                        String receiverId = messageSnapshot.child("receiverID").getValue(String.class);
//
//                                        if (senderId != null && !senderId.equals(currentUserId)) {
//                                            fetchUserProfile(senderId);
//                                        }
//                                        if (receiverId != null && !receiverId.equals(currentUserId)) {
//                                            fetchUserProfile(receiverId);
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                Log.e("ChatsList", "messagesRef onCancelled: " + error.getMessage());
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("ChatsList", "chatsRef onCancelled: " + databaseError.getMessage());
//            }
//        });
//    }
//
//    private void fetchUserProfile(String userId) {
//        Query userQuery = usersRef.child(userId);
//        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String fullname = dataSnapshot.child("fullname").getValue(String.class);
//                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
//
//                    user userProfile = new user(fullname, profileImageUrl);
//                    userProfile.setUserID(userId); // Set the user ID
//
//                    if (!userProfileList.contains(userProfile)) {
//                        userProfileList.add(userProfile);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("ChatsList", "fetchUserProfile onCancelled: " + databaseError.getMessage());
//            }
//        });
//    }
//}



package com.example.quranmentor.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quranmentor.Adapters.ProfileListAdapter;
import com.example.quranmentor.R;
import com.example.quranmentor.models.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProfileListAdapter adapter;
    private ArrayList<user> userProfileList;
    private DatabaseReference usersRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_lists);

        recyclerView = findViewById(R.id.recycler_view_messages_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userProfileList = new ArrayList<>();
        adapter = new ProfileListAdapter(userProfileList, this);
        recyclerView.setAdapter(adapter);

        // Get current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
            usersRef = FirebaseDatabase.getInstance().getReference().child("users");
            fetchChats();
        } else {
            Log.e("ChatsList", "Current user is null");
            // Handle the case where current user is null (not authenticated)
        }
    }

    private void fetchChats() {
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference()
                .child("chats").child(currentUserId);
        chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot receiverSnapshot : dataSnapshot.getChildren()) {
                        String receiverId = receiverSnapshot.getKey(); // This is the receiverId
                        fetchUserProfile(receiverId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ChatsList", "chatsRef onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void fetchUserProfile(String userId) {
        Query userQuery = usersRef.child(userId);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fullname = dataSnapshot.child("fullname").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);

                    user userProfile = new user(fullname, profileImageUrl);
                    userProfile.setUserID(userId); // Set the user ID

                    if (!userProfileList.contains(userProfile)) {
                        userProfileList.add(userProfile);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ChatsList", "fetchUserProfile onCancelled: " + databaseError.getMessage());
            }
        });
    }
}

package com.example.quranmentor.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quranmentor.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zegocloud.uikit.ZegoUIKit;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;

public class Voice_call extends AppCompatActivity {
    EditText TargetID;
    TextView showTargetID;
    ZegoSendCallInvitationButton voiceBtn , videoBtn;
    private String username;
    private String targetUserId;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);
        FunT();}

    private void FunT(){
        showTargetID = findViewById(R.id.showtargetID);
        TargetID = findViewById(R.id.usernamesET);
        voiceBtn = findViewById(R.id.voicecallbtn);
        videoBtn = findViewById(R.id.videocallbtn);

        // Extract data from intent
        username = getIntent().getStringExtra("username");
        targetUserId = getIntent().getStringExtra("targetUserId");
        currentUserId = getIntent().getStringExtra("currentUserId");

        // Display the user IDs
        showTargetID.setText(targetUserId);

        TargetID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newTargetUsername = TargetID.getText().toString();
                serVoiceCall(newTargetUsername);
                serVideoCall(newTargetUsername);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
    void serVideoCall(String targetUserID){
        videoBtn.setIsVideoCall(true);
        videoBtn.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
        videoBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID)));}
    void serVoiceCall(String targetUserID){
        voiceBtn.setIsVideoCall(false);
        voiceBtn.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
        voiceBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID)));}


}
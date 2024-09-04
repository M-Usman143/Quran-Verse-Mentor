package com.example.quranmentor.models;

import android.net.Uri;

public class MessageListProfile {
  //  private String userId;
    private String name;
    private String profileImageUrl; // Assuming profile image is stored as URI
    private String lastMessageContent;
    private long lastMessageTimestamp;

    public MessageListProfile() {}

    public MessageListProfile(String name, String profileImageUrl, String lastMessageContent, long lastMessageTimestamp) {
       // this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.lastMessageContent = lastMessageContent;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public long getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(long lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}


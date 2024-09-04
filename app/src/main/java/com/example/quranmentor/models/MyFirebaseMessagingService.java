package com.example.quranmentor.models;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Handle incoming messages here
        if (remoteMessage.getNotification() != null) {
            // Display the notification
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            // Customize notification display logic based on your app's requirements
            NotificationHelper.showNotification(getApplicationContext(), title, body);
        }
    }

}

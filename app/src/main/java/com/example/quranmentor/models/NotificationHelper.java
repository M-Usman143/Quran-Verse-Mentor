package com.example.quranmentor.models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.quranmentor.activities.Feedback;
import com.example.quranmentor.R;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;

public class NotificationHelper {

    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID"; // Unique channel ID
    private static final int NOTIFICATION_ID = 1; // Unique notification ID

    public static void showNotification(Context context, String title, String body) {
        // Create an explicit intent for an activity in your app
        Intent intent = new Intent(context, Feedback.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create a notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Description");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24) // Set the small icon for the notification
                .setContentTitle(title) // Set the title for the notification
                .setContentText(body) // Set the body text for the notification
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set the priority for the notification
                .setContentIntent(pendingIntent) // Set the intent to be triggered when the notification is clicked
                .setAutoCancel(true); // Automatically dismiss the notification when clicked

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}



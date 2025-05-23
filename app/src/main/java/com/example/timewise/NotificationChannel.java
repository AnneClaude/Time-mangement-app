package com.example.timewise;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationChannel {
    // This is a class that helps in creating a notification channel.
    //set of behaviors for notifications—like sound, vibration, and importance—grouped under a named category.
    // Its purpose is to give users control over how different types of notifications appear,
    // and it's required for showing notifications on Android 8.0 (API 26) and above.

    // Constants for notification channel properties:
    public static final String CHANNEL_ID = "notificationId"; //ID for the notification channel.
    public static final CharSequence CHANNEL_NAME = "Notification_1";// The name of the channel.
    public static final String CHANNEL_DESCRIPTION = "Channel Description";
    public NotificationChannel() {
        // Default constructor. Not doing anything here but required for instantiating the class.
    }

    public static void createNotificationChannel(Context context) {
        // Method to create the notification channel.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Checks if the Android version is Oreo (API level 26) or higher.
            // Notification channels are supported only in Android 8.0 and above.

            android.app.NotificationChannel channel = new android.app.NotificationChannel(
                    CHANNEL_ID, // ID of the channel.
                    CHANNEL_NAME, // Name of the channel displayed to the user.
                    NotificationManager.IMPORTANCE_DEFAULT
                    // The importance level determines how notifications in this channel behave.
                    // IMPORTANCE_DEFAULT means notifications will make a sound and appear in the status bar.
            );

            channel.setDescription(CHANNEL_DESCRIPTION);
            // Sets the description of the channel.

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            // Retrieves the system's NotificationManager service, which is used to manage notifications.

            if (notificationManager != null) {
                // Ensures that the NotificationManager is not null (extra safety).
                notificationManager.createNotificationChannel(channel);
                // Creates the notification channel in the system.
            }
        }
    }
}

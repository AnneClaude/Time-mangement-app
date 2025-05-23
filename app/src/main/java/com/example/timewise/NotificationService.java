package com.example.timewise;

import static com.example.timewise.NotificationChannel.CHANNEL_ID;
// Imports the constant `CHANNEL_ID` from `NotificationChannel` for setting the notification channel.

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

/*
 * Notification channels give users more control over how they receive notifications.
 * Users can configure settings for each channel, such as importance levels and sound.
 */
public class NotificationService extends Service {
    Notification notification;
    public NotificationService() {
        // Default constructor for the service class.
    }

    @Override
    public IBinder onBind(Intent intent) {
        // This service does not allow binding, so it returns null.
        return null;
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Called when the service is started.

        // Create the notification channel (only for Android 8.0 and above).
        NotificationChannel.createNotificationChannel(this);

        // Build and display the notification.
        build_and_view_Notification(this, intent);

        // Start the service in the foreground to ensure it remains active.
        startForeground(1, notification);

        return START_STICKY;
        // Ensures the service restarts automatically if the system kills it.
    }

    private void build_and_view_Notification(Context context, Intent intent) {
        // Method to build and display the notification.

        String title = "Time Is Up!";//the title of the notification
        String message = "Click to open the application";//the message that will be written in the notification

        // Define an action for the notification. Clicking it opens `BottomNavMenuActivity`.
        Intent goInfo = new Intent(context, BottomNavMenuActivtiy.class);
        PendingIntent go = PendingIntent.getActivities(
                context,
                100,
                new Intent[]{goInfo},
                PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification using `NotificationCompat.Builder`.
        notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.timericon) // Icon to display in the notification bar.
                .setContentTitle(title) // Title of the notification.
                .setContentIntent(go) // Action to perform when clicked.
                .setContentText(message) // Message body of the notification.
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Priority for how the notification appears.
                .build(); // Combine all settings into a Notification object.

        // Check if the app has permission to send notifications.
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, notify the user.
            Toast.makeText(
                    context,
                    "Please change the settings in your device to allow us to send you notifications",
                    Toast.LENGTH_SHORT
            ).show();

            // TODO: Optionally, request missing permissions here.
            return;
        }

        // Get the system notification manager.
        NotificationManager nm = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

        // Display the notification. The first parameter (1) is the notification ID.
        nm.notify(1, notification);
    }
}

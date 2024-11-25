package com.example.projectfyp.Files;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // Handle Notification payload
        if (remoteMessage.getNotification() != null) {
            String message = remoteMessage.getNotification().getBody();
            sendNotification(message); // Method to show the notification
        }

        // Handle Data payload (if any)
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");

            // Custom handling of data (if needed)
            Log.d("FCM", "Data received: Title = " + title + ", Body = " + body);
            sendNotification(body);  // Handle your data here as well
        }
    }
    
    private void sendNotification(String messageBody) {
        String channelId = "announcement_channel";
        String channelName = "Announcement Notifications";

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // For Android Oreo and above, create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("New Announcement")
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Show the notification
        notificationManager.notify(0, notificationBuilder.build());
    }
}

package com.example.camaraderie.notifications;

import static com.example.camaraderie.main.Camaraderie.getContext;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.camaraderie.R;
import com.example.camaraderie.main.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingReceiver extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("Notification", "Refreshed Firebase token: " + token);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        if (message.getNotification() != null) {

            NotificationHelper.showNotification(
                    getApplicationContext(),
                    message.getNotification().getTitle(),
                    message.getNotification().getBody(),
                    (int) System.currentTimeMillis()  // generates unique id so it doesnt conflict with any other guy (we obviously cant go back in time)
            );
        }
    }
}

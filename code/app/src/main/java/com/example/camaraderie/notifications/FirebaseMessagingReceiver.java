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

/**
 * Service that listens for Firebase Cloud Messaging (FCM) events.
 * <p>
 * Handles:
 * <ul>
 *     <li>Receiving new FCM registration tokens.</li>
 *     <li>Receiving FCM messages and displaying notifications.</li>
 * </ul>
 */
public class FirebaseMessagingReceiver extends FirebaseMessagingService {

    /**
     * Called when the FCM registration token is refreshed.
     *
     * @param token the new FCM registration token
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("Notification", "Refreshed Firebase token: " + token);
    }

    /**
     * Called when an FCM message is received.
     * <p>
     * If the message contains a notification payload, this method will show
     * a system notification using {@link NotificationHelper}.
     *
     * @param message the received FCM message
     */
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

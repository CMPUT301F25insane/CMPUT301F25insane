package com.example.camaraderie.notifications;

import static com.example.camaraderie.main.Camaraderie.getContext;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.camaraderie.R;
import com.example.camaraderie.main.MainActivity;

/**
 * Helper class for creating and showing notifications.
 * <p>
 * Provides methods to display notifications with custom layouts or default styles.
 */
public class NotificationHelper {

    /**
     * Creates a custom RemoteViews design for a notification.
     *
     * @param title   the notification title
     * @param message the notification message
     * @return a {@link RemoteViews} object representing the custom notification layout
     */
    private static RemoteViews getCustomDesign(String title, String message) {
        RemoteViews remoteViews = new RemoteViews(getContext().getPackageName(), R.layout.notification_test_layout);

        remoteViews.setTextViewText(R.id.notificationTitle, title);
        remoteViews.setTextViewText(R.id.notificationBody, message);
        remoteViews.setImageViewResource(R.id.notificationImage, R.drawable.ic_launcher_foreground);
        return remoteViews;
    }

    /**
     * Displays a notification with the given title, message, and ID.
     * <p>
     * Opens {@link MainActivity} when the notification is tapped. Uses default layout
     * for simplicity; custom layouts can be enabled for supported Android versions.
     *
     * @param context the application context
     * @param title   the notification title
     * @param message the notification message
     * @param id      a unique ID for the notification
     */
    public static void showNotification(Context context, String title, String message, int id) {
        String channelId = "general";

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                id,
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })

                .setContentIntent(pendingIntent);

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getCustomDesign(title, message));
        }
        */
        //else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_launcher_foreground);
        //}

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(id, builder.build());
    }
}
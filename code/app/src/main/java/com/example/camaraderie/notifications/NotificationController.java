package com.example.camaraderie.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationController {

    private static final String CHANNEL_ID = "basic_notification_channel";
    private Context context;
    private com.example.notifications.NotificationView view;

    public NotificationController(Context context, com.example.notifications.NotificationView view) {
        this.context = context.getApplicationContext();
        this.view = view;
        createNotificationChannel();
    }

    public void sendNotification(Notification model) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(model.getTitle())
                .setContentText(model.getMessage())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(model.getId(), builder.build());

        view.onNotificationSent(model.getId());
    }

    public void cancelNotification(Notification model) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(model.getId());

        view.onNotificationCancelled(model.getId());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Basic Notifications";
            String description = "General Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}

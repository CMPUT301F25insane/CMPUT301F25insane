package com.example.camaraderie.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

/**
 * controller class for sending and cancelling notifications
 */
public class NotificationController {

    private static final String CHANNEL_ID = "basic_notification_channel";
    private Context context;
    private com.example.notifications.NotificationView view;

    /**
     * constructor for NotificationController
     * @param context current context
     * @param view the fragment for which to set this controller to
     */
    public NotificationController(Context context, com.example.notifications.NotificationView view) {
        this.context = context.getApplicationContext();
        this.view = view;
        createNotificationChannel();
    }

    /**
     * send notification
     * @param model model holding teh info for the notificaiton to be sent
     */
    public void sendNotification(NotificationData model) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(model.getTitle())
                .setContentText(model.getMessage())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(model.getId(), builder.build());

        // after notification has been sent
        view.onNotificationSent(model.getId());
    }

    /**
     * cancel notification
     * @param model notification data object holding the information of the notification to be cancelled
     */
    public void cancelNotification(NotificationData model) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(model.getId());

        // after notification has been cancelled
        view.onNotificationCancelled(model.getId());
    }

    /**
     * setup code for creating a basic notification channel
     */
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

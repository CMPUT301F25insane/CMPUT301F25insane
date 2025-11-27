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

    private String CHANNEL_ID;
    private Context context;
    private NotificationView view;

    /**
     * constructor for NotificationController
     * @param context current context
     * @param view the fragment for which to set this controller to
     */
    public NotificationController(Context context, NotificationView view) {
        this.context = context.getApplicationContext();
        this.view = view;
        //createNotificationChannel();
    }

    public void setChannelId(String id) {
        CHANNEL_ID = id;
    }

    public String getChannelId() {return this.CHANNEL_ID;}

    /**
     * send notification
     * @param model model holding teh info for the notificaiton to be sent
     */
    public void sendNotification(NotificationData model) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(model.getTitle())
                .setContentText(model.getMessage())

                // these are for expandable notifications for lines that are too big to fit on 1 line
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(model.getMessage()))


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
    public void createNotificationChannel(CharSequence name, String description) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CharSequence name = "Basic Notifications";
            //String description = "General Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    /*private void handleNotificationTap() {
        //Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)

                // automatically cancels notif when user taps it
                .setAutoCancel(true);
    }*/
}

package com.example.camaraderie.admin_screen.admin_notification_logs;

import android.util.Log;

import com.example.camaraderie.notifications.NotificationData;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Date;

/**
 * AdminNotifLogHelper is a utility class to help list the logged notifications
 */

public class AdminNotifLogHelper {

    /**
     * An interface to initialize the callback
     */

    public interface NotifHelperCallback {
        void onComplete(ArrayList<NotificationData> logs);
    }

    /**
     * This is method allows us to actually load the notification logs from database
     * @param logs
     * @param callback
     */

    public static void loadNotificationsFromLogs(ArrayList<DocumentReference> logs, NotifHelperCallback callback) {

        ArrayList<NotificationData> notifs = new ArrayList<>();
        if (logs.isEmpty()) {
            callback.onComplete(notifs);
            return;
        }

        for (DocumentReference ref : logs) {
            ref.get().addOnSuccessListener(
                doc -> {
                    NotificationData notif = doc.toObject(NotificationData.class);
                    if (notif != null) notifs.add(notif);

                    if (logs.size() == notifs.size()) {
                        System.out.println("ran callback");
                        callback.onComplete(notifs);
                    }
                }
            )
                    .addOnFailureListener(e -> {
                        Log.e("Admin Notification Helper", "Failed to load notification " + ref, e);

                        e.printStackTrace();
                    });
        }

    }
}

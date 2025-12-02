package com.example.camaraderie.main;

import static com.example.camaraderie.main.Camaraderie.getContext;
import static com.example.camaraderie.main.Camaraderie.getUser;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.camaraderie.notifications.NotificationData;
import com.example.camaraderie.notifications.NotificationHelper;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class loads the user and their data when you open the app
 */

public class LoadUser {

    /**
     * This interface initializes the array list of notifications for the user
     */

    public interface OnNotificationsLoaded {
        void onLoaded(ArrayList<NotificationData> notifications);
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    public LoadUser(DocumentReference ref) {
        this.docRef = ref;
    }

    /**
     * The build notifications method builds any notifications the user got when they didnt have the app open
     * @param pendingNotifs
     * @param callback
     */

    private void buildNotifications(ArrayList<DocumentReference> pendingNotifs, OnNotificationsLoaded callback) {

        ArrayList<NotificationData> notifications = new ArrayList<>();

        AtomicInteger size = new AtomicInteger(pendingNotifs.size());

        for (DocumentReference ref : pendingNotifs) {
            ref.get().addOnSuccessListener(snap -> {
                NotificationData notification = snap.toObject(NotificationData.class);
                if (notification != null) {

                    if (!notification.isSent()) {
                        notifications.add(notification);
                    }
                    else {
                        size.getAndDecrement();
                        getUser().getDocRef().update("pendingNotifications", FieldValue.arrayRemove(ref));
                    }
                }

                if (notifications.size() == size.get()) {
                    callback.onLoaded(notifications);
                }
            });
        }
    }

    /**
     * The check for notifications method actually checks for notifications the user may have received
     */

    private void checkForNotifications() {

        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    ArrayList<DocumentReference> pending = (ArrayList<DocumentReference>) documentSnapshot.get("pendingNotifications");
                    if (pending == null || pending.isEmpty()) {
                        return;
                    }

                    buildNotifications(pending,
    notifications -> {

                                for (NotificationData notif : notifications) {

//                                    FirebaseMessagingReceiver fcr = new FirebaseMessagingReceiver();
//                                    fcr.showNotification(notif.getTitle(), notif.getMessage());

                                    NotificationHelper.showNotification(
                                            getContext(),
                                            notif.getTitle(),
                                            notif.getMessage(),
                                            notif.getId()
                                    );

                                    docRef.update("pendingNotifications", FieldValue.arrayRemove(notif.getRef()))
                                            .addOnSuccessListener(v -> {
                                                Log.d("Load User", "Notification displayed and removed from ref pendingNotifications field: " + notif.getTitle());
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e("Load User", "Failed to remove notification form ref pendingNotifications field: " + notif.getTitle(), e);
                                            });
                                }

                            });

                });
    }


    public void loadAllData(@Nullable Runnable onComplete) {
        checkForNotifications();


        if (onComplete != null) {
            onComplete.run();
        }

    }
}

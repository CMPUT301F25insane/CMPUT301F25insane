package com.example.camaraderie.main;

import androidx.annotation.Nullable;

import com.example.camaraderie.notifications.FirebaseMessagingReceiver;
import com.example.camaraderie.notifications.NotificationData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firestore.v1.Write;

import java.util.ArrayList;
import java.util.Map;

public class LoadUser {

    public interface OnNotificationsLoaded {
        void onLoaded(ArrayList<NotificationData> notifications);
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    public LoadUser(DocumentReference ref) {
        this.docRef = ref;
    }

    private void buildNotifications(ArrayList<DocumentReference> pendingNotifs, OnNotificationsLoaded callback) {

        ArrayList<NotificationData> notifications = new ArrayList<>();

        for (DocumentReference ref : pendingNotifs) {
            ref.get().addOnSuccessListener(snap -> {
                NotificationData notification = snap.toObject(NotificationData.class);
                if (notification != null) {
                    notifications.add(notification);
                }

                if (notifications.size() == pendingNotifs.size()) {
                    callback.onLoaded(notifications);
                }
            });
        }
    }

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
                                    FirebaseMessagingReceiver fcr = new FirebaseMessagingReceiver();
                                    fcr.showNotification(notif.getTitle(), notif.getMessage());
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

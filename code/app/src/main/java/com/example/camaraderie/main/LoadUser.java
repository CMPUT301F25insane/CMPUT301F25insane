package com.example.camaraderie.main;

import static com.example.camaraderie.main.Camaraderie.getContext;
import static com.example.camaraderie.main.MainActivity.user;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.camaraderie.notifications.FirebaseMessagingReceiver;
import com.example.camaraderie.notifications.NotificationData;
import com.example.camaraderie.notifications.NotificationHelper;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
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

package com.example.camaraderie.my_events;

import android.util.Log;

import com.example.camaraderie.Event;
import com.example.camaraderie.notifications.NotificationData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Lottery runner class to handle running the lottery and sending notifications to entrants
 */
public class LotteryRunner {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * lottery system, runs while waitlist is nonempty and selectedEvents size is less than capacity.
     * updates database, updates UI
     */
    public static void runLottery(Event event) {

        WriteBatch batch = db.batch();
        Random r = new Random();

        ArrayList<DocumentReference> winners = new ArrayList<>();
        ArrayList<DocumentReference> losers = new ArrayList<>();

        int capacityRemaining =
                event.getCapacity()
                - event.getSelectedUsers().size()
                - event.getAcceptedUsers().size();

        ArrayList<DocumentReference> waitlist = new ArrayList<>(event.getWaitlist());
        ArrayList<DocumentReference> selected = new ArrayList<>(event.getSelectedUsers());

        while (capacityRemaining > 0 && !waitlist.isEmpty()) {

            int index = r.nextInt(waitlist.size());
            DocumentReference userRef = waitlist.remove(index);

            selected.add(userRef);
            winners.add(userRef);

            // User updates
            batch.update(userRef, "waitlistedEvents", FieldValue.arrayRemove(event.getEventDocRef()));
            batch.update(userRef, "selectedEvents", FieldValue.arrayUnion(event.getEventDocRef()));

            // Event updates
            batch.update(event.getEventDocRef(), "waitlist", FieldValue.arrayRemove(userRef));
            batch.update(event.getEventDocRef(), "selectedUsers", FieldValue.arrayUnion(userRef));

            capacityRemaining--;
        }

        losers.addAll(waitlist);

        batch.commit()
                .addOnSuccessListener(v -> {
                    Log.d("Lottery", "Lottery committed successfully");

                    String title = event.getEventName() + " Lottery";

                    for (DocumentReference user : winners) {
                        sendNotificationsToEntrant(event.getEventDocRef(), user, title,
                                "Congratulations! You have been invited to the event!");
                    }

                    for (DocumentReference user : losers) {
                        sendNotificationsToEntrant(event.getEventDocRef(), user, title,
                                "Unfortunately, you were not selected in this lottery cycle. Keep waiting, there is still a chance!");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Lottery", "Lottery batch failed", e);
                });
    }

    public static void sendNotificationsToEntrant(DocumentReference event, DocumentReference uref, String title, String body) {

        DocumentReference notifRef = db.collection("Notifications").document();

        NotificationData notification = new NotificationData(uref.getId(), title, body, notifRef, FieldValue.serverTimestamp());

        notifRef.set(notification).addOnSuccessListener(v -> {
            uref.update("pendingNotifications", FieldValue.arrayUnion(notifRef))
                    .addOnSuccessListener(
                            v1 -> {
                                event.update("notificationLogs", FieldValue.arrayUnion(notifRef));
                                Log.d("Lottery Notifications", "Notification added to entrant notif list");
                            }
                    )
                    .addOnFailureListener(e -> {

                        Log.e("Lottery Notifications", "Notification failed to be added to entrant", e);
                    });
        });


    }
}

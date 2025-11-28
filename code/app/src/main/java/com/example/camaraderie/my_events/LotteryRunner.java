package com.example.camaraderie.my_events;

import android.util.Log;

import com.example.camaraderie.Event;
import com.example.camaraderie.notifications.NotificationData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.lang.reflect.Field;
import java.util.Random;

public class LotteryRunner {

    private static FirebaseFirestore db;

    /**
     * lottery system, runs while waitlist is nonempty and selectedList size is less than capacity.
     * updates database, updates UI
     */
    public static void runLottery(Event event) {
        Random r = new Random();

        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        String title = event.getEventName() + " Lottery";

        while (event.getSelectedUsers().size() + event.getAcceptedUsers().size() < event.getCapacity() &&
                !event.getWaitlist().isEmpty()) {

            int index = r.nextInt(event.getWaitlist().size());
            DocumentReference userRef = event.getWaitlist().get(index);

            event.getWaitlist().remove(userRef);
            event.getSelectedUsers().add(userRef);

            // Update user document lists
            batch.update(userRef, "waitlistedEvents", FieldValue.arrayRemove(event.getEventDocRef()));
            batch.update(userRef, "selectedEvents", FieldValue.arrayUnion(event.getEventDocRef()));

            // update events list
            batch.update(event.getEventDocRef(), "waitlist", FieldValue.arrayRemove(userRef));
            batch.update(event.getEventDocRef(), "selectedUsers", FieldValue.arrayUnion(userRef));


            // send notification to chosen entrant
            String body;

            body = "Congratulations! You have been invited to the event!";
            sendNotificationsToEntrant(userRef, title, body);

        }

        event.updateDB(() -> {
            batch.commit().addOnSuccessListener(v -> Log.d("Firebase", "Lottery run for event: " + event.getEventId()))
                    .addOnFailureListener(e -> Log.e("Firebase", "Error running lottery for event: " + event.getEventId(), e));
        });

        if (!event.getWaitlist().isEmpty()) {
            for (DocumentReference ref : event.getWaitlist()) {
                String notSelectedBody;
                notSelectedBody = "Unfortunately, you were not selected in this lottery cycle. Keep waiting, there is still a chance!";
                sendNotificationsToEntrant(ref, title, notSelectedBody);
            }
        }

    }

    private static void sendNotificationsToEntrant(DocumentReference uref, String title, String body) {

        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }

        DocumentReference notifRef = db.collection("Notifications").document();
        NotificationData notification = new NotificationData(notifRef.hashCode(), title, body, notifRef);
        notifRef.set(notification).addOnSuccessListener(v -> {
            uref.update("pendingNotifications", FieldValue.arrayUnion(notifRef))
                    .addOnSuccessListener(
                            v1 -> {
                                Log.d("Lottery Notifications", "Notification added to entrant");
                            }
                    )
                    .addOnFailureListener(e -> {
                        Log.e("Lottery Notifications", "Notification failed to be added to entrant", e);
                    });
        });


    }
}

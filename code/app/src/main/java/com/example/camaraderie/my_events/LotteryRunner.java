package com.example.camaraderie.my_events;

import android.util.Log;

import com.example.camaraderie.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.lang.reflect.Field;
import java.util.Random;

public class LotteryRunner {

    /**
     * lottery system, runs while waitlist is nonempty and selectedList size is less than capacity.
     * updates database, updates UI
     */
    public static void runLottery(Event event) {
        Random r = new Random();

        WriteBatch batch = FirebaseFirestore.getInstance().batch();

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
        }

        event.updateDB(() -> {
            batch.commit().addOnSuccessListener(v -> Log.d("Firebase", "Lottery run for event: " + event.getEventId()))
                    .addOnFailureListener(e -> Log.e("Firebase", "Error running lottery for event: " + event.getEventId(), e));
        });
    }
}

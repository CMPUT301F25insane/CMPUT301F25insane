package com.example.camaraderie.utilStuff;

import android.util.Log;

import com.example.camaraderie.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class EventDeleter {

    public static void deleteEvent(Event event) {

        DocumentReference eventDocRef = event.getEventDocRef();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        WriteBatch batch = db.batch();
        ArrayList<DocumentReference> list = new ArrayList<>();

        // add all users to list
        list.addAll(event.getWaitlist());
        list.addAll(event.getSelectedUsers());
        list.addAll(event.getAcceptedUsers());
        list.addAll(event.getCancelledUsers());

        for (DocumentReference uref : list) {

            batch.update(uref, "waitlistedEvents", FieldValue.arrayRemove(eventDocRef));
            batch.update(uref, "selectedEvents", FieldValue.arrayRemove(eventDocRef));
            batch.update(uref, "acceptedEvents", FieldValue.arrayRemove(eventDocRef));
            batch.update(uref, "cancelledEvents", FieldValue.arrayRemove(eventDocRef));

        }

        batch.commit()
                .addOnSuccessListener(vv -> {
                    Log.d("Event Deleter", "Users removed from event's lists");
                    eventDocRef.delete()
                            .addOnSuccessListener(vvv -> {
                                Log.d("Event Deleter", "Deleted event docref");
                            })
                            .addOnFailureListener(ee -> {
                                Log.e("Event Deleter", "Failed to delete event docref", ee);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("Event Deleter", "Failed to remove users from list", e);
                });
    }
}

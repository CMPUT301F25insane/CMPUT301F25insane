package com.example.camaraderie.utilStuff;

import static com.example.camaraderie.main.Camaraderie.getUser;

import android.util.Log;

import com.example.camaraderie.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;


/**
 * Utility class for deleting events and cleaning up associated user references in Firestore.
 * <p>
 * When an event is deleted, this class ensures that:
 * <ul>
 *     <li>All users associated with the event (waitlist, selected, accepted, cancelled) have the event removed from their lists.</li>
 *     <li>The event is removed from the host's created events list.</li>
 *     <li>The event document is deleted from Firestore.</li>
 * </ul>
 */
public class EventDeleter {

    /**
     * Deletes an event and updates all associated user and host references in Firestore.
     * <p>
     * This method performs a batch update to remove the event from all relevant user lists,
     * and then deletes the event document. Logs success or failure for each operation.
     *
     * @param event the {@link Event} to delete
     */
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
            batch.update(uref, "eventHistory", FieldValue.arrayRemove(eventDocRef));  // we assume here that the user has this event in their history if they remained in the waitlist. ie, upon leaving the waitlist, we remove the event from their history
        }

        batch.update(event.getHostDocRef(), "userCreatedEvents", FieldValue.arrayRemove(eventDocRef));

        // if the users we're dealing with is ourself
        if (getUser().getUserCreatedEvents().contains(eventDocRef)) {
            getUser().removeCreatedEvent(eventDocRef);
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

package com.example.camaraderie.utilStuff;

import static com.example.camaraderie.utilStuff.EventDeleter.deleteEvent;

import android.util.Log;

import com.example.camaraderie.Event;
import com.example.camaraderie.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles the deletion of a {@link User} and all associated references in Firestore.
 * <p>
 * This includes:
 * <ul>
 *     <li>Deleting events created by the user.</li>
 *     <li>Removing the user from waitlists, accepted, selected, and cancelled event lists.</li>
 *     <li>Deleting the user's Firestore document.</li>
 * </ul>
 */
public class UserDeleter {

    private final User user;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Internal callback interface for asynchronous operations on user-created events.
     */
    private interface UserDeleterCallback {
        /**
         * Called when a batch of events has been processed.
         *
         * @param events the list of processed events
         */
        void callback(ArrayList<Event> events);
    }

    /**
     * Constructs a {@code UserDeleter} for the given user.
     *
     * @param user the {@link User} to delete
     */
    public UserDeleter(User user) {
        this.user = user;
    }

    /**
     * Deletes the user along with all associated data in Firestore.
     * <p>
     * The deletion process includes:
     * <ol>
     *     <li>Deleting all events created by the user.</li>
     *     <li>Removing the user from all other events' participant lists.</li>
     *     <li>Deleting the user's Firestore document.</li>
     * </ol>
     *
     * @param onComplete a {@link Runnable} that is executed once the deletion is complete
     */
    public void DeleteUser(Runnable onComplete) {

        // delete user created events
        deleteCreatedEvents(user.getUserCreatedEvents(),
            () -> {
                removeUserFromForeignLists(() -> deleteUserDocument(onComplete));

            });

    }

    /**
     * Removes the user from all foreign event lists (waitlist, selected, accepted, cancelled).
     * <p>
     * Uses a Firestore batch to update multiple event documents atomically.
     *
     * @param onComplete a {@link Runnable} to execute after all updates are committed
     */
    private void removeUserFromForeignLists(Runnable onComplete) {

        WriteBatch batch = db.batch();

        for (DocumentReference eventRef : user.getWaitlistedEvents()) {
            batch.update(eventRef, "waitlist", FieldValue.arrayRemove(user.getDocRef()));
            batch.update(eventRef, "userLocationArrayList", FieldValue.arrayRemove(user.getDocRef()));
        }
        for (DocumentReference eventRef : user.getSelectedEvents()) {
            batch.update(eventRef, "selectedUsers", FieldValue.arrayRemove(user.getDocRef()));
            batch.update(eventRef, "userLocationArrayList", FieldValue.arrayRemove(user.getDocRef()));
        }
        for (DocumentReference eventRef : user.getAcceptedEvents()) {
            batch.update(eventRef, "acceptedUsers", FieldValue.arrayRemove(user.getDocRef()));
            batch.update(eventRef, "userLocationArrayList", FieldValue.arrayRemove(user.getDocRef()));
        }
        for (DocumentReference eventRef : user.getCancelledEvents()) {
            batch.update(eventRef, "cancelledUsers", FieldValue.arrayRemove(user.getDocRef()));
            batch.update(eventRef, "userLocationArrayList", FieldValue.arrayRemove(user.getDocRef()));
        }

        batch.commit().addOnSuccessListener(v -> {
            Log.d("User Deleter", "User removed form foreign lists");
            onComplete.run();
        });

    }

    /**
     * Deletes the user's Firestore document.
     *
     * @param onComplete a {@link Runnable} to execute after the document is deleted or if deletion fails
     */
    private void deleteUserDocument(Runnable onComplete) {
        user.getDocRef().delete()
                .addOnSuccessListener(v -> onComplete.run())
                .addOnFailureListener(e -> {
                    Log.e("UserDeleter", "Failed to delete user document", e);
                    onComplete.run();
                });
    }

    /**
     * Recursively deletes all events created by the user.
     *
     * @param refs       a list of Firestore {@link DocumentReference} objects representing events to delete
     * @param onComplete a {@link Runnable} to execute once all events are processed
     */
    private void deleteCreatedEvents(ArrayList<DocumentReference> refs, Runnable onComplete) {

        if (refs.isEmpty()) {
            onComplete.run();
            return;
        }

        DocumentReference ref = refs.remove(0);

        ref.get().addOnSuccessListener(doc -> {
            Event event = doc.toObject(Event.class);
            if (event != null) deleteEvent(event);
            else Log.e("UserDeleter", "event is null. Moving on...");

            deleteCreatedEvents(refs, onComplete);

        })
            .addOnFailureListener(e -> {
                Log.e("User Deleter", "Failed to get event", e);
                deleteCreatedEvents(refs, onComplete);
            });
    }
}

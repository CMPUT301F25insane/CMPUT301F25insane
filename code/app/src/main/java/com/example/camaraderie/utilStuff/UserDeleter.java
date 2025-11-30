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

public class UserDeleter {

    private final User user;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private interface UserDeleterCallback {
        void callback(ArrayList<Event> events);
    }

    public UserDeleter(User user) {
        this.user = user;
    }

    public void DeleteUser(Runnable onComplete) {

        // delete user created events
        deleteCreatedEvents(user.getUserCreatedEvents(),
            () -> {
                removeUserFromForeignLists(() -> deleteUserDocument(onComplete));

            });

    }

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

    private void deleteUserDocument(Runnable onComplete) {
        user.getDocRef().delete()
                .addOnSuccessListener(v -> onComplete.run())
                .addOnFailureListener(e -> {
                    Log.e("UserDeleter", "Failed to delete user document", e);
                    onComplete.run();
                });
    }

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

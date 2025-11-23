package com.example.camaraderie.utilStuff;

import com.example.camaraderie.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDeleter {

    private interface HandleDeleteCallback {
        void onListLoaded(ArrayList<DocumentReference> refs);
    }

    private User user;
    private ArrayList<DocumentReference> createdEventWaitlists = new ArrayList<>();
    private ArrayList<DocumentReference> createdEventSelectedLists = new ArrayList<>();
    private ArrayList<DocumentReference> createdEventAcceptedLists = new ArrayList<>();
    private ArrayList<DocumentReference> createdEventCancelledLists = new ArrayList<>();
    private ArrayList<DocumentReference> createdEvents = new ArrayList<>();

    private FirebaseFirestore db;

    public UserDeleter(User user) {
        this.user = user;

        createdEvents = user.getUserCreatedEvents();
        db = FirebaseFirestore.getInstance();
    }

    public void DeleteUser(Runnable onComplete) {

        // all event refs removed from associated users (theres a better way to do this but its fine for now)
        updateUsersLists(() -> {
            WriteBatch batch = db.batch();

            for (DocumentReference eventRef : createdEvents) {
                batch.delete(eventRef);
            }

            // all events deleted
            batch.commit().addOnSuccessListener(v -> {

                // deleted user (could put this in the batch but im too paranoid)
                user.getDocRef().delete().addOnSuccessListener(v1 -> {
                    onComplete.run();
                });

            });

        });
    }

    private void updateUsersLists(Runnable onComplete) {
        deserializeEventLists(() -> {

            // at this point, all docref lists exist for all events.
            WriteBatch batch = db.batch();
            for (DocumentReference userRef : createdEventWaitlists) {
                for (DocumentReference createdEventRef : createdEvents) {
                    batch.update(userRef, "waitlist", FieldValue.arrayRemove(createdEventRef));
                }
            }

            for (DocumentReference userRef : createdEventSelectedLists) {
                for (DocumentReference createdEventRef : createdEvents) {
                    batch.update(userRef, "selectedList", FieldValue.arrayRemove(createdEventRef));
                }
            }

            for (DocumentReference userRef : createdEventAcceptedLists) {
                for (DocumentReference createdEventRef : createdEvents) {
                    batch.update(userRef, "acceptedList", FieldValue.arrayRemove(createdEventRef));
                }
            }

            for (DocumentReference userRef : createdEventCancelledLists) {
                for (DocumentReference createdEventRef : createdEvents) {
                    batch.update(userRef, "cancelledList", FieldValue.arrayRemove(createdEventRef));
                }
            }

            batch.commit()
                .addOnSuccessListener(
                    v -> {
                        onComplete.run();
                    }
                );

        });
    }

    private void deserializeEventLists(Runnable onComplete) {
        loadList("waitlist", waitlist -> {

            createdEventWaitlists = waitlist;
            loadList("selectedList", selectedList -> {

                createdEventSelectedLists = selectedList;
                loadList("acceptedList", acceptedList -> {

                    createdEventAcceptedLists = acceptedList;
                    loadList("cancelledList", cancelledList -> {

                        createdEventCancelledLists = cancelledList;
                        onComplete.run();
                    });
                });
            });
        });
    }

    private void loadList(String key, HandleDeleteCallback onComplete) {

        ArrayList<DocumentReference> allLists = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        int size = createdEvents.size();
        for (DocumentReference ref : createdEvents) {
            ref.get()
                .addOnSuccessListener(
                    snapshot -> {
                        ArrayList<DocumentReference> list = (ArrayList<DocumentReference>) snapshot.get(key); // inshaallah this works
                        if (list != null) {
                            allLists.addAll(list);
                            count.getAndIncrement();
                        }
                    }
                );
        }

        // as long as we have successfully ran through all events
        if (count.get() >= size) {
            onComplete.onListLoaded(allLists);
        }
    }
}

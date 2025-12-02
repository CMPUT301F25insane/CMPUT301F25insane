package com.example.camaraderie.utilStuff;//

import android.util.Log;

import com.example.camaraderie.Event;
import com.example.camaraderie.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * util class for convenience functions
 * @author Fecici
 */
public class Util {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference eventsRef = db.collection("Events");
    private static final CollectionReference usersRef = db.collection("Users");

    /**
     * Clears all documents in both Events and Users, then seeds DB with dummy data.
     * @param onComplete Calls onComplete when finished.
     */
    public static void clearDBAndSeed(Runnable onComplete) {
        clearEvents(() -> clearUsers(() -> addDummyEvents(onComplete)));
    }

    /**
     * Clears the Events collection, then triggers callback.
     * @param onDone a lambda function defined by the caller for when the function finishes
     */
    private static void clearEvents(Runnable onDone) {
        eventsRef.get().addOnSuccessListener(snapshot -> {
            WriteBatch batch = db.batch();
            for (DocumentSnapshot doc : snapshot) batch.delete(doc.getReference());

            batch.commit()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("DB", "Events cleared");
                        onDone.run();
                    })
                    .addOnFailureListener(e -> Log.e("DB", "Failed to clear Events", e));
        });
    }

    /**
     * Clears the Users collection, then triggers callback.
     * @param onDone the callback function for when the method finishes
     */
    private static void clearUsers(Runnable onDone) {
        usersRef.get().addOnSuccessListener(snapshot -> {
            WriteBatch batch = db.batch();
            for (DocumentSnapshot doc : snapshot) batch.delete(doc.getReference());

            batch.commit()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("DB", "Users cleared");
                        onDone.run();
                    })
                    .addOnFailureListener(e -> Log.e("DB", "Failed to clear Users", e));
        });
    }

    /**
     * Seeds the database with linked Users and Events.
     * @param onDone callback function defined by caller as lambda function
     */
    private static void addDummyEvents(Runnable onDone) {
        WriteBatch batch = db.batch();

        for (int i = 0; i < 10; i++) {
            DocumentReference userDoc = usersRef.document();
            DocumentReference eventDoc = eventsRef.document();

            User newUser = new User(
                    "Bob Marley " + i,
                    "555-000" + i,
                    "email" + i + "@mail.com",
                    "address " + i,
                    userDoc.getId(),
                    null,
                    userDoc
            );

            int l = new Random().nextInt(5);

            int ll = new int[] {-1, 1, 3, 5, 7}[l];  // waitlist limit

            Event newEvent = new Event(
                    "Event " + i,
                    "Location " + i,
                    new Date(),
                    "Description " + i,
                    new Date(),
                    "05:00",
                    "05:00",
                    5,
                    ll,
                    userDoc,
                    eventDoc,
                    null,
                    false
            );

            newUser.addCreatedEvent(eventDoc);

            batch.set(eventDoc, newEvent);
            batch.set(userDoc, newUser);
        }

        batch.commit()
                .addOnSuccessListener(aVoid -> {
                    Log.d("DB", "Dummy users and events added");
                    onDone.run();
                })
                .addOnFailureListener(e -> Log.e("DB", "Failed to seed dummy data", e));
    }

    /**
     * Sets admin flag on a user.
     * @param userRef user to set the admin property of
     * @param isAdmin bool to set isAdmin to
     */
    public static void setUserAsAdmin(DocumentReference userRef, boolean isAdmin) {
        userRef.update("admin", isAdmin)
                .addOnSuccessListener(aVoid ->
                        Log.d("DB", "Admin flag updated: " + userRef.getId()))
                .addOnFailureListener(e ->
                        Log.e("DB", "Failed to set admin flag", e));
    }


}

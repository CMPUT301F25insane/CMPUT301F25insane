package com.example.camaraderie.utilStuff;//

import android.util.Log;

import com.example.camaraderie.Event;
import com.example.camaraderie.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;

public class Util {
    public static void clearAndAddDummyEvents() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventsRef = db.collection("Events");
        CollectionReference usersRef = db.collection("Users");

        // TODO: put the real delete functions here, these dont work lol
        eventsRef.document().delete();
        usersRef.document().delete();

        for (int i = 0; i < 10; i++) {

            User user = new User(
                    "firstname" + i,
                    "phoneNumber" + i,
                    "email" + i + "@gmail.com",
                    "address" + i,
                    "usrId" + i,
                    null        // this just wont exist lol
            );


            usersRef.add(user);



            Event event = new Event(
                    "event" + i,
                    "location" + i,
                    new Date(),
                    "desc" + i,
                    new Date(),
                    "time" + i,
                    i,
                    usersRef.document("usrId" + i),
                    "id" + i
            );

            eventsRef.add(event);

            System.out.println("added user and event " + i);
        }
    }

    public static void clearUsersCollection() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .get()
                .addOnSuccessListener(snapshot -> {
                    WriteBatch batch = db.batch();
                    for (DocumentSnapshot doc : snapshot) {
                        batch.delete(doc.getReference());
                    }
                    batch.commit()
                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "All Users deleted"))
                            .addOnFailureListener(e -> Log.e("Firestore", "Error deleting Users", e));
                });
    }

    public static void clearEventsCollection() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Events")
                .get()
                .addOnSuccessListener(snapshot -> {
                    WriteBatch batch = db.batch();
                    for (DocumentSnapshot doc : snapshot) {
                        batch.delete(doc.getReference());
                    }
                    batch.commit()
                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "All Events deleted"))
                            .addOnFailureListener(e -> Log.e("Firestore", "Error deleting Events", e));
                });


    }

    public static void clearDB() {
        clearEventsCollection();
        clearUsersCollection();
    }

    public static void setUserAsAdmin(DocumentReference user, boolean isAdmin) {
        String msg = "isAdmin for User " + user + " set to " + isAdmin;
        user.update("isAdmin", isAdmin)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", msg))
                .addOnFailureListener(e -> Log.e("Firestore", "Error setting user" + user + " to " + isAdmin));
    }
}

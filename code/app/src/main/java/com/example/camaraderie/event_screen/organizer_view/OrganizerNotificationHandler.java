package com.example.camaraderie.event_screen.organizer_view;

import android.util.Log;

import com.example.camaraderie.notifications.NotificationData;
import com.google.firebase.FirebaseKt;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handles the notification building and sending to firebase
 * It is used as a helper function for the other notification classes
 */

public class OrganizerNotificationHandler {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String title;  // not yet used, will do later
    private String body;
    private DocumentReference eventId;

    /**
     * This is just a constrctor that sets the body, title and event id of the event notification
     * @param title
     * @param body
     * @param eventId
     */

    public OrganizerNotificationHandler(String title, String body, DocumentReference eventId) {
        this.body = body;
        this.title = title;
        this.eventId = eventId;
    }

    /**
     * This method sends the notification to firebase, this method sets up the batch and sets the
     * notification data and updates the batch, on failure it logs the error
     * @param field
     * @param onComplete
     * @param onFuckUp
     */

    public void sendNotificationToFirebase(String field, Runnable onComplete, Runnable onFuckUp) {



        WriteBatch batch = db.batch();


        // users in this specific list will have an update upon relaunching the app or refreshing notifications
        eventId.get()
                .addOnSuccessListener(snap -> {
                    ArrayList<DocumentReference> refs = (ArrayList<DocumentReference>) snap.get(field);
                    if (refs != null) {
                        for (DocumentReference ref : refs) {

                            DocumentReference notifRef = db.collection("Notifications").document();


                            NotificationData notification = new NotificationData(ref.getId(), title, body, notifRef, FieldValue.serverTimestamp());
                            batch.set(notifRef, notification);
                            batch.update(notifRef, "timestamp", FieldValue.serverTimestamp());

                            batch.update(eventId, "notificationLogs", FieldValue.arrayUnion(notifRef));

                            batch.update(ref, "pendingNotifications", FieldValue.arrayUnion(notifRef));

                        }
                    }
                    //else { throw new RuntimeException("fuck you");}

                    batch.commit()
                            .addOnSuccessListener( v-> {
                                Log.d("Organizer notifications", "sendNotificationToFirebase: " + body);
                                if (onComplete != null) {
                                    onComplete.run();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Organizer notifications", "we fucked up", e);
                                if (onFuckUp != null) {
                                    onFuckUp.run();
                                }
                            });

                });


    }
}

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
import java.util.HashMap;
import java.util.Map;

public class OrganizerNotificationHandler {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String title;  // not yet used, will do later
    private String body;
    private DocumentReference eventId;

    public OrganizerNotificationHandler(String title, String body, DocumentReference eventId) {
        this.body = body;
        this.title = title;
        this.eventId = eventId;
    }

    public void sendNotificationToFirebase(String field, Runnable onComplete, Runnable onFuckUp) {

        DocumentReference notifRef = db.collection("Notifications").document();
        NotificationData notification = new NotificationData(notifRef.hashCode(), title, body, notifRef);

        WriteBatch batch = db.batch();
        batch.set(notifRef, notification);

        // users in this specific list will have an update upon relaunching the app or refreshing notifications
        eventId.get()
                .addOnSuccessListener(snap -> {
                    ArrayList<DocumentReference> refs = (ArrayList<DocumentReference>) snap.get(field);
                    if (refs != null) {
                        for (DocumentReference ref : refs) {
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

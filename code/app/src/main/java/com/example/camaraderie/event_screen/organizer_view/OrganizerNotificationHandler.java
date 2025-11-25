package com.example.camaraderie.event_screen.organizer_view;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OrganizerNotificationHandler {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String title;
    private String body;
    private DocumentReference eventId;

    public OrganizerNotificationHandler(String title, String body, DocumentReference eventId) {
        this.body = body;
        this.title = title;
        this.eventId = eventId;
    }

    public void sendNotificationToFirebase(Runnable onComplete, Runnable onFuckUp) {

        Map<String, Object> data = new HashMap<>();
        data.put("message", body);
        data.put("timestamp", FieldValue.serverTimestamp());

        eventId.collection("announcements").add(data)
                .addOnSuccessListener(v -> {

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

    }

}

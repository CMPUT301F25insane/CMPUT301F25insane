package com.example.camaraderie.notifications;

import com.google.firebase.firestore.DocumentReference;

/**
 * This class represents a basic notification model
 */
public class NotificationData {

    private String userId;
    private String message;
    private String title;
    private DocumentReference ref;
    private int id; // local notification ID

    // Empty constructor for Firestore
    public NotificationData() {}

    public NotificationData(String userId, String title, String message, DocumentReference ref) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.ref = ref;
        this.id = ref.getId().hashCode();  // local usage only
    }

    public String getUserId() { return userId; }
    public String getMessage() { return message; }
    public String getTitle() { return title; }
    public DocumentReference getRef() { return ref; }
    public int getId() { return id; }

    public void setUserId(String userId) { this.userId = userId; }
    public void setMessage(String message) { this.message = message; }
    public void setTitle(String title) { this.title = title; }
    public void setRef(DocumentReference ref) { this.ref = ref; }
    public void setId(int id) { this.id = id; }
}
package com.example.camaraderie.notifications;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.Date;

/**
 * This class represents a basic notification model
 * @author Fecici
 */
public class NotificationData {

    private String userId;
    private String message;
    private String title;
    private DocumentReference ref;
    private boolean sent = false;
    private Date timestamp;
    private int id; // local notification ID

    // Empty constructor for Firestore

    /**
     * Empty NotificationData constructor for firebase
     */
    public NotificationData() {}

    /**
     * NotificationData Constructor
     * @param userId notification recipient's user id
     * @param title notification title
     * @param message notification message
     * @param ref notification document refence in firestore
     * @param timestamp notification server timestamp
     */
    public NotificationData(String userId, String title, String message, DocumentReference ref, FieldValue timestamp) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.ref = ref;
        this.id = ref.getId().hashCode();  // local usage only
        // db writes the timestamp upon creating a notif
    }

    /**
     * gets user id
     * @return user id
     */
    public String getUserId() { return userId; }

    /**
     * gets notification message
     * @return notification message
     */
    public String getMessage() { return message; }

    /**
     * gets notification title
     * @return notification title
     */
    public String getTitle() { return title; }

    /**
     * gets notification doc ref
     * @return Document Reference of notification in database
     */
    public DocumentReference getRef() { return ref; }

    /**
     * gets notification id
     * @return notification id
     */
    public int getId() { return id; }

    /**
     * sets user id for notification recipient
     * @param userId recipient id
     */
    public void setUserId(String userId) { this.userId = userId; }

    /**
     * sets notification title
     * @param message new notification title
     */
    public void setMessage(String message) { this.message = message; }

    /**
     * sets notification title
     * @param title new notification title
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * sets notification document reference
     * @param ref new document reference for notification
     */
    public void setRef(DocumentReference ref) { this.ref = ref; }

    /**
     * sets notification id
     * @param id new notification id to set
     */
    public void setId(int id) { this.id = id; }

    /**
     * gets notification firebase server timestamp
     * @return firebase timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * sets notification firebase server timestamp
     * @param timestamp new timestamp
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * checks if notification has been sent by the server.
     * this value is updated by the firebase node.js script
     * when sending the notification. if the notification is not sent,
     * it remains in the user's pending notifications list
     * @return boolean true if the notification has been sent
     */
    public boolean isSent() {
        return sent;
    }

    /**
     * sets whether or not the notification has been sent
     * @param sent new notification sent value
     */
    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
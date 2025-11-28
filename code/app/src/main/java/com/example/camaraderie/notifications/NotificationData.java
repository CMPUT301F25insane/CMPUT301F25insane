package com.example.camaraderie.notifications;

import com.google.firebase.firestore.DocumentReference;

/**
 * This class represents a basic notification model
 */
public class NotificationData {

    private int id;
    private String message;
    private String title;
    private DocumentReference ref;

    /**
     * empty constructor for notification, for firebase
     */
    public NotificationData() {}

    /**
     * Constructor for Notification
     * @param title notification title or tag
     * @param message notification message
     * @param ref notification document reference
     */
    public NotificationData(String title, String message, DocumentReference ref) {
        this.id = ref.getId().hashCode();
        this.message = message;
        this.title = title;
        this.ref = ref;
    }

    /**
     *
     * @return returns noticication id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return returns notification message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @return returns notification title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param id sets notifciation id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param message sets notification message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @param title sets notification title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public DocumentReference getRef() {
        return ref;
    }

    public void setRef(DocumentReference ref) {
        this.ref = ref;
    }


}

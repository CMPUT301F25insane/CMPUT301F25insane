package com.example.camaraderie.notifications;

/**
 * This class represents a basic notification model
 */
public class NotificationData {

    private int id;
    private String message;
    private String title;

    /**
     * empty constructor for notification, for firebase
     */
    public NotificationData() {}

    /**
     * Constructor for Notification
     * @param id unique id for notificatio
     * @param title notification title or tag
     * @param message notification message
     */
    public NotificationData(int id, String title, String message) {
        this.id = id;
        this.message = message;
        this.title = title;
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
}

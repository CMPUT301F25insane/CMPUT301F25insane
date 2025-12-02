package com.example.camaraderie.notifications;

/**
 * Interface for handling notification events.
 * <p>
 * Classes implementing this interface can respond to notifications being sent or cancelled.
 */
public interface NotificationView {
    /**
     * Called when a notification has been successfully sent.
     *
     * @param notificationId the unique ID of the sent notification
     */
    void onNotificationSent(int notificationId);

    /**
     * Called when a notification has been cancelled.
     *
     * @param notificationId the unique ID of the cancelled notification
     */
    void onNotificationCancelled(int notificationId);
}
package com.example.notifications;

public interface NotificationView {
    void onNotificationSent(int notificationId);
    void onNotificationCancelled(int notificationId);
}
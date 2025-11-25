package com.example.camaraderie.notifications;

public interface NotificationView {
    void onNotificationSent(int notificationId);
    void onNotificationCancelled(int notificationId);
}
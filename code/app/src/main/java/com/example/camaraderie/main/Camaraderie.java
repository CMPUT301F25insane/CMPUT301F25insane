package com.example.camaraderie.main;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.example.camaraderie.User;
import com.google.firebase.BuildConfig;
import com.google.firebase.FirebaseApp;

import org.maplibre.android.MapLibre;

import javax.inject.Singleton;

/**
 * This was needed when Hilt was implemented. Now that Hilt is removed, this is not needed.
 * But, removing this will destroy everything.
 * Now this is the thing necessary to get a global instance of a map
 * "coconut.jpg"
 */
@Singleton
public class Camaraderie extends Application{

    private static Camaraderie instance;
    private static UserRepository userRepo = new UserRepository();

    public static void setUserRepo(UserRepository userRepo) {
        Camaraderie.userRepo = userRepo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Initialize MapLibre
        MapLibre.getInstance(this);

        FirebaseApp.initializeApp(this);

        createNotificationChannels();

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .build()
            );
        }

    }

    public static User getUser() {
        return userRepo.getUser();
    }

    public static void setUser(User user) {
        userRepo.setUser(user);
    }

    public static Camaraderie getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public void createNotificationChannels() {

        createNotificationChannel
                ("general",
                    "General Notifications",
                "Notifications for general use",
                            NotificationManager.IMPORTANCE_DEFAULT);

        createNotificationChannel
                ("lists",
                   "User list notifications",
                "Notifications for user lists by organizers",
                            NotificationManager.IMPORTANCE_DEFAULT);

        createNotificationChannel
                ("admin",
                    "Admin notifications",
                "Notifications sent by the admin (deleting, etc)",
                            NotificationManager.IMPORTANCE_HIGH);
    }

    public void createNotificationChannel(String channel_id, CharSequence name, String description, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager manager = (NotificationManager) this.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}

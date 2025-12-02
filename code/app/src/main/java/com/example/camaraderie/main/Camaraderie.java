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

    /**
     * sets the user repository
     * @param userRepo UserRepository singleton instance
     */
    public static void setUserRepo(UserRepository userRepo) {
        Camaraderie.userRepo = userRepo;
    }

    /**
     * initializes application apis, database, and notification channels
     */
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

    /**
     * returns user repository user
     * @return User in user repository
     */
    public static User getUser() {
        return userRepo.getUser();
    }

    /**
     * sets user repo user
     * @param user new user to be set
     */
    public static void setUser(User user) {
        userRepo.setUser(user);
    }

    /**
     * defines a singleton pattern for the application class
     * @return instance of application
     */
    public static Camaraderie getInstance() {
        return instance;
    }

    /**
     * defines a static method to get context from anywhere in the app
     * @return application context
     */
    public static Context getContext() {
        return instance.getApplicationContext();
    }

    /**
     * creates all notification channels for app
     */
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

    /**
     * creates a specific notification channel for the app
     * @param channel_id channel id
     * @param name channel name
     * @param description channel description
     * @param importance channel importance
     */
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

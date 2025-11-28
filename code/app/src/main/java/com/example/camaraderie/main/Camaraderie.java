package com.example.camaraderie.main;

import android.app.Application;
import android.content.Context;

import org.maplibre.android.MapLibre;

/**
 * This was needed when Hilt was implemented. Now that Hilt is removed, this is not needed.
 * But, removing this will destroy everything.
 * Now this is the thing necessary to get a global instance of a map
 * "coconut.jpg"
 */
public class Camaraderie extends Application{

    private static Camaraderie instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Initialize MapLibre
        MapLibre.getInstance(this);
    }

    public static Camaraderie getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}

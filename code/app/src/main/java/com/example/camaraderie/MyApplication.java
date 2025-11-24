package com.example.camaraderie;

import android.app.Application;

import org.maplibre.android.MapLibre;

/**
 * This was needed when Hilt was implemented. Now that Hilt is removed, this is not needed.
 * But, removing this will destroy everything.
 * Now this is the thing necessary to get a global instance of a map
 * "coconut.jpg"
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize MapLibre
        MapLibre.getInstance(this);
    }
}

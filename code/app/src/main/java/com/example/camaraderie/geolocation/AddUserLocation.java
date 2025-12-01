package com.example.camaraderie.geolocation;

import static com.example.camaraderie.main.Camaraderie.getContext;
import static com.example.camaraderie.main.MainActivity.user;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.camaraderie.Event;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.UUID;


/**
 * Utility class for adding a user's current location to an Event object
 * and storing it in Firebase Firestore.
 *
 * <p>The class handles location permissions, geolocation settings, Firestore updates,
 * and provides a callback once the location is successfully added.
 */
public class AddUserLocation {
    public static void addLocation(Event event, Runnable runnable){
        if (event == null) {
            Toast.makeText(getContext(), "Event not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * Adds the current user's location to the event if geolocation is enabled.
         *
         * <p>The method performs the following steps:
         * <ul>
         *     <li>Checks if both the user and event have geolocation enabled</li>
         *     <li>Verifies location permissions</li>
         *     <li>Fetches the last known location using FusedLocationProviderClient</li>
         *     <li>Stores the location in Firestore under "userLocationArrayList"</li>
         *     <li>Updates the local Event object's cached location list</li>
         *     <li>Executes the provided Runnable callback after successful update</li>
         * </ul>
         *
         * <p>If geolocation is disabled or location permissions are missing, a Toast is shown
         * and the callback may not be executed.
         *
         * @param event    The event to which the user's location should be added.
         * @param runnable Optional callback to execute after the location is successfully added.
         */
        if (user.isGeoEnabled() && event.isGeoEnabled()) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());


            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getContext(), "Could not get location", Toast.LENGTH_SHORT).show();
                return;
            }

            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location == null){
                        Toast.makeText(getContext(),
                                "Could not get location", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("entryId", UUID.randomUUID().toString());
                    map.put("userID", user.getUserId());
                    map.put("latitude", location.getLatitude());
                    map.put("longitude", location.getLongitude());

                    event.getEventDocRef().update("userLocationArrayList", FieldValue.arrayUnion(map))
                            .addOnSuccessListener(aVoid -> {
                                event.addUserLocationArrayList(map);

                                if (runnable != null) runnable.run();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to save location", Toast.LENGTH_SHORT).show();
                            });
                }
            });
        } else if (!user.isGeoEnabled() && event.isGeoEnabled()) {
            Toast.makeText(getContext(), "Please enable location to join this event", Toast.LENGTH_SHORT).show();
        } else {
            if (runnable != null) {
                runnable.run();
            }
        }
    }
}

package com.example.camaraderie.geolocation;

import static com.example.camaraderie.main.Camaraderie.getContext;
import static com.example.camaraderie.main.MainActivity.user;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.eap.EapSessionConfig;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.UUID;

public class AddUserLocation {
    public static void addLocation(Event event, Runnable runnable){
        if (event == null) {
            Toast.makeText(getContext(), "Event not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }

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
                    }

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("entryId", UUID.randomUUID().toString());
                    map.put("userID", user.getUserId());
                    map.put("latitude", location.getLatitude());
                    map.put("longitude", location.getLongitude());

                    event.getEventDocRef().update("locationArrayList", FieldValue.arrayUnion(map))
                            .addOnSuccessListener(aVoid -> {
                                // also update locally so the app reflects it
                                event.addLocationArrayList(map);

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

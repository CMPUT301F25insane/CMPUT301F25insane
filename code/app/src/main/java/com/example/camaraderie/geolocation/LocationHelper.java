package com.example.camaraderie.geolocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.camaraderie.Event;
import com.example.camaraderie.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;

public class LocationHelper {

    public static void addUserGeoData(
            Fragment fragment,
            Event event,
            User user,
            Runnable onComplete
    ) {

        // Check permissions
        if (ContextCompat.checkSelfPermission(fragment.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(fragment.getContext(),
                    "Location permission required", Toast.LENGTH_SHORT).show();
            return;
        }

        FusedLocationProviderClient fused =
                LocationServices.getFusedLocationProviderClient(fragment.requireContext());

        fused.getLastLocation().addOnSuccessListener(location -> {

            if (location == null) {
                Toast.makeText(fragment.getContext(),
                        "Could not get location", Toast.LENGTH_SHORT).show();
                return;
            }

            // Build location object
            HashMap<String, Object> map = new HashMap<>();
            map.put("timestamp", System.currentTimeMillis());
            map.put("userID", user.getUserId());
            map.put("latitude", location.getLatitude());
            map.put("longitude", location.getLongitude());

            // Add it to the event
            event.addLocationArrayList(map);

            // Save in Firestore
            event.updateDB(() -> {
                Toast.makeText(fragment.getContext(),
                        "Location saved", Toast.LENGTH_SHORT).show();

                if (onComplete != null) onComplete.run();
            });
        });
    }
}

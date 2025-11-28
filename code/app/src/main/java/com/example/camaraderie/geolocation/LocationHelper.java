package com.example.camaraderie.geolocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class LocationHelper {
    public interface LocationCallback {
        void onLocationReceived(double latitude, double longitude); // fixed spelling
    }

    public static void getUserLocation(Fragment fragment, LocationCallback callback){
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(fragment.requireActivity());

        if (ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // You could request permission here if needed, or require it beforehand
            Toast.makeText(fragment.getContext(), "Location permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            callback.onLocationReceived(location.getLatitude(), location.getLongitude());
                        } else {
                            Toast.makeText(fragment.getContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

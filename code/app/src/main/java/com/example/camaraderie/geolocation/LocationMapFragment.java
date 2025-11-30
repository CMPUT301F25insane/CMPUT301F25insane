package com.example.camaraderie.geolocation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.camaraderie.Event;
import com.example.camaraderie.databinding.FragmentLocationMapBinding;

import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.annotations.MarkerOptions;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.geometry.LatLngBounds;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.OnMapReadyCallback;
import org.maplibre.android.maps.Style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationMapFragment extends Fragment {
    private FragmentLocationMapBinding binding;
    private MapLibreMap map;
    private String eventId;
    private Event event;
    ArrayList<HashMap<String, Object>> userLocations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack()
        );

        binding.mapView.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            userLocations = (ArrayList<HashMap<String, Object>>) args.getSerializable("userLocations");

            if (userLocations != null) {
                Log.d("MapFragment", "Received locations: " + userLocations.size());
                // Now you can loop through them and display markers, etc.
            }
        }

        binding.mapView.getMapAsync(mapLibreMap -> {
            map = mapLibreMap;

            String styleUrl = "https://basemaps.cartocdn.com/gl/positron-gl-style/style.json";

            map.setStyle(new Style.Builder().fromUri(styleUrl), style -> {
                showMarkers();
                //showTestMarkers2();
            });
        });
    }

    private void showMarkers() {
        if (map == null || userLocations == null || userLocations.isEmpty()) {
            return;
        }

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        StringBuilder text = new StringBuilder();

        for (HashMap<String, Object> coordinates : userLocations) {

            double latitude = ((Double) coordinates.get("latitude"));
            double longitude = ((Double) coordinates.get("longitude"));
            String userId = coordinates.get("userID").toString();

            LatLng point = new LatLng(latitude, longitude);

            // Add marker
            map.addMarker(new MarkerOptions()
                    .position(point)
                    .title(userId));

            // Add to bounds
            bounds.include(point);

            // Add to TextView log
            text.append("User: ").append(userId)
                    .append(" | Lat: ").append(latitude)
                    .append(" | Lng: ").append(longitude)
                    .append("\n");
        }

        // Print all coordinates to TextView
        binding.coordsTextView.setText(text.toString());

        // Adjust camera so all markers are visible
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 120));
    }

    private void showTestMarkers2() {
        if (map == null) return;

        ArrayList<LatLng> testPoints = new ArrayList<>();
        testPoints.add(new LatLng(53.5461, -113.4938)); // Edmonton Downtown
        testPoints.add(new LatLng(53.5445, -113.4910)); // Near 2nd point
        testPoints.add(new LatLng(53.5480, -113.5000)); // Slightly west

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();

        // StringBuilder to collect coordinate text
        StringBuilder text = new StringBuilder();

        for (LatLng point : testPoints) {

            // Add marker to map
            map.addMarker(new MarkerOptions()
                    .position(point)
                    .title("Test Marker"));

            // Add to bounds
            bounds.include(point);

            // Add to text for TextView
            text.append("Lat: ")
                    .append(point.getLatitude())
                    .append(" | Lng: ")
                    .append(point.getLongitude())
                    .append("\n");
        }

        // Put text into TextView
        binding.coordsTextView.setText(text.toString());

        // Move camera so all markers visible
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100));
    }


    private void showTestMarker() {
        if (map == null) return;
        LatLng testLocation = new LatLng(53.5461, -113.4938);
        map.addMarker(new MarkerOptions()
                .position(testLocation)
                .title("Test Location"));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(50.0000, 100.0000))
                .title("Test Location"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(testLocation, 13));
    }


    @Override public void onStart() { super.onStart(); binding.mapView.onStart(); }
    @Override public void onResume() { super.onResume(); binding.mapView.onResume(); }
    @Override public void onPause() { super.onPause(); binding.mapView.onPause(); }
    @Override public void onStop() { super.onStop(); binding.mapView.onStop(); }
    @Override public void onLowMemory() { super.onLowMemory(); binding.mapView.onLowMemory(); }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.mapView.onDestroy();
        binding = null;
    }
}
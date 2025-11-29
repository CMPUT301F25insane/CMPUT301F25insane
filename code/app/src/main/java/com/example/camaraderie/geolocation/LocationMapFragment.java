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
            });
        });
    }

    private void showMarkers() {
        if (map == null || userLocations == null) {
            return;
        }

        for (HashMap<String, Object> coordinates: userLocations){
            double latitude = (double) coordinates.get("latitude");
            double longitude = (double) coordinates.get("longitude");
            String userId = coordinates.get("userID").toString();

            map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(userId));
        }

        if (!userLocations.isEmpty()) {
            double firstLat = (double) userLocations.get(0).get("latitude");
            double firstLng = (double) userLocations.get(0).get("longitude");
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(firstLat, firstLng), 12));
        }
    }


    private void showTestMarker() {
        if (map == null) return;
        LatLng testLocation = new LatLng(53.5461, -113.4938);
        map.addMarker(new MarkerOptions()
                .position(testLocation)
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
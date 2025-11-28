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
import com.example.camaraderie.UserLocation;
import com.example.camaraderie.databinding.FragmentLocationMapBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.annotations.MarkerOptions;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.OnMapReadyCallback;
import org.maplibre.android.maps.Style;

public class LocationMapFragment extends Fragment {
    private FragmentLocationMapBinding binding;
    private MapLibreMap map;
    private String eventId;
    private Event event;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            eventId = getArguments().getString("eventId");
        }

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack()
        );

        binding.mapView.onCreate(savedInstanceState);

        binding.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapLibreMap mapLibreMap) {
                map = mapLibreMap;

                String styleUrl = "https://basemaps.cartocdn.com/gl/positron-gl-style/style.json";

                map.setStyle(new Style.Builder().fromUri(styleUrl), style -> fetchEventAndShowMarkers());
            }
        });
    }

    private void showTestMarker() {
        if (map == null) return;
        LatLng testLocation = new LatLng(53.5461, -113.4938);
        map.addMarker(new MarkerOptions()
                .position(testLocation)
                .title("Test Location"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(testLocation, 13));
    }

    private void fetchEventAndShowMarkers() {
        if (eventId == null) return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        event = documentSnapshot.toObject(Event.class);
                        if (map != null && event != null) {
                            showEventMarkers(event);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("LocationMapFragment", "Failed to load event", e);
                });
    }

    private void showEventMarkers(Event event) {
        if (map == null || event == null) return;

        map.clear(); // remove any previous markers

        for (UserLocation loc : event.getLocationArrayList()) {
            LatLng position = new LatLng(loc.getLatitude(), loc.getLongitude());
            map.addMarker(new MarkerOptions()
                    .position(position)
                    .title(loc.getUserDocRef().getId())); // use userId as the marker title
        }

        // optional: zoom to first user
        if (!event.getLocationArrayList().isEmpty()) {
            UserLocation first = event.getLocationArrayList().get(0);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(first.getLatitude(), first.getLongitude()), 13));
        }
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
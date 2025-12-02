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


/**
 * Fragment that displays a MapLibre map with markers representing users' locations
 * for a specific event.
 *
 * <p>The fragment receives a list of user locations through arguments under the key
 * "userLocations". Each entry in the list should be a HashMap containing:
 * <ul>
 *     <li>"latitude": Double</li>
 *     <li>"longitude": Double</li>
 *     <li>"userID": String</li>
 * </ul>
 *
 * <p>If there is only one location, the camera zooms to it. If multiple locations exist,
 * the camera adjusts to fit all markers in view.
 * @author UmranRahman
 */
public class LocationMapFragment extends Fragment {
    private FragmentLocationMapBinding binding;
    private MapLibreMap map;
    private String eventId;
    private Event event;
    ArrayList<HashMap<String, Object>> userLocations;
    private boolean isMapReady = false;

    public void updateLocations(ArrayList<HashMap<String, Object>> newLocations) {
        this.userLocations = newLocations;
        if (isMapReady) {
            showMarkers();
        }
    }

    /**
     * onCreateView sets up the fragment to be inflated by the required XML
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * Returns the root of the binding as a view
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * sets bindings
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack()
        );

        if (getArguments() != null){
            userLocations = (ArrayList<HashMap<String, Object>>) getArguments().getSerializable("userLocations");

            if(userLocations == null){
                userLocations = new ArrayList<HashMap<String, Object>>();
            }
        }

        binding.mapView.onCreate(savedInstanceState);


        binding.mapView.getMapAsync(mapLibreMap -> {
            map = mapLibreMap;

            String styleUrl = "https://basemaps.cartocdn.com/gl/positron-gl-style/style.json";

            map.setStyle(new Style.Builder().fromUri(styleUrl), style -> {
                isMapReady = true;
                showMarkers();
            });
        });
    }


    /**
     * Displays all user-location markers on the map.
     * <p>
     * If multiple locations exist, the camera fits all markers inside a bounding box.
     * If only one location exists, the camera zooms into that location.
     * Safely exits if the map or userLocations list is null or empty.
     */
    private void showMarkers() {
        if (map == null || userLocations == null) {
            return;
        }

        map.clear();

        if (userLocations.isEmpty()) {
            return;
        }

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        int count = 0;

        for (HashMap<String, Object> coordinates : userLocations) {

            double latitude = ((Number) coordinates.get("latitude")).doubleValue();
            double longitude = ((Number) coordinates.get("longitude")).doubleValue();
            String userId = String.valueOf(coordinates.get("userID"));

            LatLng point = new LatLng(latitude, longitude);

            map.addMarker(new MarkerOptions()
                    .position(point)
                    .title(userId));

            bounds.include(point);
            count++;

        }
        
        if (count == 1){
            HashMap<String, Object> c = userLocations.get(0);
            LatLng single = new LatLng(
                    ((Number) c.get("latitude")).doubleValue(),
                    ((Number) c.get("longitude")).doubleValue()
            );

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(single, 15));
        } else if (count > 1) {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
        }
    }

    /**
     * sets map view on start
     */
    @Override public void onStart() { super.onStart(); binding.mapView.onStart(); }

    /**
     * sets onresume for map view
     */
    @Override public void onResume() { super.onResume(); binding.mapView.onResume(); }

    /**
     * sets onpause for mapview
     */
    @Override public void onPause() { super.onPause(); binding.mapView.onPause(); }

    /**
     * sets onStop for mapview
     */
    @Override public void onStop() { super.onStop(); binding.mapView.onStop(); }

    /**
     * sets on low memory for map view
     */
    @Override public void onLowMemory() { super.onLowMemory(); binding.mapView.onLowMemory(); }

    /**
     * cleans up map view and binding
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.mapView.onDestroy();
        binding = null;
    }
}
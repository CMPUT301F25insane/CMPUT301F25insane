package com.example.camaraderie.admin_screen;

import android.os.Bundle;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentAdminImagesViewBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * fragment for admin to view images in the database
 */
public class AdminImagesViewFragment extends Fragment {
    private FragmentAdminImagesViewBinding binding;

    private FirebaseFirestore db;

    private NavController nav;
    private CollectionReference eventsRef;
    private ArrayList<Event> eventsArrayList;

    private PictureArrayAdapter pictureArrayAdapter;
    private ListenerRegistration eventListener;

    private SharedEventViewModel svm;

    /**
     * empty adminImagesViewFragment constructor
     */
    public AdminImagesViewFragment() {}

    /**
     * creates binding
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminImagesViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * sets db, nav, svm, and adapters for UI. sets buttons.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");
        nav = NavHostFragment.findNavController(AdminImagesViewFragment.this);

        eventsArrayList = new ArrayList<Event>();
        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);

        pictureArrayAdapter = new PictureArrayAdapter(requireContext(), eventsArrayList, svm);

        binding.pictureList.setAdapter(pictureArrayAdapter);

        loadPictures();

        binding.backButton.setOnClickListener(v -> nav.popBackStack());
    }

    /**
     * loads images from the database
     */
    private void loadPictures() {

        eventListener = eventsRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
            }
            if (value != null && !value.isEmpty()) {
                eventsArrayList.clear();
                for (QueryDocumentSnapshot snapshot : value) {
                    Event event = snapshot.toObject(Event.class);
                    if (event.getImageUrl() != null) {
                        eventsArrayList.add(event);
                    }
                }

                pictureArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * removes binding and listener
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (eventListener != null) eventListener.remove();
    }
}
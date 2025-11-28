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

public class AdminImagesViewFragment extends Fragment {
    private FragmentAdminImagesViewBinding binding;

    private FirebaseFirestore db;

    private NavController nav;
    private CollectionReference eventsRef;
    private ArrayList<Event> eventsArrayList;

    private ListenerRegistration eventListener;

    private PictureArrayAdapter pictureArrayAdapter;

    private SharedEventViewModel svm;

    public AdminImagesViewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminImagesViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

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

    private void loadPictures() {

        eventListener = eventsRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
            }
            if (value != null && !value.isEmpty()) {
                eventsArrayList.clear();
                for (QueryDocumentSnapshot snapshot: value){
                    Event event = snapshot.toObject(Event.class);
                    eventsArrayList.add(event);
                }

                pictureArrayAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
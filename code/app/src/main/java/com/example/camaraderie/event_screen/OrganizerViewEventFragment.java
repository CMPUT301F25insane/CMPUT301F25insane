package com.example.camaraderie.event_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.dashboard.MainFragment;
import com.example.camaraderie.databinding.FragmentViewEventOrganizerBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrganizerViewEventFragment extends Fragment {

    private NavController nav;
    private FirebaseFirestore db;
    private DocumentReference eventDocRef;
    private Event event;

    private FragmentViewEventOrganizerBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String eventPath = getArguments().getString("eventDocRefPath");
        db = FirebaseFirestore.getInstance();
        eventDocRef = db.collection("Events").document(eventPath);

        eventDocRef.get().addOnSuccessListener(
                documentSnapshot -> {
                    event = documentSnapshot.toObject(Event.class);
                }
        );

        nav = NavHostFragment.findNavController(OrganizerViewEventFragment.this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewEventOrganizerBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.action__fragment_organizer_view_event_to_fragment_main);
            }
        });

        binding.hostEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.action__fragment_organizer_view_event_to_fragment_create_event_testing);
            }
        });

        binding.myEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.action__fragment_organizer_view_event_to_fragment_view_my_events);
            }
        });

        binding.deleteButtonOrgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: make a DIALOGFRAGMENT to ask for CONFIRMATION FIRST
                //TODO: do the logic for this
            }
        });
    }
}

package com.example.camaraderie.event_screen;

import static android.widget.Toast.LENGTH_SHORT;

import static com.example.camaraderie.MainActivity.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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


        db = FirebaseFirestore.getInstance();

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

        String eventPath = getArguments().getString("eventDocRefPath");
        eventDocRef = db.document(eventPath);
        eventDocRef.get().addOnSuccessListener(
                documentSnapshot -> {
                    event = documentSnapshot.toObject(Event.class);
                    fillTextViews(event);
                }
        );

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
                db.collection("Users").get()
                        .addOnSuccessListener(snapshot -> {
                            for (DocumentSnapshot userDoc : snapshot.getDocuments()) {
                                DocumentReference uRef = userDoc.getReference();
                                uRef.update("waitlistedEvents", FieldValue.arrayRemove(eventDocRef));
                                uRef.update("selectedEvents", FieldValue.arrayRemove(eventDocRef));
                                uRef.update("acceptedEvents", FieldValue.arrayRemove(eventDocRef));
                            }
                        });

                user.deleteCreatedEvent(eventDocRef);

                nav.navigate(R.id.action__fragment_organizer_view_event_to_fragment_main);

            }
        });

        binding.eventEditButtonOrdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: IMPLEMENT THIS - maybe reuse the create event screen?
                Bundle args = new Bundle();

            }
        });

        binding.OrgEventRunLotteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event != null) {
                    event.runLottery();
                    event.updateDB();
                    Toast.makeText(getContext(), "Lottery has been run!", LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fillTextViews(Event event) {

        binding.eventNameForOrgView.setText(event.getEventName());
        binding.eventDescriptionOrgView.setText(event.getDescription());
        binding.registrationDeadlineTextOrgView.setText(event.getRegistrationDeadline().toString());  //TODO: deal with date stuff
        binding.orgEventViewEventDate.setText(event.getEventDate().toString());
        binding.locationOfOrgView.setText(event.getEventLocation()); //NEED TO CHANGE THIS WHEN GEOLOCATION STUFF IS IMPLEMENTED
        binding.hostNameOrgView.setText(user.getFirstName());
        binding.nameOfOrganizer.setText(user.getFirstName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.dashboard.MainFragment;
import com.example.camaraderie.databinding.FragmentViewEventOrganizerBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class OrganizerViewEventFragment extends Fragment {

    private NavController nav;
    private FirebaseFirestore db;
    private DocumentReference eventDocRef;
    private SharedEventViewModel svm;

    private Event event;

    private FragmentViewEventOrganizerBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
        nav = NavHostFragment.findNavController(this);
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewEventOrganizerBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        svm.getEvent().observe(getViewLifecycleOwner(), evt -> {
            this.event = evt;
            eventDocRef = event.getEventDocRef();
            updateUI(evt);
        });

        binding.dashboardButton.setOnClickListener(v -> nav.navigate(R.id.fragment_main));
        binding.viewAttendeesButton.setOnClickListener(v -> nav.navigate(R.id.fragment_view_waitlist));
        binding.OrgEventRunLotteryButton.setOnClickListener(v -> runLottery());

        binding.hostEvent.setOnClickListener(v -> nav.navigate(R.id.fragment_create_event_testing));
        binding.myEvents.setOnClickListener(v -> nav.navigate(R.id.fragment_view_my_events));

        binding.deleteButtonOrgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: make a DIALOGFRAGMENT to ask for CONFIRMATION FIRST
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

                nav.navigate(R.id.fragment_main);

            }
        });

        binding.eventEditButtonOrdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (event != null) {
//                    SharedEventViewModel vm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
//                    vm.setEvent(event);
                    Bundle args = new Bundle();
                    args.putString("eventDocRefPath", eventDocRef.getPath());
                    nav.navigate(R.id.action__fragment_organizer_view_event_to_fragment_create_event_testing, args);
                }
            }
        });
    }

    private void updateUI(Event e) {
        binding.eventNameForOrgView.setText(e.getEventName());
        binding.registrationDeadlineTextOrgView.setText(e.getRegistrationDeadline().toString());  //TODO: deal with date stuff
        binding.eventDescriptionOrgView.setText(e.getDescription());
        binding.attendeeCountOrganizer.setText(
                "Accepted: " + e.getAcceptedUsers().size() +
                        " | Selected: " + e.getSelectedUsers().size() +
                        " | Waitlist: " + e.getWaitlist().size()
        );
        binding.orgEventViewEventDate.setText(event.getEventDate().toString());
        binding.locationOfOrgView.setText(event.getEventLocation()); //NEED TO CHANGE THIS WHEN GEOLOCATION STUFF IS IMPLEMENTED
        binding.hostNameOrgView.setText(user.getFirstName());
        binding.nameOfOrganizer.setText(user.getFirstName());

    }

    private void runLottery() {
        Random r = new Random();

        while (event.getSelectedUsers().size() < event.getCapacity() &&
                !event.getWaitlist().isEmpty()) {

            int index = r.nextInt(event.getWaitlist().size());
            DocumentReference userRef = event.getWaitlist().get(index);

            event.getWaitlist().remove(userRef);
            event.getSelectedUsers().add(userRef);

            // Update user document lists
            userRef.update("waitlistedEvents", FieldValue.arrayRemove(event.getEventDocRef()));
            userRef.update("selectedEvents", FieldValue.arrayUnion(event.getEventDocRef()));
        }

        event.updateDB();
        updateUI(event);
        Toast.makeText(getContext(), "Lottery has been run!", LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

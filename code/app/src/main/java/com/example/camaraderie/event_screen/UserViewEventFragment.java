package com.example.camaraderie.event_screen;

import static android.widget.Toast.LENGTH_SHORT;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import static com.example.camaraderie.MainActivity.user;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.dashboard.EventViewModel;
import com.example.camaraderie.dashboard.MainFragment;
import com.example.camaraderie.databinding.FragmentViewEventUserBinding;
import com.example.camaraderie.qr_code.QRCodeDialogFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class UserViewEventFragment extends Fragment {

    private FirebaseFirestore db;
    private NavController nav;

    private FragmentViewEventUserBinding binding;
    private DocumentReference eventDocRef;
    private Event event;
    private EventViewModel eventViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nav = NavHostFragment.findNavController(UserViewEventFragment.this);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentViewEventUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        String eventPath = getArguments().getString("eventDocRefPath");

        db = FirebaseFirestore.getInstance();
        eventDocRef = db.document(eventPath);
        eventDocRef.get().addOnSuccessListener(documentSnapshot -> {
                    event = documentSnapshot.toObject(Event.class);
                    Log.d("Firestore", "Event class loaded form db");

                    fillTextViews(event);

                    if (event.getWaitlistLimit() != -1) {
                        if (event.getWaitlist().size() >= event.getWaitlistLimit()) {
                            binding.joinButtonUserView.setEnabled(false);  // too full
                        }
                    }

                    if(event.getWaitlist().contains(user.getDocRef())) {

                        Log.d("Firestore", "User is in event");
                        binding.joinButtonUserView.setEnabled(false);
                        binding.joinButtonUserView.setBackgroundColor(Color.GRAY);

                    }
                    else {
                        Log.d("Firestore", "User is not in event");
                        binding.unjoinButtonUserView.setEnabled(false);
                        binding.unjoinButtonUserView.setBackgroundColor(Color.GRAY);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Event not loaded from db!");
                    Toast.makeText(requireContext(), "Event Not Found", LENGTH_SHORT).show();
                    NavHostFragment.findNavController(UserViewEventFragment.this)
                            .navigate(R.id.fragment_main);
                });

        binding.joinButtonUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("here", "somehow");
                eventDocRef.update("waitlist", FieldValue.arrayUnion(user.getDocRef())).addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "You have joined the event", LENGTH_SHORT).show();
                });
                binding.joinButtonUserView.setEnabled(false);
                binding.joinButtonUserView.setBackgroundColor(Color.GRAY);

                binding.unjoinButtonUserView.setEnabled(true);
                binding.unjoinButtonUserView.setBackgroundColor(Color.RED);

                user.addWaitlistedEvent(eventDocRef);

                nav.navigate(R.id.action_fragment_view_event_user_to_fragment_main);
            }

        });

        if (user.isAdmin()) {
            binding.adminDeleteEvent.setEnabled(true);
        }
        else {
            binding.adminDeleteEvent.setEnabled(false);
        }

        binding.adminDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users").get()
                        .addOnSuccessListener(snapshot -> {
                            for (DocumentSnapshot userDoc : snapshot.getDocuments()) {
                                DocumentReference uRef = userDoc.getReference();
                                uRef.update("waitlistedEvents", FieldValue.arrayRemove(eventDocRef));
                                uRef.update("selectedEvents", FieldValue.arrayRemove(eventDocRef));
                                uRef.update("acceptedEvents", FieldValue.arrayRemove(eventDocRef));
                            }
                        });

                nav.navigate(R.id.action_fragment_view_event_user_to_admin_event_data_screen);
            }
        });

        binding.unjoinButtonUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDocRef.update("waitlist", FieldValue.arrayRemove(user.getDocRef())).addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "You have left the event", LENGTH_SHORT).show();
                });

                binding.joinButtonUserView.setEnabled(true);
                binding.joinButtonUserView.setBackgroundColor(Color.GREEN);

                binding.unjoinButtonUserView.setEnabled(false);
                binding.unjoinButtonUserView.setBackgroundColor(Color.GRAY);

                // this is so fuckin jank bro
                user.removeWaitlistedEvent(eventDocRef);
                for ( Event event : eventViewModel.getLocalEvents().getValue()) {
                    if (event.getEventDocRef().equals(eventDocRef)){
                        event.removeWaitlistUser(user.getDocRef());  // update live data
                    }
                }

                nav.navigate(R.id.action_fragment_view_event_user_to_fragment_main);
            }

        });

        binding.qrButtonUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();

                args.putString("eventId", event.getEventId());

                QRCodeDialogFragment dialogFragment = QRCodeDialogFragment.newInstance(event.getEventId());
                dialogFragment.show(getParentFragmentManager(), "qr_dialog");
            }

            });

        binding.dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.action_fragment_view_event_user_to_fragment_main);
            }
        });

        binding.hostEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.action_fragment_view_event_user_to_fragment_create_event_testing);
            }
        });

        binding.myEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.action__fragment_view_event_user_to_fragment_view_my_events);
            }
        });

        binding.viewAttendeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("eventDocRefPath", eventDocRef.getPath());
                nav.navigate(R.id.action_fragment_view_event_user_to_fragment_view_waitlist, args);
            }
        });
    }

    private void fillTextViews(Event event) {

        binding.eventNameForUserView.setText(event.getEventName());
        binding.eventDescriptionUserView.setText(event.getDescription());
        binding.registrationDeadlineTextUserView.setText(event.getRegistrationDeadline().toString());  //TODO: deal with date stuff
        binding.userEventViewEventDate.setText(event.getEventDate().toString());
        binding.locationOfUserView.setText(event.getEventLocation()); //NEED TO CHANGE THIS WHEN GEOLOCATION STUFF IS IMPLEMENTED
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

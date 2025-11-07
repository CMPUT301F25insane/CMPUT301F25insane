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
import com.example.camaraderie.SharedEventViewModel;
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
    private Event event;
//    private EventViewModel eventViewModel;
    private SharedEventViewModel svm;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
        db = FirebaseFirestore.getInstance();
        nav = NavHostFragment.findNavController(this);

    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentViewEventUserBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        svm.getEvent().observe(getViewLifecycleOwner(), evt -> {
            event = evt;
            updateUI(evt);
        });

        binding.joinButtonUserView.setOnClickListener(v -> handleJoin());
        binding.unjoinButtonUserView.setOnClickListener(v -> handleUnjoin());
        binding.dashboardButton.setOnClickListener(v -> nav.navigate(R.id.fragment_main));
        binding.myEvents.setOnClickListener(v -> nav.navigate(R.id.fragment_view_my_events));
        binding.hostEvent.setOnClickListener(v -> nav.navigate(R.id.fragment_create_event_testing));
        binding.viewAttendeesButton.setOnClickListener(v -> nav.navigate(R.id.fragment_view_waitlist));

        if (user.isAdmin()) {
            binding.adminDeleteEvent.setEnabled(true);
        }
        else {
            binding.adminDeleteEvent.setEnabled(false);
        }
        binding.adminDeleteEvent.setOnClickListener(v -> adminDeleteEvent());

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
    }

    private void updateUI(Event e) {
        binding.eventNameForUserView.setText(e.getEventName());
        binding.eventDescriptionUserView.setText(e.getDescription());
        binding.registrationDeadlineTextUserView.setText(e.getRegistrationDeadline().toString());
        binding.userEventViewEventDate.setText(e.getEventDate().toString());
        binding.locationOfUserView.setText(e.getEventLocation());

        boolean userInWaitlist = e.getWaitlist().contains(user.getDocRef());
        binding.joinButtonUserView.setEnabled(!userInWaitlist);
        binding.unjoinButtonUserView.setEnabled(userInWaitlist);
    }

    private void handleJoin() {
        event.getEventDocRef().update("waitlist", FieldValue.arrayUnion(user.getDocRef()));
        user.addWaitlistedEvent(event.getEventDocRef());
        updateUI(event);
        nav.navigate(R.id.fragment_main);
    }

    private void handleUnjoin() {
        event.getEventDocRef().update("waitlist", FieldValue.arrayRemove(user.getDocRef()));
        user.removeWaitlistedEvent(event.getEventDocRef());
        updateUI(event);
        nav.navigate(R.id.fragment_main);
    }

    private void adminDeleteEvent() {
        db.collection("Users").get().addOnSuccessListener(snapshot -> {
            for (DocumentSnapshot userDoc : snapshot.getDocuments()) {
                DocumentReference uRef = userDoc.getReference();
                uRef.update("waitlistedEvents", FieldValue.arrayRemove(event.getEventDocRef()));
                uRef.update("selectedEvents", FieldValue.arrayRemove(event.getEventDocRef()));
                uRef.update("acceptedEvents", FieldValue.arrayRemove(event.getEventDocRef()));
            }
        });
        event.getEventDocRef().delete();
        nav.navigate(R.id.fragment_main);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

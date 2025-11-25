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

/**
 * The screen for user's viewing an uploaded event
 */
public class UserViewEventFragment extends Fragment {

    private FirebaseFirestore db;
    private NavController nav;

    private FragmentViewEventUserBinding binding;
    private Event event;
//    private EventViewModel eventViewModel;
    private SharedEventViewModel svm;


    /**
     * Called to have the fragment instantiate its data
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
        db = FirebaseFirestore.getInstance();
        nav = NavHostFragment.findNavController(this);

    }

    /**
     * set binding
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return constructed view
     */
    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentViewEventUserBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    /**
     * set bidnings for each button and other text views. enable and disable buttons based on priviledge,
     * setup qr code button functions
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
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

            /**
             * On pressing the QR button, open the QR code dialog fragment
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();

                args.putString("eventId", event.getEventId());

                QRCodeDialogFragment dialogFragment = QRCodeDialogFragment.newInstance(event.getEventId());
                dialogFragment.show(getParentFragmentManager(), "qr_dialog");
            }

            });

        binding.dashboardButton.setOnClickListener(new View.OnClickListener() {
            /**
             * On pressing the back button, navigate back to the main fragment
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.action_fragment_view_event_user_to_fragment_main);
            }
        });

        binding.viewPhotosUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Send the user to a fragment that has a view of the current photo
                Bundle args = new Bundle();
                args.putString("eventId", event.getEventId());
                nav.navigate(R.id.action__fragment_user_view_event_to_fragment_user_view_photos, args);
            }
        });
    }

    /**
     * updates UI textviews
     * @param e event to get details from
     */
    private void updateUI(Event e) {
        binding.eventNameForUserView.setText(e.getEventName());
        binding.eventDescriptionUserView.setText(e.getDescription());
        binding.registrationDeadlineTextUserView.setText(e.getRegistrationDeadline().toString());
        binding.userEventViewEventDate.setText(e.getEventDate().toString());
        binding.locationOfUserView.setText(e.getEventLocation());

        db.document(e.getHostDocRef().getPath()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            /**
             * on success listener for snapshot
             * @param documentSnapshot docsnap element from database
             */
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String hostName = documentSnapshot.getString("firstName");

                    if (binding != null) {
                        binding.hostNameUserView.setText(hostName);
                    }
                }

            }

            });


        boolean userInWaitlist = e.getWaitlist().contains(user.getDocRef());
        binding.joinButtonUserView.setEnabled(!userInWaitlist);
        binding.unjoinButtonUserView.setEnabled(userInWaitlist);
    }

    /**
     * handles join, updates database
     */
    private void handleJoin() {
        event.getEventDocRef().update("waitlist", FieldValue.arrayUnion(user.getDocRef()));
        user.addWaitlistedEvent(event.getEventDocRef());
        updateUI(event);
        nav.navigate(R.id.fragment_main);
    }

    /**
     * handles unjoin, updates database
     */
    private void handleUnjoin() {
        event.getEventDocRef().update("waitlist", FieldValue.arrayRemove(user.getDocRef()));
        user.removeWaitlistedEvent(event.getEventDocRef());
        updateUI(event);
        nav.navigate(R.id.fragment_main);
    }

    /**
     * admin can delete events, updates database
     */
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

    /**
     * binding set to null
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

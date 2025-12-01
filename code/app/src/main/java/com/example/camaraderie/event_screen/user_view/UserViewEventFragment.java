package com.example.camaraderie.event_screen.user_view;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.res.ColorStateList;
import android.graphics.Color;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import static com.example.camaraderie.accepted_screen.UserAcceptedHandler.userDeclineInvite;
import static com.example.camaraderie.geolocation.AddUserLocation.addLocation;
import static com.example.camaraderie.main.Camaraderie.getUser;
import static com.example.camaraderie.main.MainActivity.user;
import static com.example.camaraderie.utilStuff.EventDeleter.deleteEvent;
import static com.example.camaraderie.utilStuff.EventHelper.handleJoin;
import static com.example.camaraderie.utilStuff.EventHelper.handleUnjoin;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;

import com.example.camaraderie.databinding.FragmentViewEventUserBinding;
import com.example.camaraderie.event_screen.ViewListViewModel;


import com.example.camaraderie.qr_code.QRCodeDialogFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * The screen for user's viewing an uploaded event
 */
public class UserViewEventFragment extends Fragment {

    private FirebaseFirestore db;
    private NavController nav;
    private FragmentViewEventUserBinding binding;
    private Event event;
    private SharedEventViewModel svm;
    private ViewListViewModel vm;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


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
        vm = new ViewModelProvider(requireActivity()).get(ViewListViewModel.class);  // this will live in the activity
        db = FirebaseFirestore.getInstance();
//        nav = NavHostFragment.findNavController(this);



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
        nav = NavHostFragment.findNavController(this);


        // get event details, everything that depends on event as an object exists here

        binding.userViewEventBackButton.setOnClickListener(v -> nav.popBackStack());
        svm.getEvent().observe(getViewLifecycleOwner(), evt -> {
            event = evt;
            updateUI(evt);


            binding.viewListsButton.setOnClickListener(v -> {
                vm.setEvent(event);
                vm.generateAllLists(() -> {
                    nav.navigate(R.id.fragment_list_testing_interface);
                });
            });

            if (!user.isAdmin()) {
                binding.viewListsButton.setEnabled(false);
                binding.viewListsButton.setVisibility(INVISIBLE);
            }
        });

        // admin log stuff, only admins can view logs
        binding.adminViewLogs.setEnabled(false);
        binding.adminViewLogs.setVisibility(GONE);
        binding.adminViewLogs.setOnClickListener(v -> nav.navigate(R.id.AdminNotificationLogsFragment));

        if (getUser().isAdmin()) {
            binding.adminViewLogs.setEnabled(true);
            binding.adminViewLogs.setVisibility(VISIBLE);
        }

        binding.nameOfUseranizer.setText(getUser().getFirstName());

        // button handlers
        binding.joinButtonUserView.setOnClickListener(v -> handleJoinGeo());

        binding.unjoinButtonUserView.setOnClickListener(v -> handleUnjoin(
                event,
                () -> {
                    updateUI(event);
                    if (!nav.popBackStack(R.id.fragment_main, false)) {
                        nav.navigate(R.id.fragment_main);
                    }
                },
                // onfailure
                () -> {
                    if (!nav.popBackStack(R.id.fragment_main, false)) {
                        nav.navigate(R.id.fragment_main);
                    }
                }));


        binding.dashboardButton.setOnClickListener(v -> {
            if (!nav.popBackStack(R.id.fragment_main, false)) {
                nav.navigate(R.id.fragment_main);
            };
        });
        binding.myEvents.setOnClickListener(v -> nav.navigate(R.id.fragment_view_my_events));
        binding.hostEvent.setOnClickListener(v -> nav.navigate(R.id.fragment_create_event));
        binding.userViewProfileImage.setOnClickListener(v -> nav.navigate(R.id.update_user));

        // set up admin delete button
        binding.adminDeleteEvent.setEnabled(false);
        binding.adminDeleteEvent.setVisibility(GONE);
        if (user.isAdmin()) {
            binding.adminDeleteEvent.setEnabled(true);
            binding.adminDeleteEvent.setVisibility(VISIBLE);
            binding.adminDeleteEvent.setOnClickListener(v -> adminDeleteEvent());
        }
        // qr code button
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
        binding.registrationDeadlineTextUserView.setText(sdf.format(e.getRegistrationDeadline()));
        binding.userEventViewEventDate.setText(sdf.format(e.getEventDate()));
        binding.locationOfUserView.setText(e.getEventLocation());

        String limit;
        if (e.getWaitlistLimit() == -1) {
            limit = "none";
        }

        else limit = String.valueOf(e.getWaitlistLimit());

        binding.attendeeCountOrganizer.setText(
                "Accepted: " + e.getAcceptedUsers().size() +
                        " | Selected: " + e.getSelectedUsers().size() +
                        " | Waitlist: " + e.getWaitlist().size() +
                        " | Limit: " + limit
        );

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

        handleEnableJoin(e);
    }

    private void handleEnableJoin(Event event) {

        binding.joinButtonUserView.setEnabled(true);
        binding.joinButtonUserView.setVisibility(VISIBLE);
        binding.joinButtonUserView.setClickable(true);
        binding.joinButtonUserView.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.custom_join_button_color));

        binding.unjoinButtonUserView.setEnabled(true);
        binding.unjoinButtonUserView.setClickable(true);
        binding.unjoinButtonUserView.setVisibility(VISIBLE);
        binding.unjoinButtonUserView.setBackgroundColor(Color.RED);

        binding.deadlinePassedText.setText("");

        // user has no buttons to click
        if (event.getAcceptedUsers().contains(getUser().getDocRef())) {
            binding.joinButtonUserView.setClickable(false);
            binding.joinButtonUserView.setEnabled(false);
            binding.joinButtonUserView.setVisibility(GONE);

            binding.unjoinButtonUserView.setEnabled(false);
            binding.unjoinButtonUserView.setClickable(false);
            binding.unjoinButtonUserView.setVisibility(GONE);

            binding.deadlinePassedText.setText("You are accepted to this event.");
        }
        if (event.getCapacity() <= event.getAcceptedUsers().size()) {
            binding.joinButtonUserView.setClickable(false);
            binding.joinButtonUserView.setEnabled(false);
            binding.joinButtonUserView.setVisibility(GONE);

            binding.unjoinButtonUserView.setEnabled(false);
            binding.unjoinButtonUserView.setClickable(false);
            binding.unjoinButtonUserView.setVisibility(GONE);
            binding.deadlinePassedText.setText("Event is at capacity.");
        }

        ArrayList<DocumentReference> list = new ArrayList<>();
        list.addAll(event.getSelectedUsers());
        list.addAll(event.getWaitlist());

        // only gray out the thing, leave unjoin alone
        if (list.contains(getUser().getDocRef())) {
            binding.joinButtonUserView.setClickable(false);
            binding.joinButtonUserView.setEnabled(false);
            binding.joinButtonUserView.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        } else if (event.getRegistrationDeadline().before(new Date())) {

            binding.joinButtonUserView.setClickable(false);
            binding.joinButtonUserView.setEnabled(false);
            binding.joinButtonUserView.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            binding.deadlinePassedText.setText("Deadline has passed!");

        }
        // otherwise, gray out the unjoin button
        else {
            binding.unjoinButtonUserView.setEnabled(false);
            binding.unjoinButtonUserView.setClickable(false);
            binding.unjoinButtonUserView.setBackgroundColor(Color.GRAY);
            binding.deadlinePassedText.setText("");
        }

        // if user is selected, the unjoin button acts as a decline button
        if (event.getSelectedUsers().contains(getUser().getDocRef())) {
            binding.unjoinButtonUserView.setOnClickListener(v -> {
                userDeclineInvite(event);

                // disable the button
                binding.unjoinButtonUserView.setEnabled(false);
                binding.unjoinButtonUserView.setClickable(false);
                binding.unjoinButtonUserView.setBackgroundColor(Color.GRAY);

                if (!nav.popBackStack(R.id.fragment_main, false)) {
                    nav.navigate(R.id.fragment_main);
                };
            });

            // join button is already handled at this point
        }
    }

    private void handleJoinGeo() {
        if (event == null) {
            Toast.makeText(getContext(), "Event not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.isGeoEnabled() && event.isGeoEnabled()) {

            addLocation(
                    event,
                    () -> {

                        handleJoin(event,
                                () -> {
                                    updateUI(event);
                                    svm.setEvent(event);
                                    nav.navigate(R.id.fragment_view_event_user);
                                },
                                // on failure
                                () -> {
                                    if (!nav.popBackStack(R.id.fragment_main, false)) {
                                        nav.navigate(R.id.fragment_main);
                                    }
                                }
                        );
                    }
            );
        } else if (!user.isGeoEnabled() && event.isGeoEnabled()) {
            Toast.makeText(getContext(), "Please enable location to join this event", Toast.LENGTH_SHORT).show();
        } else {
            handleJoin(event,
                    () -> {
                        updateUI(event);
                        svm.setEvent(event);
                        nav.navigate(R.id.fragment_view_event_user);
                    },
                    // on failure
                    () -> {
                        if (!nav.popBackStack(R.id.fragment_main, false)) {
                            nav.navigate(R.id.fragment_main);
                        }
                    });
            }
    }


    /**
     * admin can delete events, updates database
     */
    private void adminDeleteEvent() {

        deleteEvent(event);
        if (!nav.popBackStack(R.id.fragment_main, false)) {
            nav.navigate(R.id.fragment_main);
        }
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

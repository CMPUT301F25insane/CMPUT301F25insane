package com.example.camaraderie.event_screen.organizer_view;

import static android.widget.Toast.LENGTH_SHORT;

import static com.example.camaraderie.main.MainActivity.user;
import static com.example.camaraderie.my_events.LotteryRunner.runLottery;

import android.os.Bundle;
import android.util.Log;
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
import com.example.camaraderie.databinding.FragmentViewEventOrganizerBinding;
import com.example.camaraderie.event_screen.ViewListViewModel;
import com.example.camaraderie.qr_code.QRCodeDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The screen for an organizer viewing their own event. They can delete and edit their event here.
 */

public class OrganizerViewEventFragment extends Fragment {

    private NavController nav;
    private FirebaseFirestore db;
    private DocumentReference eventDocRef;
    private SharedEventViewModel svm;

    private Event event;

    private FragmentViewEventOrganizerBinding binding;
    private ViewListViewModel vm;

    //private NotificationController notificationController;

    /**
     * sets svm, nav, and db.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
        vm = new ViewModelProvider(requireActivity()).get(ViewListViewModel.class);  // this will live in the activity

        nav = NavHostFragment.findNavController(this);
        db = FirebaseFirestore.getInstance();
        //notificationController = new NotificationController(getContext(), (com.example.notifications.NotificationView) getParentFragment());

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
     * @return binding root
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewEventOrganizerBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    /**
     * sets bidnings for buttons and textviews. svm gets event and updates as appropriate
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        svm.getEvent().observe(getViewLifecycleOwner(), evt -> {
            this.event = evt;
            eventDocRef = event.getEventDocRef();
            updateUI(evt);
        });

        binding.orgViewProfileImage.setOnClickListener(v -> nav.navigate(R.id.update_user));

        binding.orgViewBackButton.setOnClickListener(v -> nav.popBackStack());
        binding.dashboardButton.setOnClickListener(v -> nav.navigate(R.id.fragment_main));
        binding.viewListsButton.setOnClickListener(v -> {

            vm.setEvent(event);
            vm.generateAllLists(() -> {
                nav.navigate(R.id.fragment_list_testing_interface); //TODO: user should NOT SEE these lists in general, only capacity.
            });
        });
        binding.OrgEventRunLotteryButton.setOnClickListener(v -> {
            runLottery(event);
            updateUI(event);
            Toast.makeText(getContext(), "Lottery has been run!", LENGTH_SHORT).show();
        });

        binding.hostEvent.setOnClickListener(v -> nav.navigate(R.id.fragment_create_event_testing));
        binding.myEvents.setOnClickListener(v -> nav.navigate(R.id.fragment_view_my_events));

        binding.deleteButtonOrgView.setOnClickListener(new View.OnClickListener() {
            /**
             * delete functionality for events. updates database
             * @param v The view that was clicked.
             */
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
            /**
             * navigate to editing create event. set event docref in bundle
             * @param v The view that was clicked.
             */
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

        binding.qrButtonOrgView.setOnClickListener(new View.OnClickListener() {
            /**
             * get qr code dialogfragment
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

        binding.viewPhotosOrgView.setOnClickListener(new View.OnClickListener() {

            /**
             * navigate to view photos fragment
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v){
                // Send the organizer to a fragment that has a view of the current photo
                // and a button to upload a photo
                Bundle args = new Bundle();
                args.putString("eventId", event.getEventId());
                nav.navigate(R.id.action__fragment_organizer_view_event_to_fragment_organizer_view_photos, args);
            }


        });

        binding.showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.action__fragment_organizer_view_event_to_map);
            }
        });


        //TODO: make diagolg builder for announcement sending, or separate screen.
        //TODO: use the new organizernotificationhandler class to deal with that shit.
        //TODO: flow is as such: handler updates db, db automatically picks up on this (functions) \
        // and handles it, db handler calls the fcm, fcm calls the receiver,
        // receiver formats the remoteMessage. then notif is built, then displayed on users device.

        binding.orgNotifScreenButton.setOnClickListener(v -> {

            nav.navigate(R.id.OrganizerNotificationChooserFragment);
        });

    }

    /**
     * updates ui textviews
     * @param e event from which to get details from
     */
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

    /**
     * binding set to null
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package com.example.camaraderie.event_screen.organizer_view;

import static com.example.camaraderie.main.MainActivity.user;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentCreateEventBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

/**
 * fragment that allows an organizer to create an event
 */
public class CreateEventFragment extends Fragment {

    private FragmentCreateEventBinding binding;
    private DocumentReference eventDocRef;
    private Event event;

    private EditText eventName;
    private TextView eventDate;
    private TextView eventDeadline;
    private EditText eventLocation;
    private EditText eventDescription;
    private EditText eventCapacity;
    private EditText eventTime;
    private EditText optionalLimit;

    private Uri eventPosterUri;
    private boolean editing = false;

    /**
     * Instantiate the fragment
     * @param savedInstanceState
     *  This is the previously saved state of the fragment
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Create the view for the fragment
     * @param inflater
     *  The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container
     *  If non-null, this is the parent view that the fragment's
     *  UI should be attached to.  The fragment should not add the view itself
     *  but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState
     *  This is the previously saved state of the fragment
     * @return
     *  Return the View for the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentCreateEventBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    /**
     * sets edittext definitions, gets args if editing and sets bool, sets button listeners
     * Call when the view has been created but before the previous state has been restored
     * @param view
     *  The View returned by onCreateView
     * @param savedInstanceState
     *  This fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventName = binding.inputFieldForCreateEventName;
        eventDate = binding.inputFieldForCreateEventDate;
        eventDeadline = binding.inputFieldForCreateEventRegistrationDeadline;
        eventLocation = binding.inputFieldForCreateEventLocation;
        eventDescription = binding.inputFieldForCreateEventDescription;
        eventCapacity = binding.inputFieldForCreateEventNumOfAttendees;
        eventTime = binding.inputFieldForCreateEventTime;
        optionalLimit = binding.inputFieldForCreateEventWaitlistLimit;

        Bundle args = getArguments();
        if (args != null) {
            editing = true;

            String path = args.getString("eventDocRefPath");
            eventDocRef = FirebaseFirestore.getInstance().document(path);

            eventDocRef.get().addOnSuccessListener(doc -> {
                event = doc.toObject(Event.class);
                assert event != null;
                fillTextViews(event);
            });

        }

        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        //Add code to save the photo into the database
                        //Need to set the Uri to the event's uri
                        eventPosterUri = uri;
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        binding.inputFieldForCreateEventDate.setOnClickListener(new View.OnClickListener() {
            /**
             * sets date dialogfragment
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                openDateDialogue();
            }
        });

        binding.inputFieldForCreateEventRegistrationDeadline.setOnClickListener(new View.OnClickListener() {
            /**
             * sets deadline dialogfragment
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                openDeadlineDialogue();
            }
        });

        binding.buttonForAddPicture.setOnClickListener(new View.OnClickListener() {
            /**
             * setup nav for adding pictures to event
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        binding.buttonForGoingBack.setOnClickListener(new View.OnClickListener() {
            /**
             * On pressing the back button, navigate back to the main fragment
             * @param v
             *  The view that was clicked.
             */
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(CreateEventFragment.this)
                        .popBackStack();
            }
        });

        binding.buttonForConfirm.setOnClickListener(new View.OnClickListener() {
            /**
             * On pressing the confirm button, create the event
             * @param v
             *  The view that was clicked.
             */
            @Override
            public void onClick(View v) {

                try {
                    createEvent(eventName, eventDate, eventDeadline, eventLocation, eventDescription, eventCapacity, optionalLimit, eventTime, eventPosterUri);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Please enter valid details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonForAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });
    }

    /**
     * fill textviews for fragment
     * @param event event from whcih to get the details
     */
    private void fillTextViews(Event event) {
        eventName.setText(event.getEventName());
        eventDate.setText(event.getEventDate().toString());
        eventDeadline.setText(event.getRegistrationDeadline().toString());
        eventLocation.setText(event.getEventLocation());
        eventDescription.setText(event.getDescription());
        eventCapacity.setText(String.valueOf(event.getCapacity()));
        eventTime.setText(event.getEventTime());
    }

    /**
     * Destroy the view for the fragment
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    /**
     * creates new event and updates db. if editing is true, it updates db instead
     * @param eventName new name
     * @param eventDate new date
     * @param eventDeadline new deadline
     * @param eventLocation new location
     * @param eventDescription new description
     * @param eventCapacity new capatiy
     * @param optionalLimit new optional limit
     * @param eventTime new time
     * @throws ParseException throws parseException if parse fails (mainly date)
     */
    private void createEvent(EditText eventName,
                             TextView eventDate,
                             TextView eventDeadline,
                             EditText eventLocation,
                             EditText eventDescription,
                             EditText eventCapacity,
                             EditText optionalLimit,
                             EditText eventTime,
                             Uri eventPosterUri) throws ParseException {

        String name = eventName.getText().toString();
        String description = eventDescription.getText().toString();
        String location = eventLocation.getText().toString();
        String time = eventTime.getText().toString();
        int capacity = Integer.parseInt(eventCapacity.getText().toString());
        int limit = -1;  // false false
        if (!optionalLimit.getText().toString().isEmpty()){
            limit = Integer.parseInt(optionalLimit.getText().toString());
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date deadline =  formatter.parse(eventDeadline.getText().toString());
        Date date = formatter.parse(eventDate.getText().toString());


        // NEED TO GET TIME, AND CREATE EVENT ID
        // validate user input and store in database.
        // DO NOT LEAK THE DB BY DOCUMENT INJECTION BY ACCIDENT

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (editing) {
            event.setEventName(name);
            event.setEventDescription(description);
            event.setEventLocation(location);
            event.setEventTime(time);
            event.setRegistrationDeadline(deadline);
            event.setEventDate(date);
            event.setCapacity(capacity);
            event.setPosterUri(eventPosterUri);
            if (limit != -1) {
                event.setWaitlistLimit(limit);
            }
            eventDocRef.set(event, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        SharedEventViewModel vm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
                        vm.setEvent(event);

                        NavHostFragment.findNavController(CreateEventFragment.this)
                                .navigate(R.id.action_fragment_create_event_testing_to__fragment_organizer_view_event);
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating event", e));
        }
        else {
            DocumentReference eventRef = db.collection("Events").document();
            String eventId = eventRef.getId();
            Event newEvent;
            if (limit != -1) {
                newEvent = new Event(name, location, deadline, description, date, time, capacity, limit, user.getDocRef(), eventRef, eventId, eventPosterUri);
            }
            else {
                newEvent = new Event(name, location, deadline, description, date, time, capacity, user.getDocRef(), eventRef, eventId, eventPosterUri);
            }


            eventRef.set(newEvent)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Event added with ID: " + eventId);

                        user.addCreatedEvent(eventRef);

                        SharedEventViewModel vm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
                        vm.setEvent(newEvent);

                        NavHostFragment.findNavController(CreateEventFragment.this)
                                .navigate(R.id.action_fragment_create_event_testing_to__fragment_organizer_view_event);
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error adding event", e));

        }

    }

    /**
     * event date picker, sets binding textview
     */
    private void openDateDialogue() {
        DatePickerDialog dateDialog;
        dateDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                binding.inputFieldForCreateEventDate.setText(year + "-" + (month+1) + "-" + day);
            }

        }, 2025, 10, 6);

        dateDialog.show();

    }

    /**
     * deadline date picker, sets binding textview
     */
    private void openDeadlineDialogue() {
        DatePickerDialog dateDialog;
        dateDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                binding.inputFieldForCreateEventRegistrationDeadline.setText(year + "-" + (month+1) + "-" + day);
            }

        }, 2025, 10, 6);

        dateDialog.show();

    }
}

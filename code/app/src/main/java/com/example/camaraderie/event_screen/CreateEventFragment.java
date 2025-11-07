package com.example.camaraderie.event_screen;

import static com.example.camaraderie.MainActivity.user;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.databinding.FragmentCreateEventTestingBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

/**
 * Interface that allows an organizer to create an event
 */
public class CreateEventFragment extends Fragment {

    private FragmentCreateEventTestingBinding binding;
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
    private boolean editing = false;

    /**
     * Instantiate the interface
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

        binding = FragmentCreateEventTestingBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    /**
     * Call when the view has been created but before the previous state has been restored
     * @param view
     *  The View returned by onCreateView
     * @param savedInstanceState
     *  This fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventName = binding.createEventName;
        eventDate = binding.createEventDate;
        eventDeadline = binding.createEventDeadline;
        eventLocation = binding.createEventLocation;
        eventDescription = binding.createEventDescription;
        eventCapacity = binding.createEventCapacity;
        eventTime = binding.createEventTime;
        optionalLimit = binding.optionalLimit;

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

        binding.createEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialogue();
            }
        });

        binding.createEventDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeadlineDialogue();
            }
        });

        binding.createEventPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        binding.createEventBackButton.setOnClickListener(new View.OnClickListener() {
            /**
             * On pressing the back button, navigate back to the main fragment
             * @param v
             *  The view that was clicked.
             */
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(CreateEventFragment.this)
                        .navigate(R.id.action_fragment_create_event_testing_to_fragment_main);

            }
        });

        binding.createEventConfirmButton.setOnClickListener(new View.OnClickListener() {
            /**
             * On pressing the confirm button, create the event
             * @param v
             *  The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                try {
                    createEvent(eventName, eventDate, eventDeadline, eventLocation, eventDescription, eventCapacity, optionalLimit, eventTime);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Please enter valid details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

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

    private void createEvent(EditText eventName,
                             TextView eventDate,
                             TextView eventDeadline,
                             EditText eventLocation,
                             EditText eventDescription,
                             EditText eventCapacity,
                             EditText optionalLimit,
                             EditText eventTime) throws ParseException {

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
            if (limit != -1) {
                event.setWaitlistLimit(limit);
            }
            eventDocRef.set(event, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Bundle args = new Bundle();
                        args.putString("eventDocRefPath", eventDocRef.getPath());

                        NavHostFragment.findNavController(CreateEventFragment.this)
                                .navigate(R.id.action_fragment_create_event_testing_to__fragment_organizer_view_event, args);
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating event", e));
        }
        else {
            DocumentReference eventRef = db.collection("Events").document();
            String eventId = eventRef.getId();
            Event newEvent;
            if (limit != -1) {
                newEvent = new Event(name, location, deadline, description, date, time, capacity, limit, user.getDocRef(), eventRef, eventId);
            }
            else {
                newEvent = new Event(name, location, deadline, description, date, time, capacity, user.getDocRef(), eventRef, eventId);
            }


            eventRef.set(newEvent)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Event added with ID: " + eventId);

                        user.addCreatedEvent(eventRef);

                        Bundle args = new Bundle();
                        args.putString("eventDocRefPath", eventRef.getPath());

                        NavHostFragment.findNavController(CreateEventFragment.this)
                                .navigate(R.id.action_fragment_create_event_testing_to__fragment_organizer_view_event, args);
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error adding event", e));

        }

    }

    private void openDateDialogue() {
        DatePickerDialog dateDialog;
        dateDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                binding.createEventDate.setText(year + "-" + (month+1) + "-" + day);
            }

        }, 2025, 10, 6);

        dateDialog.show();

    }

    private void openDeadlineDialogue() {
        DatePickerDialog dateDialog;
        dateDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                binding.createEventDeadline.setText(year + "-" + (month+1) + "-" + day);
            }

        }, 2025, 10, 6);

        dateDialog.show();

    }
}

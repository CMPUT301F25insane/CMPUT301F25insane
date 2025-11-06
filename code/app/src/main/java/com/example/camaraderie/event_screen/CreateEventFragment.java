package com.example.camaraderie.event_screen;

import static com.example.camaraderie.MainActivity.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

/**
 * Interface that allows an organizer to create an event
 */
public class CreateEventFragment extends Fragment {

    private FragmentCreateEventTestingBinding binding;

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
                EditText eventName = binding.createEventName;
                EditText eventDate = binding.createEventDate;
                EditText eventDeadline = binding.createEventDeadline;
                EditText eventLocation = binding.createEventLocation;
                EditText eventDescription = binding.createEventDescription;
                EditText eventCapacity = binding.createEventCapacity;
                EditText eventTime = binding.createEventTime;

                try {
                    createEvent(eventName, eventDate, eventDeadline, eventLocation, eventDescription, eventCapacity, eventTime);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
                             EditText eventDate,
                             EditText eventDeadline,
                             EditText eventLocation,
                             EditText eventDescription,
                             EditText eventCapacity,
                             EditText eventTime) throws ParseException {

        String name = eventName.getText().toString();
        String description = eventDescription.getText().toString();
        String location = eventLocation.getText().toString();
        String time = eventTime.getText().toString();
        int capacity = Integer.parseInt(eventCapacity.getText().toString());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date deadline =  formatter.parse(eventDeadline.getText().toString());
        Date date = formatter.parse(eventDate.getText().toString());

        // NEED TO GET TIME, AND CREATE EVENT ID
        // validate user input and store in database.
        // DO NOT LEAK THE DB BY DOCUMENT INJECTION BY ACCIDENT


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("Events").document();
        String eventId = eventRef.getId();
        Event event = new Event(name, location, deadline, description, date, time, capacity, user.getDocRef(), eventRef, eventId);

        eventRef.set(event)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Event added with ID: " + eventId);

                    user.addCreatedEvent(eventRef);

                    NavHostFragment.findNavController(CreateEventFragment.this)
                            .navigate(R.id.action_fragment_create_event_testing_to_fragment_main);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error adding event", e));

    }
}

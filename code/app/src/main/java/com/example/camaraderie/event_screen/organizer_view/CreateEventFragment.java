package com.example.camaraderie.event_screen.organizer_view;

import static com.example.camaraderie.main.MainActivity.user;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentCreateEventBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Document;

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
    private Switch geoSwitch;
    private boolean geoEnabled;
    private NavController nav;

    private String eventImageString;
    private boolean editing = false;

    private Date today = new Date();
    private int day = today.getDate();;
    private int month = today.getMonth();
    private int year = today.getYear() + 1900;

    /**
     * Instantiate the fragment
     * @param savedInstanceState
     *  This is the previously saved state of the fragment
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nav = NavHostFragment.findNavController(this);

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
        geoSwitch = binding.geoSwitch; //switch

        Bundle args = getArguments();
        if (args != null) {
            editing = true;

            //geolocation only available during creation
            geoSwitch.setEnabled(false); // cannot toggle geo after creation


            String path = args.getString("eventDocRefPath");
            assert path != null;
            eventDocRef = FirebaseFirestore.getInstance().document(path);

            eventDocRef.get().addOnSuccessListener(doc -> {
                event = doc.toObject(Event.class);
                assert event != null;
                geoSwitch.setChecked(event.isGeoEnabled()); // show current value
                fillTextViews(event);
            });

        }

        /**
         * This creates a photo picker activity
         */
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Called if the user picks a photo
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        //Add code to save the photo into the database
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte [] bytes = stream.toByteArray();
                            String imageString = Base64.encodeToString(bytes, Base64.DEFAULT);
                            eventImageString = imageString;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
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

        binding.buttonForGoingBack.setOnClickListener(v -> nav.popBackStack());


        binding.buttonForConfirm.setOnClickListener(new View.OnClickListener() {
            /**
             * On pressing the confirm button, create the event
             * @param v
             *  The view that was clicked.
             */
            @Override
            public void onClick(View v) {

                try {
                    createEvent(eventName, eventDate, eventDeadline, eventLocation, eventDescription, eventCapacity, optionalLimit, eventTime, eventImageString);
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
                             String eventImageString) throws ParseException {

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
            event.setImageString(eventImageString);
            if (limit != -1) {
                event.setWaitlistLimit(limit);
            }
            geoEnabled = geoSwitch.isChecked(); //geolocation
            eventDocRef.set(event, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        SharedEventViewModel vm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
                        vm.setEvent(event);

                        nav.navigate(R.id._fragment_organizer_view_event);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error updating event", e);
                        if (!nav.popBackStack(R.id.fragment_main, false)) {
                            nav.navigate(R.id.fragment_main);
                        }
                    });
        }
        else {
            DocumentReference eventRef = db.collection("Events").document();
            String eventId = eventRef.getId();
            Event newEvent;
            newEvent = new Event(name, location, deadline, description, date, time, capacity, limit, user.getDocRef(), eventRef, eventId, eventImageString, geoSwitch.isChecked());

            eventRef.set(newEvent)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Event added with ID: " + eventId);

                        user.addCreatedEvent(eventRef);

                        user.getDocRef().update("userCreatedEvents", FieldValue.arrayUnion(eventRef))
                            .addOnSuccessListener(v -> {
                                //user.updateDB(() -> {
                                    SharedEventViewModel vm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
                                    vm.setEvent(newEvent);
                                    nav.navigate(R.id._fragment_organizer_view_event);
                                //});
                            });


                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error adding event", e);
                        if (!nav.popBackStack(R.id.fragment_main, false)) {
                            nav.navigate(R.id.fragment_main);
                        }
                    });

        }

    }

    /**
     * event date picker, sets binding textview
     */
    private void openDateDialogue() {
        DatePickerDialog dateDialog;
        dateDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String format = year + "-" + (month+1) + "-" + day;
                binding.inputFieldForCreateEventDate.setText(format);
            }

        }, year, month, day);

        dateDialog.show();

    }

    /**
     * deadline date picker, sets binding textview
     */
    private void openDeadlineDialogue() {
        DatePickerDialog dateDialog;
        dateDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String format = year + "-" + (month+1) + "-" + day;
                binding.inputFieldForCreateEventRegistrationDeadline.setText(format);
            }

        }, year, month, day);

        dateDialog.show();

    }
}

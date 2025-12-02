package com.example.camaraderie.event_screen.organizer_view;

import static com.example.camaraderie.image_stuff.ImageHandler.deleteEventImage;
import static com.example.camaraderie.image_stuff.ImageHandler.uploadEventImage;
import static com.example.camaraderie.main.MainActivity.user;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentCreateEventBinding;
import com.example.camaraderie.image_stuff.ImageHandler;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
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
    private TextView eventDateTime;
    private TextView eventDeadlineTime;
    private EditText optionalLimit;
    private Switch geoSwitch;
    private boolean geoEnabled;
    private NavController nav;

    private String eventImageString;
    private Uri imageUri;
    private boolean editing = false;
    private boolean image_uploaded = false;
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
        eventDateTime = binding.inputFieldForCreateEventTime;
        eventDeadlineTime = binding.inputFieldForCreateEventRegistrationTime;
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

        /*
         * This creates a photo picker activity
         */
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Called if the user picks a photo
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        imageUri = uri;
                        image_uploaded = true;
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

        binding.inputFieldForCreateEventTime.setOnClickListener(new View.OnClickListener() {
            /**
             * sets time dialogfragment
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) { openTimeDialog(binding.inputFieldForCreateEventTime); }
        });

        binding.inputFieldForCreateEventRegistrationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog(binding.inputFieldForCreateEventRegistrationTime);
            }
        });

        binding.buttonForGoingBack.setOnClickListener(v -> nav.popBackStack());

        binding.buttonForConfirm.setEnabled(true);
        binding.buttonForConfirm.setOnClickListener(new View.OnClickListener() {
            /**
             * On pressing the confirm button, create the event
             * @param v
             *  The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                binding.buttonForConfirm.setEnabled(false);  // just do this to stop uer form pressing a billion times

                try {
                    createEvent(eventName, eventDate, eventDeadline, eventLocation, eventDescription, eventCapacity, optionalLimit, eventDateTime, eventDeadlineTime, eventImageString);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Please fill out all necessary fields", Toast.LENGTH_SHORT).show();
                    binding.buttonForConfirm.setEnabled(true);
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
        String date = (event.getEventDate().getYear()+1900) + "-" + (event.getEventDate().getMonth()+1) + "-" + event.getEventDate().getDay();
        eventDate.setText(date);
        String deadline = (event.getRegistrationDeadline().getYear()+1900) + "-" + (event.getRegistrationDeadline().getMonth()+1) + "-" + event.getRegistrationDeadline().getDay();
        eventDeadline.setText(deadline);
        eventLocation.setText(event.getEventLocation());
        eventDescription.setText(event.getDescription());
        eventCapacity.setText(String.valueOf(event.getCapacity()));
        eventDateTime.setText(event.getEventDateTime());
        if (event.getWaitlistLimit() != -1) {
            optionalLimit.setText(String.valueOf(event.getWaitlistLimit()));
        }
        eventDeadlineTime.setText(event.getEventDeadlineTime());
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
     * @param eventDateTime new time
     * @throws ParseException throws parseException if parse fails (mainly date)
     */
    private void createEvent(EditText eventName,
                             TextView eventDate,
                             TextView eventDeadline,
                             EditText eventLocation,
                             EditText eventDescription,
                             EditText eventCapacity,
                             EditText optionalLimit,
                             TextView eventDateTime,
                             TextView eventDeadlineTime,
                             String eventImageString) throws ParseException {

        String name = eventName.getText().toString();
        String description = eventDescription.getText().toString();
        String location = eventLocation.getText().toString();
        String dateTime = eventDateTime.getText().toString();
        String deadlineTime = eventDeadlineTime.getText().toString();
        int capacity = Integer.parseInt(eventCapacity.getText().toString());
        int limit = -1;  // false false
        if (!optionalLimit.getText().toString().isEmpty()){
            limit = Integer.parseInt(optionalLimit.getText().toString());
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date deadline =  formatter.parse(eventDeadline.getText().toString());
        Date date = formatter.parse(eventDate.getText().toString());
        int hours = Integer.parseInt(dateTime.substring(0, 2));
        int minutes = Integer.parseInt(dateTime.substring(3, 5));
        date.setHours(hours);
        date.setMinutes(minutes);
        int dhours = Integer.parseInt(deadlineTime.substring(0, 2));
        int dminutes = Integer.parseInt(deadlineTime.substring(3, 5));
        deadline.setHours(dhours);
        deadline.setMinutes(dminutes);

        // NEED TO GET TIME, AND CREATE EVENT ID
        // validate user input and store in database.
        // DO NOT LEAK THE DB BY DOCUMENT INJECTION BY ACCIDENT

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (editing) {
            event.setEventName(name);
            event.setEventDescription(description);
            event.setEventLocation(location);
            event.setEventDateTime(dateTime);
            event.setEventDeadlineTime(deadlineTime);
            event.setRegistrationDeadline(deadline);
            event.setEventDate(date);
            event.setCapacity(capacity);
            if (limit != -1) {
                event.setWaitlistLimit(limit);
            }
            geoEnabled = geoSwitch.isChecked(); //geolocation
            eventDocRef.set(event, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        SharedEventViewModel vm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
                        vm.setEvent(event);

                        deleteEventImage(event);

                        // if no image was chosen
                        if (image_uploaded) {
                            uploadEventImage(event, imageUri, new ImageHandler.UploadCallback() {
                                @Override
                                public void onSuccess(String downloadUrl) {
                                    event.setImageUrl(downloadUrl);
                                    event.updateDB(() -> Toast.makeText(getContext(), "Image saved", Toast.LENGTH_SHORT).show());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.e("UPLOAD", "Failed", e);
                                    Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

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
            newEvent = new Event(name, location, deadline, description, date, dateTime, deadlineTime, capacity, limit, user.getDocRef(), eventRef, eventId, geoSwitch.isChecked());

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

                        if (image_uploaded) {
                            uploadEventImage(newEvent, imageUri, new ImageHandler.UploadCallback() {
                                @Override
                                public void onSuccess(String downloadUrl) {
                                    newEvent.setImageUrl(downloadUrl);
                                    newEvent.updateDB(() -> Toast.makeText(getContext(), "Image saved", Toast.LENGTH_SHORT).show());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.e("UPLOAD", "Failed", e);
                                    Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


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

        dateDialog.getDatePicker().setMinDate(today.getTime() + 2 * 86_400_000L);  // set min to 2 days from now
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date deadline = formatter.parse(binding.inputFieldForCreateEventRegistrationDeadline.getText().toString());
            long longDeadline = (deadline.getTime() + 86_400_000L);
            dateDialog.getDatePicker().setMinDate(longDeadline);
        } catch (Exception e) {}
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
        dateDialog.getDatePicker().setMinDate(today.getTime() + 86_400_000L);  // set min deadline to tmr
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(binding.inputFieldForCreateEventDate.getText().toString());
            long longDate = (date.getTime() - 86_400_000L);  // if date is defined, then its already 2 days from now
            dateDialog.getDatePicker().setMaxDate(longDate);
        } catch (Exception e) {

        }

        dateDialog.show();

    }

    private void openTimeDialog(TextView arg) {
        TimePickerDialog timeDialog;
        timeDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String strHourOfDay = "" + hourOfDay;
                String strMinute = "" + minute;
                if(hourOfDay < 10) {
                    strHourOfDay = "0" + hourOfDay;
                }
                if(minute < 10) {
                    strMinute = "0" + minute;
                }
                String format = strHourOfDay + ":" + strMinute;
                arg.setText(format);
            }

        }, 0, 0, true);
        timeDialog.show();
    }
}

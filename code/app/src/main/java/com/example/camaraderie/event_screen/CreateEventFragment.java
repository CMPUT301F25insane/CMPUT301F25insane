package com.example.camaraderie.event_screen;

import android.os.Bundle;
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

import com.example.camaraderie.R;
import com.example.camaraderie.databinding.FragmentCreateEventTestingBinding;

/**
 *
 */
public class CreateEventFragment extends Fragment {

    private FragmentCreateEventTestingBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentCreateEventTestingBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

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
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(CreateEventFragment.this)
                        .navigate(R.id.action_fragment_create_event_testing_to_fragment_main);

            }
        });

        binding.createEventConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eventName = binding.createEventName;
                EditText eventDate = binding.createEventDate;
                EditText eventDeadline = binding.createEventDeadline;
                EditText eventLocation = binding.createEventLocation;
                EditText eventDescription = binding.createEventDescription;
                EditText eventCapacity = binding.createEventCapacity;

                try {
                    createEvent(eventName, eventDate, eventDeadline, eventLocation, eventDescription, eventCapacity);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

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
                             EditText eventCapacity) throws ParseException {

        String name = eventName.getText().toString();
        String description = eventDescription.getText().toString();
        String location = eventLocation.getText().toString();
        int capacity = Integer.parseInt(eventCapacity.getText().toString());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date deadline =  formatter.parse(eventDeadline.getText().toString());
        Date date = formatter.parse(eventDate.getText().toString());

        // NEED TO GET TIME, AND PASS HOST AND CREATE EVENT ID
        // validate user input and store in database.
        // DO NOT LEAK THE DB BY DOCUMENT INJECTION BY ACCIDENT




        // navigate back ON SUCCESS
        NavHostFragment.findNavController(CreateEventFragment.this).navigate(R.id.action_fragment_create_event_testing_to_fragment_main);
    }
}

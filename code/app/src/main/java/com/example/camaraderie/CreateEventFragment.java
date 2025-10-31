package com.example.camaraderie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.databinding.FragmentCreateEventTestingBinding;

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

                createEvent(eventName, eventDate, eventDeadline, eventLocation, eventDescription, eventCapacity);
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
                             EditText eventCapacity) {
        // validate user input and store in database.
        // DO NOT LEAK THE DB BY DOCUMENT INJECTION BY ACCIDENT




        // navigate back ON SUCCESS
        NavHostFragment.findNavController(CreateEventFragment.this).navigate(R.id.action_fragment_create_event_testing_to_fragment_main);
    }
}

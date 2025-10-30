package com.example.camaraderie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.camaraderie.databinding.CreatedEventBinding;

public class CreatedEventFragment extends Fragment {

    private CreatedEventBinding binding;
    private Event event;
    private User user;
    private static final String ARG_EVENT = "event";
    private static final String ARG_USER = "user";

    // Factory method to create a new instance with Event
    public static CreatedEventFragment newInstance(Event event, User user) {
        CreatedEventFragment fragment = new CreatedEventFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT, event);
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = CreatedEventBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        event = (Event) getArguments().getSerializable(ARG_EVENT);
        user = (User) getArguments().getSerializable(ARG_USER);

        fillTextViews(event);

        binding.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.getWaitlist().addUserToWaitlist(user);
            }
        });
    }

    private void fillTextViews(Event event) {
        binding.eventName.setText(event.getEventName());
        binding.description.setText(event.getDescription());
        binding.registrationDeadline.setText(event.getRegistrationDeadline());
        binding.appName.setText("Comaraderie");
        binding.dateAndTime.setText(event.getEventTime());
        binding.location.setText(event.getEventLocation()); //NEED TO CHANGE THIS WHEN GEOLOCATION STUFF IS IMPLEMENTED
        binding.organizerName.setText(event.getHost().getFirstName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package com.example.camaraderie.event_screen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import static com.example.camaraderie.MainActivity.user;
import com.example.camaraderie.databinding.FragmentViewEventUserBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserViewEventFragment extends Fragment {

    private FirebaseFirestore db;

    private FragmentViewEventUserBinding binding;
    private DocumentReference event;
    private static final String ARG_EVENT = "event";
    private static final String ARG_USER = "user";
    private String eventName;
    private String description;
    private String deadline;
    private String dateAndTime;
    private String location;
    private DocumentReference hostDocRef;
    private DocumentReference waitlistDocRef;
    private String hostName;
/**
    public static UserViewEventFragment newInstance(String event, String user) {
        UserViewEventFragment fragment = new UserViewEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT, event);
        args.putString(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }
 */

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentViewEventUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String eventPath;
        String userPath;

        eventPath = (String) getArguments().getSerializable(ARG_EVENT);
        userPath = (String)  getArguments().getSerializable(ARG_USER);

        db = FirebaseFirestore.getInstance();
        event = db.document(eventPath);
        userPath = user.getUserId();

        fillTextViews(event);

        binding.joinButtonUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("here", "somehow");
                waitlistDocRef.update("users", FieldValue.arrayUnion(user)).addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "You have joined the event", Toast.LENGTH_SHORT).show();
                });
            }

        });

        binding.unjoinButtonUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitlistDocRef.update("users", FieldValue.arrayRemove(user)).addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "You have left the event", Toast.LENGTH_SHORT).show();
                });
            }
        });

        binding.dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    // NEEDS TO BE CHANGED WHEN THE EVENT DATABASE OBJECTS ARE CREATED
    private void fillTextViews(DocumentReference event) {

        Log.d("Huge", "NOthing works" + event);

        event.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Log.d("Here","Event has nothing???");
                if (documentSnapshot.exists()){
                    eventName = documentSnapshot.getString("eventName");
                    description = documentSnapshot.getString("description");
                    deadline = documentSnapshot.getString("registrationDeadline");
                    dateAndTime = documentSnapshot.getString("dateAndTime");
                    location = documentSnapshot.getString("eventLocation");
                }
            }
        });

        binding.eventNameForUserView.setText(eventName);
        binding.eventDescriptionUserView.setText(description);
        binding.registrationDeadlineTextUserView.setText(deadline);
        binding.userEventViewEventDate.setText(dateAndTime);
        binding.locationOfUserView.setText(location); //NEED TO CHANGE THIS WHEN GEOLOCATION STUFF IS IMPLEMENTED
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
